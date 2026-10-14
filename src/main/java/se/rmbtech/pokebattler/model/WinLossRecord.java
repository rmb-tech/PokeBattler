package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WinLossRecord {
    private int wins;
    private int losses;

    public void add(boolean won) {
        if (won) {
            wins++;
        } else {
            losses++;
        }
    }

    public int timesUsed() {
        return wins + losses;
    }

    public double winPercentage() {
        return timesUsed() == 0 ? 0 : (wins * 100.0) / timesUsed();
    }

    public int getWins() { return wins; }
    public int getLosses() { return losses; }

    public void setWins(int wins) { this.wins = Math.max(0, wins); }
    public void setLosses(int losses) { this.losses = Math.max(0, losses); }
}