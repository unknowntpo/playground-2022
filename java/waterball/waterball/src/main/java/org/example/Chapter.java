package org.example;

import java.util.List;
import java.util.Objects;

public class Chapter {
    private String name;
    private int number;

    private List<Mission> missions;


    public Chapter(int number, String name, List<Mission> missions) {
        setName(name);
        setNumber(number);
        setMissions(missions);
    }

    public void setName(String name) {
        this.name = ValidationUtils.lengthShouldBe(name, 1, 30);
    }

    public void setNumber(int number) {
        this.number = ValidationUtils.shouldBePositive(number);
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = Objects.requireNonNull(missions);
    }

    public Mission getFirstMission() {
        return missions.get(0);
    }
}
