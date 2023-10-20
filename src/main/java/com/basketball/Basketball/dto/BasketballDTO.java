package com.basketball.Basketball.dto;

public class BasketballDTO {


    private String name;
    private String team;
    private String coach;
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
}
