package Controller;

import Controller.PatientMenu;
import Database.CRUD;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import Class.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PatientAlarmManager {
    public ListView alarmListView;
    public Button createNewAlarmButton;
    public Button backToMenuButton;
    public Text new_Edit;
    public Text alarmTitleText;
    public TextField alarmTitleTextField;
    public Text hourText;
    public ChoiceBox hourChoiceBox;
    public Text minuteText;
    public ChoiceBox minuteChoiceBox;
    public Text delayHourText;
    public ChoiceBox delayHourChoiceBox;
    public Text ERRORText;
    public Button create_EditButton;

    private static final String DEFAULT_CONTROL_INNER_BACKGROUND = "#ffff59";
    private static final String HIGHLIGHTED_CONTROL_INNER_BACKGROUND = "derive(red, 50%)";
    private ArrayList<Integer> alarmIDs = new ArrayList<Integer>();
    private int thisAlarmID;


    public void initialize() throws SQLException {
        listViewOutput();
        for (int i = 0; i < 10; i++) {
            hourChoiceBox.getItems().add("0"+i);
            minuteChoiceBox.getItems().add("0"+i);
            delayHourChoiceBox.getItems().add("0"+i);
        }
        for (int i = 10; i <= 23; i++) {
            hourChoiceBox.getItems().add(i);
            minuteChoiceBox.getItems().add(i);
            delayHourChoiceBox.getItems().add(i);
        }
        for (int i = 14; i <= 59; i++) {
            minuteChoiceBox.getItems().add(i);
        }

    }

    private void listViewOutput() throws SQLException {
        alarmListView.getItems().clear();
        PatientMenu.alarms = CRUD.alarmList(CRUD.getCurrentPatient().getEmail());
        for (Alarm alarm : PatientMenu.alarms){
            if (alarm.isActive()){
                System.out.println(alarm.isUsed());
                if (alarm.isUsed()){
                    alarmListView.getItems().add(alarm.getTitle());
                }
                else{
                    alarmListView.getItems().add("Missed : " + alarm.getTitle());
                }
                alarmIDs.add(alarm.getAlarmID());
            }
        }
        alarmListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                            setStyle("-fx-control-inner-background: #c2c2c2 ;");
                        } else {
                            setText(item);
                            if (item.startsWith("Missed : ")) {
                                setStyle("-fx-control-inner-background: " + HIGHLIGHTED_CONTROL_INNER_BACKGROUND + ";");
                            } else {
                                setStyle("-fx-control-inner-background: " + DEFAULT_CONTROL_INNER_BACKGROUND + ";");
                            }
                        }
                    }
                };
            }
        });
    }

    public void listViewChange() throws SQLException {
        setAllVisible();
        thisAlarmID = alarmIDs.get(alarmListView.getSelectionModel().getSelectedIndex());
        for (Alarm alarm : PatientMenu.alarms){
            if (alarm.getAlarmID()==thisAlarmID){
                new_Edit.setText("Edit Alarm");
                alarmTitleTextField.setText(alarm.getTitle());
                hourChoiceBox.getSelectionModel().select(alarm.getHour());
                minuteChoiceBox.getSelectionModel().select(alarm.getMinute());
                delayHourChoiceBox.getSelectionModel().select(alarm.getDelayHour());
                create_EditButton.setText("Edit Alarm");
            }
        }
    }

    public void handleButtonAction(ActionEvent event) throws Exception{
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("'") + 1);
        eventName = eventName.substring(0, eventName.indexOf("'"));
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fxmlLoader = null;
        if (eventName.equals("Create Alarm")){
            if (hourChoiceBox.getSelectionModel().getSelectedItem()==null
                 || minuteChoiceBox.getSelectionModel().getSelectedItem()==null
                    || delayHourChoiceBox.getSelectionModel().getSelectedItem()==null
                    || alarmTitleTextField.getText().equals("")){
                ERRORText.setText("Please Compelet All Fields First!");
            }
            else {
                ERRORText.setText("");
                boolean result = CRUD.newAlarm(hourChoiceBox.getSelectionModel().getSelectedIndex()
                        , minuteChoiceBox.getSelectionModel().getSelectedIndex()
                        , delayHourChoiceBox.getSelectionModel().getSelectedIndex()
                        , alarmTitleTextField.getText(), CRUD.getCurrentPatient().getEmail());
                if (result){
                    create_EditButton.setText("Created");
                    initialize();
                }
            }
        }
        else if (eventName.equals("Created")){
            create_EditButton.setText("Create Alarm");
        }
        else if (eventName.equals("Edit Alarm")){
            if (hourChoiceBox.getSelectionModel().getSelectedItem()==null
                    || minuteChoiceBox.getSelectionModel().getSelectedItem()==null
                    || delayHourChoiceBox.getSelectionModel().getSelectedItem()==null
                    || alarmTitleTextField.getText().equals("")){
                ERRORText.setText("Please Compelet All Fields First!");
            }
            else {
                ERRORText.setText("");
                boolean result = CRUD.updateAlarm(thisAlarmID, hourChoiceBox.getSelectionModel().getSelectedIndex()
                        , minuteChoiceBox.getSelectionModel().getSelectedIndex()
                        , delayHourChoiceBox.getSelectionModel().getSelectedIndex()
                        , alarmTitleTextField.getText());
                if (result){
                    create_EditButton.setText("Edited");
                    initialize();;
                }
            }
        }
        else if (eventName.equals("Edited")){
            create_EditButton.setText("Edit Alarm");
        }
        else if (eventName.equals("Create New Alarm")){
            setAllVisible();
            new_Edit.setText("New Alarm");
            create_EditButton.setText("Create Alarm");
            hourChoiceBox.getSelectionModel().clearSelection();
            minuteChoiceBox.getSelectionModel().clearSelection();
            delayHourChoiceBox.getSelectionModel().clearSelection();
            alarmTitleTextField.setText("");
        }
        else if (eventName.equals("Back To Menu")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
    }

    private void setAllVisible(){
        new_Edit.setVisible(true);
        alarmTitleText.setVisible(true);
        alarmTitleTextField.setVisible(true);
        hourText.setVisible(true);
        hourChoiceBox.setVisible(true);
        minuteText.setVisible(true);
        minuteChoiceBox.setVisible(true);
        delayHourText.setVisible(true);
        delayHourChoiceBox.setVisible(true);
        create_EditButton.setVisible(true);
    }
}
