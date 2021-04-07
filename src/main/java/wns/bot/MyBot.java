package wns.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import wns.entity.Client;
import wns.entity.TimeForRecord;
import wns.handlers.HandlerCallback;
import wns.handlers.HandlerText;
import wns.service.ClientService;
import wns.state.State;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
public class MyBot extends TelegramLongPollingBot {

    @Autowired
    private TimeForRecord time;
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    @Autowired
    private HandlerText handlerText;
    @Autowired
    private HandlerCallback handlerCallback;
    private List<SendMessage> listMessages;
    @Autowired
    private ClientService clientService;

    @Scheduled(cron = "0 0 23 * * ?")
    public void restartTime()
    {
        TimeForRecord.record.clear();
        TimeForRecord.createTime();
        ClientService.db.clear();
    }
    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message;
        listMessages = new ArrayList<>();
        if(update.hasCallbackQuery())
        {
            listMessages.addAll(handlerCallback.getDataFromCallback(update));
            for(SendMessage message1: listMessages)
            {
                try {
                    execute(message1);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            message = handlerText.takeMessage(update);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {

    }


}
