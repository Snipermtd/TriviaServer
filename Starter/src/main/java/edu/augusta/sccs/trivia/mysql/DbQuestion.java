package edu.augusta.sccs.trivia.mysql;

import edu.augusta.sccs.trivia.AnswerType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;


@Entity
@Table(name = "questions")
public class DbQuestion {
    @Id
    private String uuid;

    @NotNull
    private byte difficulty;

    @NotNull
    private String question;

    @NotNull
    private String answer;

    @NotNull
    private AnswerType answerType;

    private String choices;



    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public @NotNull int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(@NotNull int difficulty) {
        this.difficulty = (byte) difficulty;
    }

    public @NotNull String getQuestion() {
        return question;
    }

    public void setQuestion(@NotNull String question) {
        this.question = question;
    }

    public @NotNull String getAnswer() {
        return answer;
    }

    public void setAnswer(@NotNull String answer) {
        this.answer = answer;
    }

    public @NotNull AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(@NotNull AnswerType answerType) {
        this.answerType = answerType;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

}
