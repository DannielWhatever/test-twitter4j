package cl.app;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Daniel on 01-03-2016.
 */
public enum Hour {

    MORNING("06~12"),
    MEDLEY("12~16"),
    EVENING("16~21"),
    NIGHT("21~06");

    private final String range;

    Hour(String range){
        this.range = range;
    }

    public String getRange() {
        return range;
    }

    public static Hour of(Date date){
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        int hour = ldt.getHour();
        Hour elem = null;
        switch(hour){
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                elem = Hour.MORNING;
                break;
            case 13:
            case 14:
            case 15:
            case 16:
                elem = Hour.MEDLEY;
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
                elem = Hour.EVENING;
                break;
            case 22:
            case 23:
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                elem = Hour.NIGHT;
                break;
        }
        return elem;
    }

}