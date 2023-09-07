package ru.lynxdev.telegrambot.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String id;
    private Integer throneRoom;
    private Integer skipCW = null;
    private boolean vacation = false;

    public Player(String id, int throneRoom) {
        this.id = id;
        this.throneRoom = throneRoom;
    }

    public Player(String id, Integer throneRoom, Integer skipCW) {
        this.id = id;
        this.throneRoom = throneRoom;
        this.skipCW = skipCW;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
