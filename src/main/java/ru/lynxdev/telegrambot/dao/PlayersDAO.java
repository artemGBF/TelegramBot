package ru.lynxdev.telegrambot.dao;

import org.springframework.stereotype.Repository;
import ru.lynxdev.telegrambot.model.Player;

import java.util.Map;
import java.util.Set;

@Repository
public interface PlayersDAO {

    Set<Player> getAll();

    Set<Player> findAllByTR(int throneRoom);

    void add(Player player);

    void kick(String playerId, int throneRoom);

    boolean promote(String playerId, int throneRoom);

    void vacation(String playerId, boolean vacation);
}
