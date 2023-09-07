package ru.lynxdev.telegrambot.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.lynxdev.telegrambot.model.Player;

import java.util.Set;
import java.util.stream.Collectors;

import static ru.lynxdev.telegrambot.dao.InMemoryDB.players;

@Component
public class PlayersDAOImpl implements PlayersDAO {

    @Override
    public Set<Player> getAll() {
        return players;
    }

    @Override
    public Set<Player> findAllByTR(int throneRoom) {
        return players.stream()
                .filter(p -> p.getThroneRoom() == throneRoom)
                .collect(Collectors.toSet());
    }

    @Override
    public void add(Player player) {
        players.add(player);
    }

    @Override
    public void kick(String playerId, int throneRoom) {
        players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .ifPresent(player -> players.remove(player));
    }

    @Override
    public boolean promote(String playerId, int throneRoom) {
        if (throneRoom == 11) {
            return false;
        }

        players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .ifPresent(player -> player.setThroneRoom(throneRoom + 1));

        return true;
    }

    @Override
    public void vacation(String playerId, boolean vacation) {
        players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .ifPresent(player -> player.setVacation(vacation));
    }
}
