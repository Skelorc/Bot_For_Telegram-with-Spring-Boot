package wns.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import wns.cons.Const;
import wns.entity.Client;
import wns.entity.TimeForRecord;
import wns.keyboard.InlineKeyboard;
import wns.keyboard.MenuForBot;
import wns.service.ClientService;
import wns.state.State;


@Component
public class HandlerText {


    @Autowired
    private ClientService clientService;
    @Autowired
    private Client client;

    @Autowired
    private TimeForRecord time;

    @Autowired
    private InlineKeyboard keyboard;


    SendMessage message;
    public SendMessage takeMessage(Update update)
    {
       client = getClientFromId(update);



        message = new SendMessage();
       message.setText(getMessageFromClient(update));
       message.setChatId(String.valueOf(client.getChatId()));
       message.setReplyMarkup(keyboard.createKeyboard(client.getState()));
       return message;
    }


    private Client getClientFromId(Update update) {
        Long chatId = update.getMessage().getChatId();
        Client clientFromDB = clientService.findClientFromDB(chatId);
        if(clientFromDB==null)
        {
            clientFromDB = new Client(chatId, null,null,null,State.HELLO);
            clientService.saveClientToDB(clientFromDB);
            return clientFromDB;
        }
        else
        {
            return  clientFromDB;
        }
    }

    private String getMessageFromClient(Update update)
    {
        State state = client.getState();
        String text = update.getMessage().getText();
        String answer = "";
        switch (state)
        {
            case HELLO:
              answer = createAnswerForHello();
              message.setReplyMarkup(keyboard.createKeyboard(State.HELLO));
              break;
            case CHOICE_TIME:
                answer = checkTime(text);
                break;
            case INPUT_NUMBER_CAR:
                answer = checkNumberCar(text);
                break;
            case INPUT_PHONE:
                answer = checkPhoneNumber(text);
                break;
            case CREATE_RECORD:
                answer = afterCreateRecord();
                break;

        }
        return answer;
    }

    private String createAnswerForHello()
    {

        String answer = Const.START;
        client.setState(State.HELLO);
        return answer;
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

    private String checkNumberCar(String text)
    {
       // String regex = "[0-9]+";
        /*if(text.length()==3)
        {
            if(text.matches(regex))
            {*/
                client.setState(State.INPUT_PHONE);
                client.setCar_number(text);
                clientService.saveClientToDB(client);
                return "Указанная вами информация - " + text + ". Введите номер телефона для обратной связи с вами. Пример: +7 123 456 7890";
        /*    }
        }*/
    }

    private String checkPhoneNumber(String text) {
        String regex = "[0-9]+";
        String textAfterReplace = text.replaceAll("\\s+", "").trim();
        if(textAfterReplace.startsWith("+"))
        {
            if(textAfterReplace.length()>10)
            {
                String number = textAfterReplace.substring(1);
                if(number.matches(regex))
                {
                    client.setPhoneNumber(textAfterReplace);
                    client.setState(State.CHECK_DATA);
                    clientService.saveClientToDB(client);
                    return "Проверьте ещё раз введённую информацию: Время - " + client.getTime() + ", номер вашей машины - " +
                            client.getCar_number()+", ваш номер телефона " + client.getPhoneNumber() + ".";
                }
            }
        }
        else
        {
            return "Введите номер ещё раз. Пример ввода номера : +71234567890";
        }
        return "Введите номер ещё раз. Пример ввода номера : +71234567890";
    }

    private String afterCreateRecord()
    {
        return "Приветствую вас! Вы уже записаны сегодня на " + client.getTime() + ". Хотите изменить время или данные о себе?";
    }
}
