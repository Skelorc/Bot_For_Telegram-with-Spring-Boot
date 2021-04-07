package wns.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import wns.cons.Const;
import wns.entity.Client;
import wns.entity.TimeForRecord;
import wns.keyboard.InlineKeyboard;
import wns.service.ClientService;
import wns.state.State;

import java.sql.Time;
import java.util.ArrayList;

@Component
public class HandlerCallback {

    @Autowired
    private Client client;
    @Autowired
    private ClientService clientService;
    private ArrayList<SendMessage> listMessages;

    @Autowired
    private TimeForRecord time;

    @Autowired
    private InlineKeyboard keyboard;

    private SendMessage messageForClient;
    private SendMessage messageForServer;

    public ArrayList<SendMessage> getDataFromCallback(Update update)
    {
        listMessages = new ArrayList<>();
        String text = update.getCallbackQuery().getData();
        checkDataFromCallback(update,text);
        return listMessages;
    }

    private void checkDataFromCallback(Update update, String text) {
        messageForServer = new SendMessage();
        messageForClient = new SendMessage();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        client = clientService.findClientFromDB(chatId);
        String dataFromQuery = update.getCallbackQuery().getData();
        if(dataFromQuery.contains(".")&&dataFromQuery.length()<5)
        {
            checkTimeFromDataQuery(dataFromQuery);
        }
        switch (text)
        {
            case "message.edit":
                editData();
                break;
            case "message.create_record":
                createRecord();
                break;
        }

    }

    private void checkTimeFromDataQuery(String dataFromQuery) {
        String answer = checkTime(dataFromQuery);
        messageForClient.setText(answer);
        messageForClient.setChatId(String.valueOf(client.getChatId()));
        listMessages.add(messageForClient);
        client.setState(State.INPUT_NUMBER_CAR);
        clientService.saveClientToDB(client);
    }

    private void createRecord() {
        client.setState(State.CREATE_RECORD);
        clientService.saveClientToDB(client);
        messageForServer.setText("Новый клиент! Время записи " + client.getTime()+", информация о машине клиента " + client.getCar_number()+", номер телефона " + client.getPhoneNumber());
        messageForServer.setChatId(String.valueOf(870360030));
        messageForClient.setChatId(String.valueOf(client.getChatId()));
        messageForClient.setText("Вы записаны. Будем вас ждать.");
        listMessages.add(messageForClient);
        listMessages.add(messageForServer);
    }

    private void editData() {

        if(client.getTime()!=null)
        {
            if(!TimeForRecord.record.contains(Double.parseDouble(client.getTime()))) {
                TimeForRecord.record.add(Double.parseDouble(client.getTime()));
            }
        }
        //  client.setTime(null);
        clientService.saveClientToDB(client);
        TimeForRecord.sort();
        messageForClient.setText(createAnswerForHello());
        messageForClient.setChatId(String.valueOf(client.getChatId()));
        messageForClient.setReplyMarkup(keyboard.createKeyboard(client.getState()));
        listMessages.add(messageForClient);
    }

    private String checkTime(String text)
    {
        if(text.contains("."))
        {
            if(!time.takeTime(text))
            {
                return "Пожалуйста, выберите другое время, так как введённое вами занято, либо не существует!";
            }
            else
            {
                client.setState(State.INPUT_NUMBER_CAR);
                client.setTime(text);
                clientService.saveClientToDB(client);
                return "Выбранное время - " + text + ". Пожалуйста, введите марку машины, номер, и если хотите, любую дополнительную информацию";
            }
        }
        else
        {
            return "Пожалуйста, выберите другое время, так как введённое вами занято, либо не существует!";
        }
    }

    private String createAnswerForHello()
    {

        String answer = Const.START;
        client.setState(State.HELLO);
        return answer;
    }
}
