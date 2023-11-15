package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.example.ValidationUtils.*;

public class Journey {

    private String name;
    private String description;
    private BigDecimal price;
    private List<Chapter> chapters;

    private List<Adventurer> adventurers;

    public List<TourGroup> getTourGroups() {
        return tourGroups;
    }

    public void setTourGroups(List<TourGroup> tourGroups) {
        this.tourGroups = Objects.requireNonNull(tourGroups);
    }

    private List<TourGroup> tourGroups;

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public Journey(
            String name,
            String description,
            BigDecimal price,
            List<Chapter> chapters,
            List<Adventurer> adventurers,
            List<TourGroup> tourGroups
    ) {
        setChapters(chapters);
        setName(name);
        setDescription(description);
        setPrice(price);
        setAdventurers(adventurers);
        setTourGroups(tourGroups);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = ValidationUtils.lengthShouldBe(name, 1, 30);
    }

    public void setDescription(String description) {
        this.description = ValidationUtils.lengthShouldBe(description, 0, 300);
    }

    public void setPrice(BigDecimal price) {
        this.price = ValidationUtils.shouldBeBiggerThan(price, 1);
    }

    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    public void setAdventurers(List<Adventurer> adventurers) {
        this.adventurers = Objects.requireNonNull(adventurers);
    }

    public Adventurer join(Student student) {
        int number = adventurers.size() + 1;

        // Establish dual relation between adventurer and student
        Adventurer adventurer = new Adventurer(number, student, this);
        adventurers.add(adventurer);
        student.getAdventurers().add(adventurer);

        // Start first mission
        Mission firstMission = getFirstMission();
        adventurer.carryOn(firstMission);

        // Match to a TourGroup
        TourGroup tourGroup = matchTourGroup(adventurer);
        tourGroup.add(adventurer);
        System.out.printf("[Journey]: Adventurer %s joined Journey %s -> Matched to TourGroup %s\n",
                student.getAccount(),
                getName(),
                tourGroup.getNumber()
        );

        return adventurer;
    }

    private TourGroup matchTourGroup(Adventurer adventurer) {
        if (!tourGroups.isEmpty()) {
            return tourGroups.get((int) (Math.random() * tourGroups.size()));
        }
        return new TourGroup(1, new ArrayList<>(List.of(adventurer)));
    }

    private Mission getFirstMission() {
        return getChapters().get(0).getFirstMission();
    }
}