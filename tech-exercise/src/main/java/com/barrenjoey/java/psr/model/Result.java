package com.barrenjoey.java.psr.model;

import java.util.List;

public class Result {
    private boolean tie;
    private List<Player> players;
    private boolean indecisive;

    public boolean isTie() {
        return tie;
    }

    public void setTie(boolean tie) {
        this.tie = tie;
    }

    public List<Player> getPlayer() {
        return players;
    }

    public void setPlayer(List<Player> players) {
        this.players = players;
    }

    public static Result tieResult() {
        Result result = new Result();
        result.setTie(true);
        return result;
    }

    public boolean isIndecisive() {
        return indecisive;
    }

    public void setIndecisive(boolean indecisive) {
        this.indecisive = indecisive;
    }
}
