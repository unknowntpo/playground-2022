package org.example;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TourGroup {
    private int  number ;

    public void setAdventurers(List<Adventurer> adventurers) {
        this.adventurers = Objects.requireNonNull(adventurers);
        for (Adventurer adventurer: adventurers) {
            adventurer.setTourGroup(this);
        }
    }

    private List<Adventurer> adventurers;

    public int getNumber() {
        return number;
    }

    public TourGroup(int number, List<Adventurer> adventurers) {
        this.number = number;
        setAdventurers(adventurers);
    }

    public void setNumber(int number) {
        this.number = ValidationUtils.shouldBePositive(number);
    }

    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    public void add(Adventurer adventurer) {
        adventurers.add(adventurer);
        adventurer.setTourGroup(this);
    }
}
