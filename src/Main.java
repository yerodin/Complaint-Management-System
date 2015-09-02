import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
    private User user;
    private Controller controller;
   final  private DatabaseConnector databaseConnector = new DatabaseConnector();
    final private ObservableList<Complaint> complaints = FXCollections.observableArrayList();
    private FilteredList<Complaint> filteredData = new FilteredList<>(complaints, p -> true);

    private Task<Void> mainTask;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "hall_complaint_manager.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        primaryStage.setTitle("Hall Complaint Manager");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setMinWidth(1280);
        initAPP();
        primaryStage.show();
    }

    private void initAPP()
    {
        controller.getLoginProgressIndicator().setVisible(false);
        controller.getLoginMessageLabel().setVisible(false);
        controller.getPane_complaints().setVisible(false);
        controller.getPane_login().setVisible(true);
        initUI();
        controller.getTableview_complaint().setItems(complaints);
        controller.setEnabledComplaint(false, null);
    }

    private void initUI()
    {
        controller.getLoginButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.getLoginMessageLabel().setVisible(false);
                controller.getLoginProgressIndicator().setVisible(true);
                Task<User> task = new Task<User>() {
                    @Override
                    protected User call() throws Exception {
                        return user = databaseConnector.login(controller.getField_username().getText(), controller.getField_password().getText(), 1);
                    }
                };
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        controller.getLoginProgressIndicator().setVisible(false);
                        user = task.getValue();
                        if (user != null) {
                            controller.getLoginMessageLabel().setTextFill(Paint.valueOf("BLUE"));
                            controller.getLoginMessageLabel().setText("Login successful!");
                            controller.getLoginMessageLabel().setVisible(true);
                            controller.getLoginProgressIndicator().setVisible(true);
                            startUpdateService();
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }
                            controller.getPane_complaints().setVisible(true);
                            controller.getPane_login().setVisible(false);
                        } else {
                            controller.getLoginMessageLabel().setTextFill(Paint.valueOf("RED"));
                            controller.getLoginMessageLabel().setVisible(true);
                            if (databaseConnector.getStatusId() == 1)
                                controller.getLoginMessageLabel().setText(databaseConnector.getStatus());
                            else
                                controller.getLoginMessageLabel().setText("Error: 1");
                        }

                    }
                });
                new Thread(task).start();
            }

        });
        controller.getButton_refresh().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        update();
                        return null;
                    }
                };
                new Thread(task).start();
            }
        });

        controller.getButton_statusapply().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.getButton_statusapply().setDisable(true);
                Task<Boolean> submit = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        Complaint c = null;
                        for (Complaint complaint : complaints)
                            if (complaint.getID() == Integer.valueOf(controller.getLabel_complaintid().getText())) {
                                c = complaint;
                            }
                        if(databaseConnector.changeStatus(user, controller.getChoice_status().getSelectionModel().getSelectedIndex(), c, 56))
                        {
                            assert c != null;
                            c.setStatus(controller.getChoice_status().getSelectionModel().getSelectedItem().toString());
                            controller.getButton_statusapply().setDisable(false);
                            return true;
                        }
                        controller.getButton_statusapply().setDisable(false);
                        return false;
                    }
                };
                submit.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        controller.getButton_reply().setDisable(false);

                        if (!submit.getValue()) {
                            Stage dialogStage = new Stage();
                            dialogStage.initModality(Modality.WINDOW_MODAL);
                            dialogStage.setScene(new Scene(VBoxBuilder.create().
                                    children(new Text(databaseConnector.getStatus()), new Button("Ok.")).
                                    alignment(Pos.CENTER).padding(new Insets(5)).build()));
                            dialogStage.show();
                        }
                        else
                        {

                        }


                    }
                });
                new Thread(submit).start();
            }
        });


        controller.getTableview_complaint().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Complaint>() {
            @Override
            public void changed(ObservableValue<? extends Complaint> observable, Complaint oldValue, Complaint newValue) {
                if (newValue == null) {
                    controller.setEnabledComplaint(false, null);
                } else {
                    controller.setEnabledComplaint(true, newValue);
                }
            }
        });

        controller.getPane_scroll_replies().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                controller.getBox_replies().setPrefWidth(newValue.doubleValue());
            }
        });

        controller.getButton_reply().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.getButton_reply().setDisable(true);
                Reply r = new Reply(-1, Integer.valueOf(controller.getLabel_complaintid().getText()), user.getFirstName(), controller.getText_reply().getText(), null);
                Task<Boolean> submit = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return databaseConnector.submitReply(user, r, 56);
                    }
                };
                submit.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        controller.getButton_reply().setDisable(false);

                        if (!submit.getValue()) {
                            Stage dialogStage = new Stage();
                            dialogStage.initModality(Modality.WINDOW_MODAL);
                            dialogStage.setScene(new Scene(VBoxBuilder.create().
                                    children(new Text(databaseConnector.getStatus()), new Button("Ok.")).
                                    alignment(Pos.CENTER).padding(new Insets(5)).build()));
                            dialogStage.show();
                        }
                        update();

                    }
                });
                new Thread(submit).start();
            }
        });

        final Text[] tablePlaceholders = {
                new Text("Start by creating a student with the Add Student button to the right."),
                new Text("No results found.")};
        controller.getField_search().textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(complaint -> {


                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                try {
                    if (String.valueOf(complaint.getID()).contains(lowerCaseFilter)) {
                        return true;
                    } else if (complaint.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (complaint.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (complaint.getBlock().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (complaint.getRoom().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (complaint.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (complaint.getSubcategory().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (complaint.getSubject().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (complaint.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }


                    controller.getTableview_complaint().setPlaceholder(tablePlaceholders[1]);
                    return false; // Does not match.
                } catch (NullPointerException nullP) {
                    // Do nothing... For now
                }
                controller.getTableview_complaint().setPlaceholder(tablePlaceholders[1]);
                return false;
            });
            controller.getTableview_complaint().setPlaceholder(tablePlaceholders[0]);
//        studentTableView.setPlaceholder(tablePlaceholders[0]);

            // 3. Wrap the FilteredList in a SortedList.
            SortedList<Complaint> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            sortedData.comparatorProperty().bind(controller.getTableview_complaint().comparatorProperty());
            controller.getTableview_complaint().setItems(sortedData);
        });




    }

    private void startUpdateService()
    {
        mainTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(true)
                {
                    update();
                    Thread.sleep(30000);
                }
            }
        };
        new Thread(mainTask).start();
    }

    private void update()
    {
        Complaint[] newComplaints = databaseConnector.getNewComplaints(user,23);
        Reply[] newReplies = databaseConnector.getNewReplies(user, 24);
        boolean update = false;
        if(newComplaints != null)
        {
            for(Complaint complaint:newComplaints)
            {
                for(Complaint complaint1:complaints)
                    if(complaint.getID() == complaint1.getID())
                    {
                        complaint1.set(complaint);
                        break;
                    }
                complaints.add(complaint);
            }
            update = true;
        }
        if(newReplies != null)
        {
            update = true;
            for(Reply reply:newReplies) {
                for(Complaint complaint:complaints)
                {
                    if (complaint.getID() == reply.getComplaint()) {
                        complaint.addReply(reply);
                        System.out.println(reply);
                    }


                }
            }

        }
        if(update)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.setEnabledComplaint(true,controller.getTableview_complaint().getSelectionModel().getSelectedItem());
                }
            });
        }

    }





    public static void main(String[] args) {
        launch(args);
    }
}
