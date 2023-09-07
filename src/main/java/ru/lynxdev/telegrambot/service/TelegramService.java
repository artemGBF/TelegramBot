package ru.lynxdev.telegrambot.service;

import ch.qos.logback.core.net.ObjectWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.lynxdev.telegrambot.config.BotConfig;
import ru.lynxdev.telegrambot.dao.PlayersDAO;
import ru.lynxdev.telegrambot.model.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.lynxdev.telegrambot.dao.InMemoryDB.players;

@Component
public class TelegramService extends TelegramLongPollingBot {
    private final String message = "Welcome to CW: ";

    @Autowired
    private BotConfig botConfig;
    @Autowired
    private PlayersDAO playersDAO;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().trim();
            long chatId = update.getMessage().getChatId();

            LocalTime now = LocalTime.now();
            if (now.isAfter(LocalTime.of(0, 0, 0))
                    && now.isBefore(LocalTime.of(10, 0, 0))) {
                this.sendMessage(chatId, "Error! It's too early!");
            }

            if (messageText.startsWith("/ТЗ") || messageText.startsWith("/тз") || messageText.startsWith("/Тз")) {
                int number;
                String points;
                if (messageText.length() > 4 && Character.isDigit(messageText.charAt(3))) {
                    number = Integer.parseInt(messageText.substring(3, 5));
                    points = messageText.substring(5);
                } else {
                    number = Integer.parseInt(messageText.substring(3, 4));
                    points = messageText.substring(4);
                }

                Set<Player> players = playersDAO.findAllByTR(number);
                this.sendMessage(chatId, message +
                        players.stream()
                                .map(Player::getId)
                                .collect(Collectors.joining(" ")) +
                        points);
            } else if (messageText.startsWith("/Sync") || messageText.startsWith("/sync")) {
                try {
                    FileOutputStream stream = new FileOutputStream("dump.json");
                    stream.write(new ObjectMapper().writeValueAsBytes(playersDAO.getAll()));
                    stream.close();
                } catch (Exception e) {
                    this.sendMessage(chatId, "Error during sync");
                }
            } else if (messageText.startsWith("/load")||messageText.startsWith("/Load")) {
                try {
                    FileInputStream stream = new FileInputStream("dump.json");
                    byte[] bytes = stream.readAllBytes();
                    players = new ObjectMapper().readValue(bytes, new TypeReference<>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    });
                    this.sendMessage(chatId, "Dataset was loaded");
                } catch (Exception e) {
                    this.sendMessage(chatId, "Error during loading");
                }
            } else if (messageText.startsWith("/Kick")||messageText.startsWith("/kick")) {
                try {
                    String[] s = messageText.split(" ");
                    playersDAO.kick(s[1], Integer.parseInt(s[2]));
                    this.sendMessage(chatId, "Player was kicked");
                } catch (Exception e) {
                    this.sendMessage(chatId, "Error!");
                }
            } else if (messageText.startsWith("/Add")||messageText.startsWith("/add")) {
                try {
                    String[] s = messageText.split(" ");
                    if (s.length == 3) {
                        playersDAO.add(new Player(s[1], Integer.parseInt(s[2])));
                    } else {
                        playersDAO.add(new Player(s[1], Integer.parseInt(s[2]), Integer.parseInt(s[3])));
                    }
                    this.sendMessage(chatId, "Player was successfully added");
                } catch (Exception e) {
                    this.sendMessage(chatId, "Error!");
                }
            } else {
                this.sendMessage(chatId, "Error! Input should be like /ТЗ6");
            }
        }

    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}