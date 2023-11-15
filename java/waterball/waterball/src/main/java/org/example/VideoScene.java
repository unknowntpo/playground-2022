package org.example;

public class VideoScene extends Scene{
    public VideoScene(String name, int number, int expAward) {
        super(name, number, expAward);
    }

    @Override
    public int calculateExpAward() {
        // VideoScene specific
        return (int) (expAward * 1.1);
    }
}
