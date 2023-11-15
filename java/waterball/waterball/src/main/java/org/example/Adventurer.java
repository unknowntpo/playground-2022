package org.example;

public class Adventurer {


    private int number;
    private Student student;


    private Journey journey;

    public TourGroup getTourGroup() {
        return tourGroup;
    }

    public void setTourGroup(TourGroup tourGroup) {
        this.tourGroup = tourGroup;
    }

    private TourGroup tourGroup;

    public Adventurer(int number, Student student, Journey journey) {
        setNumber(number);
        setStudent(student);
        setJourney(journey);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = ValidationUtils.shouldBePositive(number);
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public void carryOn(Mission mission) {
        student.carryOn(mission);
    }
}
