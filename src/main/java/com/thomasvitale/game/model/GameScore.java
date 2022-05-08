package com.thomasvitale.game.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record GameScore(
    @JsonProperty("Player")
    String player,
    @JsonProperty("SessionId")
    String sessionId,
    @JsonProperty("Time")
    LocalDateTime gameTime,
    @JsonProperty("Level")
    String level,
    @JsonProperty("LevelScore")
    int levelScore
){}
