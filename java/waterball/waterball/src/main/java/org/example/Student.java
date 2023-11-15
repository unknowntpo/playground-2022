package org.example;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Student {
    private final static LevelSheet LEVEL_SHEET = new LevelSheet();
    private String account;
    private String password;
    private int level = 1;
    private int exp = 0;
    private List<MissionCarryOn> missionCarryOns;

    private List<Adventurer> adventurers;

    public String getAccount() {
        return account;
    }

    public Student(
            String account,
            String password,
            List<MissionCarryOn> missionCarryOns,
            List<Adventurer> adventurers
            ) {
        setAccount(account);
        setPassword(password);
        setMissionCarryOns(missionCarryOns);
        setAdventurers(adventurers);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMissionCarryOns(List<MissionCarryOn> missionCarryOns) {
        this.missionCarryOns = Objects.requireNonNull(missionCarryOns);
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public void gainExp(int exp) {
        this.exp += exp;
        int newLevel = LEVEL_SHEET.query(this.exp);
        int levelUp = newLevel - level;
        System.out.printf("[AWARD]: Student %s gain exp %d\n", account, exp);
        for (int i = 0; i < levelUp; i++) {
            levelUp();
        }
    }

    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    public void setAdventurers(List<Adventurer> adventurers) {
        this.adventurers = Objects.requireNonNull(adventurers);
    }

    public void levelUp() {
        this.level++;
        System.out.printf("[AWARD]: Level Up! Student %s leveled up to level %d\n", account, level);

    }


    ;

    public MissionCarryOn carryOn(Mission mission) {
        System.out.printf("[Mission]: Student %s started mission %s\n", getAccount(), mission.getName());
        MissionCarryOn missionCarryOn = new MissionCarryOn(this, mission);
        missionCarryOns.add(missionCarryOn);
        return missionCarryOn;
    }

    public List<MissionCarryOn> getMissionCarryOns() {
        return missionCarryOns;
    }

}
