package org.example;

import java.util.List;
import java.util.Objects;

public class Mission {
    private  String name ;
    private int number;

    private Challenge challenge;



    private List<Scene> scenes;


    public Mission(int number, String name, Challenge challenge, List<Scene> scenes) {
        setName(name);
        setNumber(number);
        setChallenge(challenge);
        setScenes(scenes);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = ValidationUtils.lengthShouldBe(name, 1, 30);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = ValidationUtils.shouldBePositive(number);
    }

    public Challenge getChallenge() {
        return challenge;
    }
    public void setChallenge(Challenge challenge) {
        this.challenge = Objects.requireNonNull(challenge);
    }


    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = Objects.requireNonNull(scenes);
    }



    public int calculateExpAward() {
        // TODO: Should be sum of exps in all scene under this Mission.
        int sum  = 0;
        for (Scene s: scenes) {
            sum += s.calculateExpAward();
        }
        return sum;
    }
}
