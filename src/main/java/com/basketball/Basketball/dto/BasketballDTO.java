package com.basketball.Basketball.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class BasketballDTO {

    @NotBlank(message = "Name should not be empty or null")
    private String name;
    @NotBlank(message = "Team should not be empty or null")
    private String team;
    @NotBlank(message = "Coach should not be empty or null")
    private String coach;
    @NotNull(message = "Points should not be null")
    private Double points;

    public BasketballDTO(String name, String team, String coach, Double points) {
        this.name = name;
        this.team = team;
        this.coach = coach;
        this.points = points;
    }

    public BasketballDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "BasketballDTO{" +
                "name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", coach='" + coach + '\'' +
                ", points=" + points +
                '}';
    }
}
