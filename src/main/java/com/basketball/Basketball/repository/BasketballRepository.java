package com.basketball.Basketball.repository;

import com.basketball.Basketball.entity.Basketball;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketballRepository extends JpaRepository<Basketball, Integer> {

    List<Basketball> findByPointsContaining(Double points);

}
