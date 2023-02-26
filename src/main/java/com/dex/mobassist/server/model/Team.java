package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Team {
    private String name;
    private String color;
    private String logoUrl;

    public static Team createTeam(@NonNull String name, @NonNull String color) {
        return createTeam(name, color, null);
    }

    public static Team createTeam(@NonNull String name, @NonNull String color, String logoUrl) {
        final Team team = new Team();

        team.name = name;
        team.color = color;
        team.logoUrl = logoUrl;

        return team;
    }
}
