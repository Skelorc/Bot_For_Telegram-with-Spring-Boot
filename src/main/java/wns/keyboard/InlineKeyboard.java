package wns.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import wns.entity.TimeForRecord;
import wns.state.State;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboard {

    private final InlineKeyboardMarkup keyboardMarkup;
    private final List<List<InlineKeyboardButton>> rowList;
    private final List<InlineKeyboardButton> listButtonsFirst;
    private final List<InlineKeyboardButton> listButtonsSecond;
    private final List<InlineKeyboardButton> listButtonsThird;
    private final List<InlineKeyboardButton> listButtonsFour;
    InlineKeyboardButton buttonSave;
    InlineKeyboardButton buttonTime;

    public InlineKeyboard() {
        keyboardMarkup = new InlineKeyboardMarkup();
        rowList = new ArrayList<>();
        listButtonsFirst = new ArrayList<>();
        listButtonsSecond = new ArrayList<>();
        listButtonsThird = new ArrayList<>();
        listButtonsFour = new ArrayList<>();
    }

    private void createButton(State state) {
        if (state == State.HELLO)
        {
            createButtonsForTakeTime();
        }else if (state == State.CHECK_DATA) {
            createButtonsForCheckData("Всё верно!", "Редактировать!");
        } else if (state == State.CREATE_RECORD) {
            createButtonsForCheckData("Оставить всё, как есть!", "Изменить данные!");
        }
    }

    private void createButtonsForCheckData(String msg1, String msg2) {
        buttonSave = new InlineKeyboardButton();
        buttonTime = new InlineKeyboardButton();
        buttonSave.setText(msg1);
        buttonSave.setCallbackData("message.create_record");
        buttonTime.setText(msg2);
        buttonTime.setCallbackData("message.edit");
        listButtonsFirst.add(buttonSave);
        listButtonsSecond.add(buttonTime);
    }

    private void createButtonsForTakeTime() {

        String time;
        for(int i = 0; i< TimeForRecord.record.size(); i++)
        {
            if(i<=5)
            {
                buttonTime = new InlineKeyboardButton();
                time = String.valueOf(TimeForRecord.record.get(i));
                buttonTime.setCallbackData(time);
                buttonTime.setText(time);
                listButtonsFirst.add(buttonTime);
            }
            else if(i<=11)
            {
                buttonTime = new InlineKeyboardButton();
                time = String.valueOf(TimeForRecord.record.get(i));
                buttonTime.setCallbackData(time);
                buttonTime.setText(time);
                listButtonsSecond.add(buttonTime);
            }
            else if(i<=17)
            {
                buttonTime = new InlineKeyboardButton();
                time = String.valueOf(TimeForRecord.record.get(i));
                buttonTime.setCallbackData(time);
                buttonTime.setText(time);
                listButtonsThird.add(buttonTime);
            }
            else
            {
                buttonTime = new InlineKeyboardButton();
                time = String.valueOf(TimeForRecord.record.get(i));
                buttonTime.setCallbackData(time);
                buttonTime.setText(time);
                listButtonsFour.add(buttonTime);
            }
        }
    }


    public InlineKeyboardMarkup createKeyboard(State state) {
        clearDataFromButtons();
            createButton(state);
            rowList.add(listButtonsFirst);
            rowList.add(listButtonsSecond);
            rowList.add(listButtonsThird);
            rowList.add(listButtonsFour);
        keyboardMarkup.setKeyboard(rowList);
        return keyboardMarkup;
    }

    private void clearDataFromButtons()
    {
        listButtonsFirst.clear();
        listButtonsSecond.clear();
        listButtonsThird.clear();
        listButtonsFour.clear();
        rowList.clear();
    }

    public InlineKeyboardMarkup getKeyboardMarkup() {
        return keyboardMarkup;
    }
}
