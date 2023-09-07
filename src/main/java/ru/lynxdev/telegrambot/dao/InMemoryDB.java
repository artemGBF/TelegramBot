package ru.lynxdev.telegrambot.dao;

import ru.lynxdev.telegrambot.model.Player;

import java.util.*;


public class InMemoryDB {
    public static Set<Player> players = new HashSet<>();

    static {
        players.add(new Player("@avshipilov", 6));
        players.add(new Player("@avshipilov", 11));
        players.add(new Player("@alekscom", 7));
        players.add(new Player("@alekscom", 8));
        players.add(new Player("@alekscom", 11));
    }


}
