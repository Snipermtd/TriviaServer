package edu.augusta.sccs.trivia.mysql;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "players")
public class DbPlayer {
    @Id
    private String uuid;

    @NotNull
    private String username;

    @NotNull
    private byte lastDifficulty;

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public @NotNull String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    @NotNull
    public int getLastDifficulty() {
        return lastDifficulty;
    }

    public void setLastDifficulty(@NotNull int lastDifficulty) {
        this.lastDifficulty = (byte) lastDifficulty;
    }

}
