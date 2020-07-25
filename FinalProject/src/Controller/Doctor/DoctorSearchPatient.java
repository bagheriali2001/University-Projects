package Controller;

import Database.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import Class.*;

public class DoctorSearchPatient {
    public ChoiceBox patientNameChoiceBox;
    public ImageView avatarImage;
    public Text firstNameText;
    public TextField firstNameTextField;
    public Text lastNameText;
    public TextField lastNameTextField;
    public Text birthdayText;
    public DatePicker birthdayDatePicker;
    public Text genderText;
    public TextField genderTextField;
    public Text weightText;
    public TextField weightTextField;
    public Text heightText;
    public TextField heightTextField;
    public Button goToChatButton;
    public Button backToMenuButton;

    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private ArrayList<Patient> patients = new ArrayList<Patient>();
    Patient currentPatient = null;
    String avatarIMGPath;


    public void initialize() throws SQLException {
        appointments = CRUD.acceptedAppointmentList(CRUD.getCurrentDoctor().getEmail(),"Doctor");
        patients = CRUD.patientsFullList();
        String contantTitle = null;
        String contantEmail = null;
        for (Appointment appointment:appointments){
            for (Patient patient : patients){
                if (appointment.getFrom().equals(patient.getEmail())){
                    contantTitle = patient.getFirstName() + " " + patient.getLastName() ;
                    contantEmail = patient.getEmail();
                    break;
                }
            }
            patientNameChoiceBox.getItems().add(contantTitle);
            if (CRUD.informationForce!=null && CRUD.informationForce.equals(contantEmail)){
                patientNameChoiceBox.getSelectionModel().select(contantTitle);
                choiceBoxViewChange();
                CRUD.informationForce="";
            }
        }
    }

    public void choiceBoxViewChange() throws SQLException {
        String patientName = patientNameChoiceBox.getSelectionModel().getSelectedItem().toString();
        setAllVisible();
        for (Patient patient : patients){
            if (patientName.equals(patient.getFirstName() + " " + patient.getLastName())){
                currentPatient=patient;
                break;
            }
        }
        setAllVisible();
        firstNameTextField.setText(currentPatient.getFirstName());
        lastNameTextField.setText(currentPatient.getLastName());
        birthdayDatePicker.setValue(currentPatient.getBirthday());
        genderTextField.setText(currentPatient.getGender());
        weightTextField.setText(currentPatient.getWeight());
        heightTextField.setText(currentPatient.getHeight());
        avatarIMGPath = currentPatient.getAvatarIMGPath();
        Image avatar = new Image(avatarIMGPath);
        avatarImage.setImage(avatar);
    }

    public void handleButtonAction(ActionEvent event) throws Exception{
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("'") + 1);
        eventName = eventName.substring(0, eventName.indexOf("'"));
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fxmlLoader = null;
        if(eventName.equals("Back to Menu")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if(eventName.equals("Go To Chat")){
            CRUD.chatForce=currentPatient.getEmail();
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMassages.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
    }

    private void setAllVisible(){
        firstNameText.setVisible(true);
        firstNameTextField.setVisible(true);
        lastNameText.setVisible(true);
        lastNameTextField.setVisible(true);
        birthdayText.setVisible(true);
        birthdayDatePicker.setVisible(true);
        genderText.setVisible(true);
        genderTextField.setVisible(true);
        weightText.setVisible(true);
        weightTextField.setVisible(true);
        heightText.setVisible(true);
        heightTextField.setVisible(true);
        goToChatButton.setVisible(true);
    }
}
