package ru.lynxdev.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum ThroneRoomEnum {
    TR6(6),
    TR7(7),
    TR8(8),
    TR9(9),
    TR10(10),
    TR11(11);

    private Integer number;

}
