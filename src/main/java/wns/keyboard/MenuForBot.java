package wns.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import wns.entity.TimeForRecord;

import java.util.ArrayList;

@Component
public class MenuForBot {

    private ReplyKeyboardMarkup menu = new ReplyKeyboardMarkup();

    private ArrayList<KeyboardRow> keyboard = new ArrayList<>();

    private KeyboardRow keyboardFirstRow = new KeyboardRow();
    private KeyboardRow keyboardSecondRow = new KeyboardRow();
    private KeyboardRow keyboardThirdRow = new KeyboardRow();




    public ReplyKeyboardMarkup createKeyboardForChoiceTime()
    {
        clearButtons();
        menu.setSelective(true);
        menu.setResizeKeyboard(true);
        menu.setOneTimeKeyboard(true);
        for(int i = 0; i< TimeForRecord.record.size();i++)
        {
            if(i<=7)
            {
                keyboardFirstRow.add(String.valueOf(TimeForRecord.record.get(i)));
            }
            else if(i<=15)
            {
                keyboardSecondRow.add(String.valueOf(TimeForRecord.record.get(i)));
            }
            else
            {
                keyboardThirdRow.add(String.valueOf(TimeForRecord.record.get(i)));
            }
        }
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        menu.setKeyboard(keyboard);
        return menu;
    }

    private void clearButtons() {
        keyboard.clear();
    }

}
