package com.basketball.Basketball.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Basketball {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String team;
    private String coach;
    private Double points;

    public Basketball(Integer id, String name, String team, String coach, Double points) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.coach = coach;
        this.points = points;
    }

    public Basketball() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getCoach() {
        return coach;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Double getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Basketball{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", coach='" + coach + '\'' +
                ", points=" + points +
                '}';
    }
}
