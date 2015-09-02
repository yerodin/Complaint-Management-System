import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane pane_login;

    @FXML
    private AnchorPane pane_complaints;

    @FXML
    private TextArea text_reply;

    @FXML
    private Button button_filter;

    @FXML
    private TableView<Complaint> tableview_complaint;

    @FXML
    private TableColumn column_id;

    @FXML
    private TableColumn column_firstname;

    @FXML
    private TableColumn column_lastname;

    @FXML
    private TableColumn column_block;

    @FXML
    private TableColumn column_room;

    @FXML
    private TableColumn column_category;

    @FXML
    private TableColumn column_subcategory;

    @FXML
    private TableColumn column_subject;

    @FXML
    private TableColumn column_status;

    @FXML
    private Label label_complaintid;

    @FXML
    private TextField field_password;

    @FXML
    private Button button_search;

    @FXML
    private CheckBox check_filter;

    @FXML
    private Button button_reply;

    @FXML
    private Button button_statusapply;

    @FXML
    private Label label_subcategory;

    @FXML
    private Label label_loginerror;

    @FXML
    private Label label_subject;

    @FXML
    private TextField field_username;

    @FXML
    private ChoiceBox<String> choice_status;

    @FXML
    private Button button_login;

    @FXML
    private ProgressIndicator progressindicator_login;

    @FXML
    private Label label_category;

    public Button getButton_refresh() {
        return button_refresh;
    }

    @FXML
    private Button button_refresh;

    @FXML
    private VBox box_replies;

    public VBox getBox_replies() {
        return box_replies;
    }

    public ScrollPane getPane_scroll_replies() {
        return pane_scroll_replies;
    }

    @FXML
    private ScrollPane pane_scroll_replies;

    @FXML
    private TextField field_search;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        assert progressindicator_login != null;
        // Set up the invoice table
        column_id.setCellValueFactory(new PropertyValueFactory<Complaint,Integer>("ID"));
        column_firstname.setCellValueFactory(new PropertyValueFactory<Complaint,String>("firstName"));
        column_lastname.setCellValueFactory(new PropertyValueFactory<Complaint,String>("lastName"));
        column_block.setCellValueFactory(new PropertyValueFactory<Complaint,String>("block"));
        column_room.setCellValueFactory(new PropertyValueFactory<Complaint,String>("room"));
        column_category.setCellValueFactory(new PropertyValueFactory<Complaint,String>("category"));
        column_subcategory.setCellValueFactory(new PropertyValueFactory<Complaint,String>("subcategory"));
        column_subject.setCellValueFactory(new PropertyValueFactory<Complaint,String>("subject"));
        column_status.setCellValueFactory(new PropertyValueFactory<Complaint,String>("status"));

    }

    public Button getLoginButton()
    {
        return button_login;
    }

    public AnchorPane getPane_login() {
        return pane_login;
    }

    public AnchorPane getPane_complaints() {
        return pane_complaints;
    }

    public TextArea getText_reply() {
        return text_reply;
    }

    public Button getButton_filter() {
        return button_filter;
    }

    public TableView<Complaint> getTableview_complaint() {
        return tableview_complaint;
    }

    public Label getLabel_complaintid() {
        return label_complaintid;
    }

    public TextField getField_password() {
        return field_password;
    }

    public Button getButton_search() {
        return button_search;
    }

    public CheckBox getCheck_filter() {
        return check_filter;
    }

    public Button getButton_reply() {
        return button_reply;
    }

    public Button getButton_statusapply() {
        return button_statusapply;
    }

    public Label getLabel_subcategory() {
        return label_subcategory;
    }

    public Label getLabel_subject() {
        return label_subject;
    }

    public TextField getField_username() {
        return field_username;
    }

    public TextField getField_search() {
        return field_search;
    }

    public ChoiceBox<?> getChoice_status() {
        return choice_status;
    }

    public Label getLabel_category() {
        return label_category;
    }

    public Label getLoginMessageLabel()

    {
        return label_loginerror;
    }

    public ProgressIndicator getLoginProgressIndicator()
    {
        return progressindicator_login;
    }

    public void setEnabledComplaint(boolean enabled, Complaint complaint)
    {
        System.out.println("run");
        System.out.println(complaint);
        if(complaint != null)
        {

            System.out.println("run1");
            button_reply.setDisable(false);
            label_complaintid.setText(String.valueOf(complaint.getID()));
            label_category.setText(complaint.getCategory());
            label_subcategory.setText(complaint.getSubcategory());
            label_subject.setText(complaint.getSubject());
            choice_status.setItems(FXCollections.observableArrayList("OPEN", "IN PROGRESS", "CLOSED"));
            String status = complaint.getStatus();
            int statusInt = 0;
            if(status.equals("IN PROGRESS"))
                statusInt = 1;
            else if(status.equals("CLOSED")) {
                button_reply.setDisable(true);
                statusInt = 2;
            }
            choice_status.getSelectionModel().select(statusInt);
            VBox firstComplaintBox = new VBox(3);
            firstComplaintBox.getChildren().add(new Label("on " + complaint.getDate() + " at " + complaint.getTime() + " " + complaint.getFirstName() + " said:"));
            Label cmessageLabel = new Label(complaint.getMessage());
            cmessageLabel.setWrapText(true);
            firstComplaintBox.getChildren().add(cmessageLabel);
            box_replies.getChildren().clear();
            box_replies.getChildren().add(firstComplaintBox);
            for(Reply reply: complaint.getReplies())
            {
                VBox replyBox = new VBox(3);
                replyBox.getChildren().add(new Label("on " + reply.getDate() + " at " + reply.getTime() + " " + reply.getUser() + " said:"));
                Label rmessageLabel = new Label(reply.getMessage());
                rmessageLabel.prefWidthProperty().bind(box_replies.widthProperty());
                rmessageLabel.setWrapText(true);
                rmessageLabel.setTextAlignment(TextAlignment.JUSTIFY);
                replyBox.getChildren().add(rmessageLabel);
                box_replies.getChildren().add(replyBox);
            }
        }
        else
        {
            label_complaintid.setText("");
            label_category.setText("");
            label_subcategory.setText("");
            label_subject.setText("");
            choice_status.getSelectionModel().clearSelection();
        }

        if(enabled)
        {
            button_reply.setDisable(false);
            button_statusapply.setDisable(false);
        }
        else
        {
            button_reply.setDisable(true);
            button_statusapply.setDisable(true);
        }
    }

}
