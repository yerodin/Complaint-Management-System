/**
 * Created by Yerodin on 8/7/2015.
 */
public class Resident {

    private String firstName, lastName, block, room;

    public Resident(String firstName, String lastName, String block, String room)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.block = block;
        this.room = room;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBlock() {
        return block;
    }

    public String getRoom() {
        return room;
    }
}
