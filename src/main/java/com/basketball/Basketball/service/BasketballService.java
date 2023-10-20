package com.basketball.Basketball.service;

import com.basketball.Basketball.entity.Basketball;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BasketballService {

    void create(Basketball basketball);

    void delete(Integer id);

    void update(Basketball basketball, Integer id);

    Basketball get(Integer id) throws NoSuchFieldException;
    List<Basketball> getAll();


}
