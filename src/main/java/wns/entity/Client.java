package wns.entity;

import org.springframework.stereotype.Component;
import wns.state.State;


@Component
public class Client {


    private Long chatId;
    private String phoneNumber;
    private String car_number;
    private String time;
    private State state;

    public Client(Long chatId, String time,String phoneNumber, String car_number, State state) {
        this.chatId = chatId;
        this.phoneNumber = phoneNumber;
        this.car_number = car_number;
        this.time = time;
        this.state = state;
    }

    public Client() {
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

}
