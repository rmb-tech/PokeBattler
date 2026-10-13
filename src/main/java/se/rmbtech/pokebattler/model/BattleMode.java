package se.rmbtech.pokebattler.model;

public enum BattleMode {
    DUEL("1v1 Strid", 1, 1),
    TEAM("3v3 Lagstrid", 3, 3),
    RAID("RAID (5v1 mot Raid-boss)", 5, 1);

    private final String label;
    private final int playerTeamSize;
    private final int cpuTeamSize;

    BattleMode(String label, int playerTeamSize, int cpuTeamSize) {
        this.label = label;
        this.playerTeamSize = playerTeamSize;
        this.cpuTeamSize = cpuTeamSize;
    }

    public String getLabel() { return label; }
    public int getPlayerTeamSize() { return playerTeamSize; }
    public int getCpuTeamSize() { return cpuTeamSize; }
}