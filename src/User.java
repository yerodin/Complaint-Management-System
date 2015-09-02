/**
 * Created by Yerodin on 8/7/2015.
 */
public class User
{
    private int id, permission;
    private String firstName, lastName, username, password, sid;

    public User(int id, String firstName, String lastName, String username, String password, int permission, String sid)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.sid = sid;
    }

    public int getId() {
        return id;
    }

    public int getPermission() {
        return permission;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSID(){return sid;}
}
