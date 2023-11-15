package org.example;

public class MissionCarryOn {

    private State state = State.ONGOING;
    private Student student;
    private Mission mission;

    public Object getMissionName() {
        return mission.getName();
    }

    public enum State {
        ONGOING, COMPLETED
    }

    public MissionCarryOn(Student student, Mission mission) {
        this.student = student;
        this.mission = mission;
    }

    public State getState() {
        return state;
    }

    public Student getStudent() {
        return student;
    }

    public Mission getMission() {
        return mission;
    }

    public void complete() {
        this.state = State.COMPLETED;
        System.out.printf("[MISSION]: Student %s has already completed mission '%s'", student.getAccount(), mission.getName());
        student.gainExp(mission.calculateExpAward());
    }
}
