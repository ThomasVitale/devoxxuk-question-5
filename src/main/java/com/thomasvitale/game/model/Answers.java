package com.thomasvitale.game.model;

public record Answers(
    String player,
    String sessionId,
    boolean optionA,
    boolean optionB,
    boolean optionC,
    boolean optionD,
    int remainingTime
){}
