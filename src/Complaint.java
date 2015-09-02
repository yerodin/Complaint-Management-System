import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

/**
 * Created by Yerodin on 8/7/2015.
 */
public class Complaint {

    public final static int OPEN = 0;
    public final static int INPROGRESS = 1;
    public final static int CLOSED=2;


    private IntegerProperty ID;
    private StringProperty category, subcategory, subject, message, timestamp, status;
    private Resident resident;
    private ArrayList<Reply> replies;

    public Complaint(int ID, Resident resident, String category, String subcategory, String subject, String message, String timestamp, String status)
    {
        this.ID = new SimpleIntegerProperty(ID);
        this.resident = resident;
        this.category = new SimpleStringProperty(category);
        this.subcategory = new SimpleStringProperty(subcategory);
        this.subject = new SimpleStringProperty(subject);
        this.message = new SimpleStringProperty(message);
        this.timestamp = new SimpleStringProperty(timestamp);
        this.status = new SimpleStringProperty(status);
        replies = new ArrayList<>();
    }

    public void set(Complaint c)
    {
        setID(c.getID());
        this.resident = c.getResident();
        setCategory(c.getCategory());
        setSubcategory(c.getSubcategory());
        setSubject(c.getSubject());
        setMessage(c.getMessage());
        setTimestamp(c.getTimestamp());
        setStatus(c.getStatus());
        replies = c.replies;
    }

    public void addReply(Reply reply)
    {
        replies.add(reply);
    }

    public int getID() {
        return ID.get();
    }

    public IntegerProperty IDProperty() {
        return ID;
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getSubcategory() {
        return subcategory.get();
    }

    public StringProperty subcategoryProperty() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory.set(subcategory);
    }

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public Resident getResident() {
        return resident;
    }


    public String getFirstName(){return getResident().getFirstName();}
    public String getLastName(){return getResident().getLastName();}
    public String getBlock(){return getResident().getBlock();}
    public String getRoom(){return getResident().getRoom();}


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

    public ArrayList<Reply> getReplies()
    {
       return replies;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "ID=" + ID +
                ", category=" + category +
                ", subcategory=" + subcategory +
                ", subject=" + subject +
                ", message=" + message +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", resident=" + resident +
                ", replies=" + replies +
                '}';
    }
}
