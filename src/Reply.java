import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormatSymbols;

/**
 * Created by Yerodin on 8/7/2015.
 */
public class Reply {
    private int ID, complaint;
    private StringProperty user, message, timestamp;

    public Reply(int ID, int complaint, String user, String message, String timestamp)
    {
        this.ID = ID;
        this.complaint = complaint;
        this.user = new SimpleStringProperty(user);
        this.message = new SimpleStringProperty(message);
        this.timestamp = new SimpleStringProperty(timestamp);
    }

    public int getID() {
        return ID;
    }

    public int getComplaint() {
        return complaint;
    }

    public String getUser() {
        return user.get();
    }

    public StringProperty userProperty() {
        return user;
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public String getDate()
    {
        String year = getTimestamp().substring(0, 4);
        String month = new DateFormatSymbols().getMonths()[Integer.parseInt(getTimestamp().substring(5, 7)) - 1];
        String day = getTimestamp().substring(8, 10);
        if(Integer.parseInt(day) < 10)
            day = day.substring(1);
        return month+" "+day+", "+year;
    }

    public String getTime()
    {
        int hr = Integer.parseInt(getTimestamp().substring(11, 13));
        String tod = "AM";
        String rem = getTimestamp().substring(13);
        if(hr >= 12)
        {
            tod = "PM";
            if(hr > 12)
            {
                hr = hr-12;
            }
        }
        return hr+rem+tod;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "ID=" + ID +
                ", complaint=" + complaint +
                ", user=" + user +
                ", message=" + message +
                ", timestamp=" + timestamp +
                '}';
    }
}
