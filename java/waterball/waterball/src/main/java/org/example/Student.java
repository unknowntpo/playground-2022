package org.example;

public class Student {
    private final static LevelSheet LEVEL_SHEET =new LevelSheet();
    private String account;
    private String password;
    private int level = 1;
    private int exp = 0;

    public String getAccount() {
        return account;
    }

    public Student(String account, String password) {
        this.account = account;
        this.password = password;
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

    public void levelUp() {
        this.level++;
        System.out.printf("[AWARD]: Level Up! Student %s leveled up to level %d\n", account, level );

    }


    ;

    public MissionCarryOn carryOn(Mission mission) {
        reutrn new MissionCarryOn()
    }

    ;
}
