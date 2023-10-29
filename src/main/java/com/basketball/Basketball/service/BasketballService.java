package com.basketball.Basketball.service;

import com.basketball.Basketball.entity.Basketball;
import com.basketball.Basketball.exception.NoBasketballTeamWithProvidedId;
import com.basketball.Basketball.repository.BasketballRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BasketballService {

    private final BasketballRepository basketballRepository;
    private final EmailSenderService emailSenderService;

    public BasketballService(BasketballRepository basketballRepository, EmailSenderService emailSenderService) {
        this.basketballRepository = basketballRepository;
        this.emailSenderService = emailSenderService;
    }
    @Transactional
    public void create(Basketball basketball) {
        basketballRepository.save(basketball);
        emailSenderService.sendSimpleMessage("markovicradmilo35@gmail.com"
                , " team creation",
                "Basketball team: " + basketball.getTeam() + "\n" +
                        " name: " + basketball.getName() + "\n" +
                        " coach: " + basketball.getCoach() + "\n" +
                        " points " + basketball.getPoints() + "\n" +
                        " was successfully created");

    }

    public void delete(Integer id) {
        var optionalBasketballTeamDB = basketballRepository.findById(id);
        if (optionalBasketballTeamDB.isEmpty()) {
            throw new NoBasketballTeamWithProvidedId("Basketball team with id " + id + " is not present in database");
        }
        basketballRepository.deleteById(id);
    }

    public void update(Basketball basketball, Integer id) {
        var optionalBasketballTeamDB = basketballRepository.findById(id);
        if (optionalBasketballTeamDB.isEmpty()) {
            throw new NoBasketballTeamWithProvidedId("Basketball team with id " + id + " is not present in database");
        }

        var currentBasketballTeam = optionalBasketballTeamDB.get();
        if (!StringUtils.isBlank(basketball.getName())) {
            currentBasketballTeam.setName(basketball.getName());
        }

        if (!StringUtils.isBlank(basketball.getTeam())) {
            currentBasketballTeam.setTeam(basketball.getTeam());
        }

        if (!StringUtils.isBlank(basketball.getCoach())) {
            currentBasketballTeam.setCoach(basketball.getCoach());
        }

        if (basketball.getPoints() != null) {
            currentBasketballTeam.setPoints(basketball.getPoints());
        }
        basketballRepository.save(currentBasketballTeam);
    }

    public Basketball get(Integer id) {
        Optional<Basketball> optionalBasketball = basketballRepository.findById(id);
        if (optionalBasketball.isEmpty()) {
            throw new NoBasketballTeamWithProvidedId("Basketball team with id " + id + " is not present in database");
        }
        return optionalBasketball.get();
    }

    public List<Basketball> getAll() {
        return basketballRepository.findAll();
    }

    public Page<Basketball> getBasketballsWithPagination(Integer offset, Integer pageSize) {
        return basketballRepository.findAll(PageRequest.of(offset, pageSize));
    }

    public List<Basketball> getBasketballsWithSort() {
        return basketballRepository.findAll(Sort.by("points").ascending());
    }


}
