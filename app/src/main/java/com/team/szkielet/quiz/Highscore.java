package com.team.szkielet.quiz;

public class Highscore {
    private int highscore;
    private String email;

    public Highscore(int highscore, String email) {
        this.highscore = highscore;
        this.email = email;
    }

    public int getHighscore() {
        return highscore;
    }

    public String getEmail() {
        return email;
    }
}
