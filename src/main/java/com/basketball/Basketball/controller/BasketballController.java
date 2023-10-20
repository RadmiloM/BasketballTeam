package com.basketball.Basketball.controller;


import com.basketball.Basketball.converter.BasketballMapper;
import com.basketball.Basketball.dto.BasketballDTO;
import com.basketball.Basketball.entity.Basketball;
import com.basketball.Basketball.service.BasketballServiceImpl;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/basketball")
public class BasketballController {

    private static final Logger LOGGER =LogManager.getLogger(BasketballController .class);
    private final Bucket bucket;

    private final BasketballServiceImpl basketballServiceImpl;

    private final BasketballMapper basketballMapper;
    @Value("${spring.uri}")
    private String uri;


    public BasketballController(BasketballServiceImpl basketballService, BasketballMapper basketballMapper) {
        this.basketballServiceImpl = basketballService;
        this.basketballMapper = basketballMapper;
        var limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody BasketballDTO basketballDTO) throws URISyntaxException {
        LOGGER.info("Create method start");
        var basketball = basketballMapper.mapToEntity(basketballDTO);
        basketballServiceImpl.create(basketball);
        LOGGER.info("Create method before response");
        return ResponseEntity.created(new URI(uri)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Basketball> getBasketball(@PathVariable Integer id) {
        LOGGER.info("Method getBasketball called and started");
        if (bucket.tryConsume(1)) {
            var basketballTeam = basketballServiceImpl.get(id);
            return ResponseEntity.ok(basketballTeam);
        }
        LOGGER.info("Method getBasketball before response");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        LOGGER.info("Method delete was called");
        basketballServiceImpl.delete(id);
        LOGGER.info("Before response method");
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody BasketballDTO basketballDTO) {
        LOGGER.info("Method update was called and started");
        if (bucket.tryConsume(1)) {
            var basketball = basketballMapper.mapToEntity(basketballDTO);
            basketballServiceImpl.update(basketball, id);
            return ResponseEntity.ok().build();
        }
        LOGGER.info("Method update before response");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @GetMapping
    public List<Basketball> findAll() {
        LOGGER.info("Method find all was called");
        return basketballServiceImpl.getAll();
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<Basketball>> getBasketballsWithPagination(@RequestParam Integer offset, @RequestParam Integer pageSize) {
        LOGGER.info("Method getBasketballsWithPagination was called");
        Page<Basketball> basketballsWithPagination = basketballServiceImpl.getBasketballsWithPagination(offset, pageSize);
        LOGGER.info("Method getBasketballsWithPagination before response");
        return ResponseEntity.ok(basketballsWithPagination);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Basketball>> getBasketballsWithSort() {
        LOGGER.info("Method getBasketballsWithSort was called");
        List<Basketball> sortedBasketballTeams = basketballServiceImpl.getBasketballsWithSort();
        LOGGER.info("Method getBasketballsWithSort before response");
        return ResponseEntity.ok(sortedBasketballTeams);
    }

    @GetMapping("/test")
    public ResponseEntity<Long> test() {
        return ResponseEntity.ok(123l);
    }
}
