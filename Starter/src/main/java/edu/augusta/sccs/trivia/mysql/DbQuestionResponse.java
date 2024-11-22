package edu.augusta.sccs.trivia.mysql;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "responses")
public class DbQuestionResponse {
    @Id
    private String uuid;

    @ManyToOne(optional=false)
    @JoinColumn(name = "playerUuid", referencedColumnName = "uuid",  // a reference to a non-PK column
            foreignKey = @ForeignKey(name="PlayerToQuestionResponseForeignKey"))
    private DbPlayer player;

    @ManyToOne(optional=false)
    @JoinColumn(name = "questionUuid", referencedColumnName = "uuid",  // a reference to a non-PK column
            foreignKey = @ForeignKey(name="QuestionTOQuestionResponseForeignKey"))
    private DbQuestion question;

    @NotNull
    private boolean correct;

    @NotNull
    private Instant timestamp;

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public DbPlayer getPlayer() {
        return player;
    }

    public void setPlayer(DbPlayer player) {
        this.player = player;
    }

    public DbQuestion getQuestion() {
        return question;
    }

    public void setQuestion(DbQuestion question) {
        this.question = question;
    }

    @NotNull
    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(@NotNull boolean correct) {
        this.correct = correct;
    }

    public @NotNull Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Instant timestamp) {
        this.timestamp = timestamp;
    }

}
