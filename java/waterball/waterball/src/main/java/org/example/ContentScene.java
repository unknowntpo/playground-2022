package org.example;

public class ContentScene extends Scene   {
    public ContentScene(String name, int number, int expAward) {
        super(name, number, expAward);
    }

    @Override
    public int calculateExpAward() {
        return (int)(expAward * 1.5);
    }
}
