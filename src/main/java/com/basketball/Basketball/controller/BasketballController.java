package com.basketball.Basketball.controller;


import com.basketball.Basketball.converter.BasketballMapper;
import com.basketball.Basketball.dto.BasketballDTO;
import com.basketball.Basketball.entity.Basketball;
import com.basketball.Basketball.entity.ExternalBasketballTeams;
import com.basketball.Basketball.exception.NoBasketballTeamWithProvidedId;
import com.basketball.Basketball.security.BasketballSecurity;
import com.basketball.Basketball.service.BasketballService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/basketball")
public class BasketballController {

    private static final Logger LOGGER = LogManager.getLogger(BasketballController.class);


    private final Bucket bucket;
    private final BasketballSecurity basketballSecurity;
    private final WebClient webClient;
    private final BasketballService basketballService;

    private final BasketballMapper basketballMapper;
    @Value("${spring.uri}")
    private String uri;


    public BasketballController(
            BasketballSecurity basketballSecurity,
            WebClient.Builder webClientBuilder,
            BasketballService basketballService,
            BasketballMapper basketballMapper) {
        this.basketballSecurity = basketballSecurity;
        this.webClient = webClientBuilder.baseUrl("https://basketball-data.p.rapidapi.com/tournament/teams").build();
        this.basketballService = basketballService;
        this.basketballMapper = basketballMapper;
        var limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid BasketballDTO basketballDTO) throws URISyntaxException {
        LOGGER.info("Create method start");
        var basketball = basketballMapper.mapToEntity(basketballDTO);
        basketballService.create(basketball);
        LOGGER.info("Create method before response");
        return ResponseEntity.created(new URI(uri)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Basketball> getBasketball(@PathVariable Integer id) {
        LOGGER.info("Method getBasketball called and started");
        if (bucket.tryConsume(1)) {
            var basketballTeam = basketballService.get(id);
            return ResponseEntity.ok(basketballTeam);
        }
        LOGGER.info("Method getBasketball before response");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        LOGGER.info("Method delete was called");
        basketballService.delete(id);
        LOGGER.info("Before response method");
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody BasketballDTO basketballDTO) {
        LOGGER.info("Method update was called and started");
        if (bucket.tryConsume(1)) {
            var basketball = basketballMapper.mapToEntity(basketballDTO);
            basketballService.update(basketball, id);
            return ResponseEntity.ok().build();
        }
        LOGGER.info("Method update before response");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping
    public List<Basketball> findAll() {
        LOGGER.info("Method find all was called");
        return basketballService.getAll();
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<Basketball>> getBasketballsWithPagination(@RequestParam Integer offset, @RequestParam Integer pageSize) {
        LOGGER.info("Method getBasketballsWithPagination was called");
        Page<Basketball> basketballsWithPagination = basketballService.getBasketballsWithPagination(offset, pageSize);
        LOGGER.info("Method getBasketballsWithPagination before response");
        return ResponseEntity.ok(basketballsWithPagination);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Basketball>> getBasketballsWithSort() {
        LOGGER.info("Method getBasketballsWithSort was called");
        List<Basketball> sortedBasketballTeams = basketballService.getBasketballsWithSort();
        LOGGER.info("Method getBasketballsWithSort before response");
        return ResponseEntity.ok(sortedBasketballTeams);
    }

    @GetMapping("/external-api")
    public Mono<ResponseEntity<List<ExternalBasketballTeams>>> getExternalApiData() {
        HttpHeaders headers = basketballSecurity.getHttpHeaders();
        return webClient
                .get()
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToFlux(ExternalBasketballTeams.class)
                .collectList()
                .map(ResponseEntity::ok);
    }


    @GetMapping("/test")
    public ResponseEntity<Collection<Basketball>> test() {
        List<Basketball> result = List.of(new Basketball(11, "Rade", "Test", "Muu", 22.3),
                new Basketball(14, "Raki", "Joda", "hello", 11.3),
                new Basketball(13,"Rade","Ilic","roki",22.3),
                new Basketball());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/send")
    public ResponseEntity<List<BasketballDTO>> sent(@RequestBody List<BasketballDTO> basketballDTOS) {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Radmilo");
        for (BasketballDTO basketballDTO : basketballDTOS) {
            if (map.containsValue(basketballDTO.getName())) {
                throw new NoBasketballTeamWithProvidedId("Value is already present in the map " + basketballDTO.getName());
            }
            System.out.println("Basketball name: " + basketballDTO.getName());
            System.out.println("Basketball team: " + basketballDTO.getTeam());
            System.out.println("Basketball coach: " + basketballDTO.getCoach());
            System.out.println("Basketball points: " + basketballDTO.getPoints());
        }
        return ResponseEntity.ok(basketballDTOS);
    }



}
