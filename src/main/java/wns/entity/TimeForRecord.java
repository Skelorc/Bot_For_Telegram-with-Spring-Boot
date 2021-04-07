package wns.entity;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class TimeForRecord {
    public static ArrayList<Double> record = new ArrayList<>();

    public TimeForRecord() {
        createTime();
    }

    public static void createTime()
    {
        record.add(9.00);
        record.add(9.30);
        record.add(10.00);
        record.add(10.30);
        record.add(11.00);
        record.add(11.30);
        record.add(12.00);
        record.add(12.30);
        record.add(13.00);
        record.add(13.30);
        record.add(14.00);
        record.add(14.30);
        record.add(15.00);
        record.add(15.30);
        record.add(16.00);
        record.add(16.30);
        record.add(17.00);
        record.add(17.30);
        record.add(18.00);
        record.add(18.30);
        record.add(19.00);
        record.add(19.30);
        record.add(20.00);
        record.add(20.30);
    }

    public static void sort()
    {
        Collections.sort(record);
    }

    public void clearTime()
    {
        record.clear();
    }

    public boolean takeTime(String time)
    {
        String replace = time.replace(":", ".");
        boolean b = record.removeIf(s -> s.equals(Double.parseDouble(replace)));
        return b;
    }

}
