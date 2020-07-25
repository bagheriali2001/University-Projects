package Controller;

import Database.CRUD;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import Class.*;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class PatientMenu {

    public Text helloMassage;
    public Button editProfileButton;
    public Button requestAppointmentButton;
    public Button chatWithDoctorButton;
    public Button medicineButton;
    public Button doctorInformationButton;
    public Button logoutButton;
    public ImageView avatarImage;

    Timer timer = new Timer();
    Alert alert = new Alert(Alert.AlertType.NONE);
    public static ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    String avatarIMGPath;


    @FXML
    public void initialize() throws SQLException {
        helloMassage.setText("Hello " + Database.CRUD.getCurrentPatient().getFirstName() + " " + Database.CRUD.getCurrentPatient().getLastName());
        avatarIMGPath = CRUD.getCurrentPatient().getAvatarIMGPath();
        Image avatar = new Image(avatarIMGPath);
        avatarImage.setImage(avatar);
        //for medicen
        alarms = CRUD.alarmList(CRUD.getCurrentPatient().getEmail());
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalTime current = LocalTime.now();
                for (Alarm alarm : PatientMenu.alarms){
                    LocalTime localTime = null;
                    if (alarm.isActive()){
                        if (alarm.getHour()<10 && alarm.getMinute()<10){
                            localTime = LocalTime.parse("0" + alarm.getHour() + ":" + "0" + alarm.getMinute() + ":00");

                        }
                        else if (alarm.getHour()<10){
                            localTime = LocalTime.parse("0" + alarm.getHour() + ":" + alarm.getMinute() + ":00");

                        }
                        else if (alarm.getMinute()<10){
                            localTime = LocalTime.parse(alarm.getHour() + ":" + "0" + alarm.getMinute() + ":00");

                        }
                        else {
                            localTime = LocalTime.parse(alarm.getHour() + ":" + alarm.getMinute() + ":00");
                        }
                        if (current.compareTo(localTime)>=0){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    alert.setAlertType(Alert.AlertType.CONFIRMATION);
                                    alert.setContentText(alarm.getTitle() + "\n" + alarm.getHour() + ":" + alarm.getMinute());
                                    alert.setTitle("Medicine Alarm");
                                    alert.setHeaderText("Time To Eat Medicine");
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if(!result.isPresent()){
                                        alarm.notUsed();
                                    }
                                    else if(result.get() == ButtonType.OK){
                                        if (alarm.getDelayHour()==0){
                                            alarm.setActive(false);
                                            try {
                                                boolean res = CRUD.deleteAlarm(alarm.getAlarmID());
                                                if (res){
                                                }
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                        }
                                        else {
                                            alarm.next();
                                            try {
                                                boolean res = CRUD.updateAlarm(alarm.getAlarmID(),alarm.getMinute()
                                                        ,alarm.getHour(),alarm.getDelayHour(),alarm.getTitle());
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                        }
                                    }
                                    //oke button is pressed
                                    else if(result.get() == ButtonType.CANCEL){
                                        try {
                                            boolean res = CRUD.notUsedAlarm(alarm.getAlarmID());
                                            if (res){
                                            }
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }, 0 ,60000);
    }

    @FXML
    public void handleButtonAction(ActionEvent event) throws Exception{
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("'") + 1);
        eventName = eventName.substring(0, eventName.indexOf("'"));

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fxmlLoader = null;
        switch (eventName){
            case "Edit My Profile":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientProfileEdit.fxml"));
                break;
            case "Request Appointment":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientRequestAppointment.fxml"));

                break;
            case "Chat with Doctor":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMassages.fxml"));
                break;
            case "Medicine":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientAlarmManager.fxml"));
                break;
            case "Doctors Information":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientDoctorsInformationView.fxml"));
                break;
            case "Log out":
                CRUD.getCurrentPatient().empty();
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/login.fxml"));
                break;
        }
        Parent root = (Parent) fxmlLoader.load();
        scene.setRoot(root);
    }
}
