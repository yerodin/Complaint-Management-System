
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yerodin on 8/7/2015.
 */
public class DatabaseConnector {
    final private String SERVER_IP = "localhost";
    final private String LOGIN_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//login.php";
    final private String COMPLAINT_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//get_complaint.php";
    final private String CATEGORIES_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//get_categories.php";
    final private String SUBCATEGORIES_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//get_subcategories.php";
    final private String RESIDENT_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//get_resident.php";
    final private String NEWCOMPLAINT_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//get_newcomplaints.php";
    final private String NEWREPLY_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//get_newreplies.php";
    final private String LEAVEREPLY_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//leave_reply.php";
    final private String UPDATE_STATUS_URL = "http://" + SERVER_IP + "//cms//dataprovider//hcmanager//update_status.php";

    public final static int SUCCESS = 1;
    public final static int FAILURE = 0;

    private JSONParser jParser;
    private int statusId;
    private String status;

    private int replyVersion;
    private int complaintVersion;

    private String[] categories;
    private String[] subcategories;

    public DatabaseConnector()
    {
        jParser = new JSONParser();
        replyVersion = 0;
        complaintVersion = 0;
    }

    private void fillCategories(User currentUser) throws Exception
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", currentUser.getSID()));
        JSONObject jObj = jParser.makeHttpRequest(CATEGORIES_URL, "POST", params);
        try
        {
            if (jObj.getInt("success") == SUCCESS)
            {
                JSONArray dcategories = jObj.getJSONArray("data");
                categories = new String[dcategories.length()+1];
                for(int i = 0; i < dcategories.length();++i)
                {
                    JSONObject category= dcategories.getJSONObject(i);
                    int id = category.getInt("id");
                    String name = category.getString("name");
                    categories[id] = name;
                }
            }
            else
                status = "error";
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", currentUser.getSID()));
        JSONObject jObj2 = jParser.makeHttpRequest(SUBCATEGORIES_URL, "POST", params);
        try
        {
            if (jObj2.getInt("success") == SUCCESS)
            {
                JSONArray dcategories = jObj2.getJSONArray("data");
                subcategories = new String[dcategories.length()+1];
                for(int i = 0; i < dcategories.length();++i)
                {
                    JSONObject category= dcategories.getJSONObject(i);
                    int id = category.getInt("id");
                    String name = category.getString("name");
                    subcategories[id] = name;
                }
            }
            else
                status = "error";
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }
    }

    public User login(String username, String password, int taskID)
    {
        statusId = taskID;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject jObj = jParser.makeHttpRequest(LOGIN_URL, "POST", params);
        System.out.println(jObj);
        try
        {
            if (jObj.getInt("success") == SUCCESS)
            {
                JSONObject user = jObj.getJSONArray("data").getJSONObject(0);
                int id = user.getInt("id");
                String firstName = user.getString("first_name");
                String lastName = user.getString("last_name");
                username = user.getString("username");
                int permission = user.getInt("permission");
                String sid = jObj.getString("sid");
                status = "Successful Login.";
                User u = new User(id,firstName,lastName,username,password,permission,sid);
                fillCategories(u);
                return u;
            }
            else
                status = "Incorrect username/password combination.";
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }
        return null;
    }

    private Resident getResident(User currentUser, String resID)
    {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sid", currentUser.getSID()));
        params.add(new BasicNameValuePair("uid", resID));
        JSONObject jObj = jParser.makeHttpRequest(RESIDENT_URL, "POST", params);
        System.out.println(jObj);
        try
        {
            if (jObj.getInt("success") == SUCCESS)
            {
                JSONObject resident = jObj.getJSONArray("data").getJSONObject(0);
                String firstName = resident.getString("first_name");
                String lastName = resident.getString("last_name");
                String block = resident.getString("block_alias");
                String room = resident.getString("number");
                return new Resident(firstName,lastName,block,room);
            }
            else
                status = "Not Logged In";
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }
        return null;
    }

    public Complaint[] getNewComplaints(User currentUser,int taskID)
    {
        statusId = taskID;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", currentUser.getSID()));
        params.add(new BasicNameValuePair("cversion", String.valueOf(complaintVersion)));
        JSONObject jObj = jParser.makeHttpRequest(NEWCOMPLAINT_URL, "POST", params);
        Complaint[] returnArray=null;
        try
        {
            if (jObj.getInt("success") == SUCCESS)
            {
                if(jObj.getInt("flag") == 1)
                    return null;
                JSONArray complaints = jObj.getJSONArray("data").getJSONArray(0);
                if(complaints.length() > 0)
                    returnArray = new Complaint[complaints.length()];
                for(int i = 0; i < complaints.length(); ++i)
                {
                     JSONObject complaint = complaints.getJSONObject(i);
                    int id = complaint.getInt("complaint_id");
                    String userID = complaint.getString("user_id");
                    String category = categories[complaint.getInt("category")];
                    String subcategory = subcategories[complaint.getInt("sub_category")];
                    String subject = complaint.getString("subject");
                    String message = complaint.getString("message");
                    String timestamp  = complaint.getString("tstamp");
                    String statu  = complaint.getString("stat");
                    Resident r = getResident(currentUser, userID);
                    returnArray[i]= new Complaint(id,r,category,subcategory,subject,message,timestamp,statu);

                }
                complaintVersion = jObj.getInt("cversion");
                status = "Successful";
                return returnArray;
            }
            else
                status = "Incorrect username/password combination.";
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }
        return null;
    }

    public Reply[] getNewReplies(User currentUser,int taskID)
    {
        statusId = taskID;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", currentUser.getSID()));
        params.add(new BasicNameValuePair("rversion", String.valueOf(replyVersion)));
        JSONObject jObj = jParser.makeHttpRequest(NEWREPLY_URL, "POST", params);
        System.out.println(jObj);
        Reply[] returnArray=null;
        try
        {
            if (jObj.getInt("success") == SUCCESS)
            {
                if(jObj.getInt("flag") == 1)
                    return null;
                JSONArray replies = jObj.getJSONArray("data").getJSONArray(0);
                if(replies.length() > 0)
                    returnArray = new Reply[replies.length()];
                for(int i = 0; i < replies.length(); ++i)
                {
                    JSONObject reply = replies.getJSONObject(i);
                    int id = reply.getInt("reply_id");
                    int cid = reply.getInt("complaint_id");
                    String user = reply.getString("user");
                    String message = reply.getString("message");
                    String timestamp = reply.getString("tstamp");
                    returnArray[i]= new Reply(id,cid,user,message,timestamp);
                    }
                    replyVersion = jObj.getInt("rversion");
                    status = "Successful";
                    return returnArray;
            }
            else
                status = "Incorrect username/password combination.";
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }
        return null;
    }

    public Complaint getComplaint(User currentUser, int complaintID, int taskID)
    {
        statusId = taskID;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sid", currentUser.getSID()));
        params.add(new BasicNameValuePair("cid", String.valueOf(complaintID)));
        JSONObject jObj = jParser.makeHttpRequest(LOGIN_URL, "POST", params);
        try
        {
            if (jObj.getInt("success") == SUCCESS)
            {
                JSONObject complaint = jObj.getJSONArray("data").getJSONObject(0);
                int id = complaint.getInt("complaint_id");
                String userID = complaint.getString("user_id");
                String category = categories[complaint.getInt("category")];
                String subcategory = subcategories[complaint.getInt("subcategory")];
                String subject = complaint.getString("subject");
                String message = complaint.getString("message");
                String timestamp  = complaint.getString("tstamp");
                String statu  = complaint.getString("stat");
                Resident r = getResident(currentUser,userID);
                status = "Successful";
                return new Complaint(id,r,category,subcategory,subject,message,timestamp,statu);
            }
            else
                status = "Incorrect username/password combination.";
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }
        return null;
    }

    public boolean submitReply(User currentUser, Reply r, int taskID)
    {
        statusId = taskID;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user", r.getUser()));
        params.add(new BasicNameValuePair("message", r.getMessage()));
        params.add(new BasicNameValuePair("cid", String.valueOf(r.getComplaint())));
        params.add(new BasicNameValuePair("sid", currentUser.getSID()));
        JSONObject jObj = jParser.makeHttpRequest(LEAVEREPLY_URL, "POST", params);
        System.out.println(jObj);
        try
        {
            if (jObj.getInt("success") == SUCCESS)
                return true;
            else
                status = jObj.getString("data");
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }
        return false;
    }

    public boolean changeStatus(User currentUser, int stat,Complaint complaint, int taskID)
    {
        statusId = taskID;
        String statS;
        switch(stat)
        {
            case 0:
                statS = "OPEN";
                break;
            case 1:
                statS = "IN-PROGRESS";
                break;
            case 2:
                statS = "CLOSED";
                break;
                default:
                    statS = "OPEN";
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", statS));
        params.add(new BasicNameValuePair("cid", String.valueOf(complaint.getID())));
        params.add(new BasicNameValuePair("sid", currentUser.getSID()));
        JSONObject jObj = jParser.makeHttpRequest(UPDATE_STATUS_URL, "POST", params);
        System.out.println(jObj);
        try
        {
            if (jObj.getInt("success") == SUCCESS)
            {
                return true;
            }
            else
                status = jObj.getString("data");
        } catch (Exception e)
        {
            e.printStackTrace();
            status = "Error, could not connect to server.";
        }
        return false;
    }


    public int getStatusId()
    {
        return statusId;
    }
    public String getStatus()
    {
        return status;
    }

}
