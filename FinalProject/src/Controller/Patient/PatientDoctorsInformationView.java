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

import javax.security.auth.login.CredentialException;

public class PatientDoctorsInformationView {
    public ListView doctorsListView;
    public ChoiceBox doctorsChoiceBox;
    public Text doctorNameText;
    public ImageView avatarImage;
    public Text doctorDescriptionText;
    public TextArea informationTextArea;
    public Button seeDoctorsPostButton;
    public Button goToChatButton;
    public Button backToMenuButton;

    private ArrayList<Doctor> doctors = new ArrayList<Doctor>();
    private ArrayList<String> specialitys = new ArrayList<String>();
    private ArrayList<String> doctorsEmail = new ArrayList<String>();
    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private ArrayList<String> myDoctors = new ArrayList<String>();
    String doctorType_Email;
    String avatarIMGPath= null;

    public void initialize() throws SQLException {
        doctorsChoiceBox.getItems().add("All");
        doctorsChoiceBox.getItems().add("My Doctors");
        doctors = CRUD.doctorsListWithPath();
        String doctorEmail= null;
        for (Doctor doc : doctors){
            if (specialitys.size()==0){
                specialitys.add(doc.getSpeciality());
                doctorsChoiceBox.getItems().add(doc.getSpeciality());
                doctorEmail=doc.getEmail();
            }
            else{
                for (String speciality : specialitys){
                    if (!doc.getSpeciality().equals(speciality)){
                        specialitys.add(doc.getSpeciality());
                        doctorsChoiceBox.getItems().add(doc.getSpeciality());
                        doctorEmail=doc.getEmail();
                        break;
                    }
                }
            }
            if (CRUD.informationForce!=null && CRUD.informationForce.equals(doctorEmail)){
                doctorsChoiceBox.getSelectionModel().select(doc.getSpeciality());
                choiceBoxChange();
                doctorsListView.getSelectionModel().select(doc.getFirstName()+ " " + doc.getLastName());
                listViewChange();
                CRUD.informationForce="";
            }
        }
        appointments=CRUD.acceptedAppointmentList(CRUD.getCurrentPatient().getEmail(), "Patient");
        for (Appointment appointment : appointments){
            boolean isDoctorNameEntered = false;
            for (String docEmail : myDoctors){
                if (docEmail.equals(appointment.getTo())){
                    isDoctorNameEntered=true;
                    break;
                }
            }
            if (!isDoctorNameEntered){
                myDoctors.add(appointment.getTo());
            }
        }
    }

    public void choiceBoxChange() throws SQLException {
        doctorsListView.getItems().clear();
        doctorsEmail.clear();
        String selected = doctorsChoiceBox.getSelectionModel().getSelectedItem().toString();
        if (selected.equals("All")){
            doctorType_Email="all";
            for (Doctor doctor : doctors){
                doctorsListView.getItems().add(doctor.getFirstName() + " " + doctor.getLastName());
                doctorsEmail.add(doctor.getEmail());
            }
        }
        else if (selected.equals("My Doctors")){
            for (Doctor doctor : doctors){
                for (String doctorEmail : myDoctors){
                    if (doctor.getEmail().equals(doctorEmail)){
                        doctorsListView.getItems().add(doctor.getFirstName() + " " + doctor.getLastName());
                        doctorsEmail.add(doctor.getEmail());
                        break;
                    }
                }
            }
        }
        else {
            for (Doctor doctor : doctors){
                if (doctorsChoiceBox.getSelectionModel().getSelectedItem().toString()
                        .equals(doctor.getSpeciality())){
                    doctorType_Email=doctor.getEmail();
                    break;
                }
            }
            for (Doctor doctor : doctors){
                if (doctor.getEmail().equals(doctorType_Email)){
                    doctorsListView.getItems().add(doctor.getFirstName() + " " + doctor.getLastName());
                    doctorsEmail.add(doctor.getEmail());
                }
            }
        }
    }

    public void listViewChange() throws SQLException {
        setAllVisible();
        String thisDoctorEmail = doctorsEmail.get(doctorsListView.getSelectionModel().getSelectedIndex());
        for (Doctor doctor : doctors){
            if (doctor.getEmail().equals(thisDoctorEmail)){
                doctorNameText.setText(doctor.getFirstName()+ " " + doctor.getLastName());
                informationTextArea.setText(doctor.getInformation());
                avatarIMGPath = doctor.getAvatarIMGPath();
                Image avatar = new Image(avatarIMGPath);
                avatarImage.setImage(avatar);
                for (String doctorName : myDoctors){
                    if (doctor.getEmail().equals(doctorName)){
                        goToChatButton.setDisable(false);
                    }
                }
                break;
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
        
        if (eventName.equals("Saw Doctors Post")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientPostList.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Go To Chat")){
            for (Doctor doctor : doctors){
                if (doctorNameText.getText().equals(doctor.getFirstName()+ " " + doctor.getLastName())){
                    CRUD.chatForce=doctor.getEmail();
                    fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMassages.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    scene.setRoot(root);
                    break;
                }
            }

        }
        else if (eventName.equals("Back to Menu")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
    }

    private void setAllVisible(){
        doctorNameText.setVisible(true);
        doctorDescriptionText.setVisible(true);
        informationTextArea.setVisible(true);
        goToChatButton.setVisible(true);
    }
}
