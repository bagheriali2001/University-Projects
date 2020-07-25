package Controller;

import Class.Appointment;
import Class.Patient;
import Database.CRUD;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorAppointmentList {
    public ListView appointmentListView;
    public Text patientText;
    public Text patientNameText;
    public Text reasonText;
    public TextArea reasonTextArea;
    public Text dateRequestedText;
    public DatePicker dateRequestedDatePicker;
    public Button openFileButton;
    public Text appointmentStatusText;
    public ChoiceBox statusChoiceBox;
    public Text doctorAnswerText;
    public TextArea doctorAnswerTextArea;
    public Text ERRORText;
    public Button saveChangesButton;
    public Button goToChatButton;
    public Button goToInformationButton;
    public Button backToMenuButton;

    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private ArrayList<Patient> patients = new ArrayList<Patient>();
    private String filePath;
    @FXML
    public void handleButtonAction(ActionEvent event) throws Exception{
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("'") + 1);
        eventName = eventName.substring(0, eventName.indexOf("'"));

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fxmlLoader = null;
        if(eventName.equals("Save Changes")){
            if ((doctorAnswerTextArea.getText().equals("") || statusChoiceBox.getSelectionModel().getSelectedItem()==null)){
                ERRORText.setText("Please Compelet All Fields First!");
            }
            else {
                ERRORText.setText("");
                String appointmentID = appointmentListView.getSelectionModel().getSelectedItem().toString();
                appointmentID = appointmentID.substring( appointmentID.indexOf("ID : ") + 5 , appointmentID.indexOf(" Patient"));
                int acception = 0;
                if (statusChoiceBox.getSelectionModel().getSelectedIndex()==0){
                    acception=0;
                    goToChatButton.setDisable(true);
                    goToInformationButton.setDisable(true);
                }
                else if (statusChoiceBox.getSelectionModel().getSelectedIndex()==1){
                    acception=1;
                    goToChatButton.setDisable(false);
                    goToInformationButton.setDisable(false);
                }
                else if (statusChoiceBox.getSelectionModel().getSelectedIndex()==2){
                    acception=-1;
                    goToChatButton.setDisable(true);
                    goToInformationButton.setDisable(true);
                }
                boolean result = CRUD.updateAppointment(appointmentID,doctorAnswerTextArea.getText(),acception);
                if (result){
                    saveChangesButton.setText("Saved !");
                }
            }
        }
        else if (eventName.equals("Go To Chat")){
            String appointmentID = appointmentListView.getSelectionModel().getSelectedItem().toString();
            appointmentID = appointmentID.substring( appointmentID.indexOf("ID : ") + 5 , appointmentID.indexOf(" Patient"));
            for (Appointment appointment : appointments){
                if (Integer.parseInt(appointmentID)==appointment.getAppointmentsID()){
                    CRUD.chatForce=appointment.getFrom();
                    fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMassages.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    scene.setRoot(root);
                    break;
                }
            }
        }
        else if (eventName.equals("Go To Information")){
            String appointmentID = appointmentListView.getSelectionModel().getSelectedItem().toString();
            appointmentID = appointmentID.substring( appointmentID.indexOf("ID : ") + 5 , appointmentID.indexOf(" Patient"));
            for (Appointment appointment : appointments){
                if (Integer.parseInt(appointmentID)==appointment.getAppointmentsID()){
                    CRUD.informationForce=appointment.getFrom();
                    fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorSearchPatient.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    scene.setRoot(root);
                    break;
                }
            }
        }
        else if (eventName.equals("Back to Menu")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }


    }

    public void initialize() throws SQLException {
        setAllUnVisible();
        appointments = CRUD.appointmentList(CRUD.getCurrentDoctor().getEmail(),"Doctor");
        patients = CRUD.patientsList();
        String appointmentTitle = null;
        for (Appointment appointment:appointments){
            for (Patient patient : patients){
                if (appointment.getFrom().equals(patient.getEmail())){
                    appointmentTitle =" Patient Name : " + patient.getFirstName() + " " + patient.getLastName() ;
                    break;
                }
            }
            appointmentListView.getItems().add( "Appointment ID : " + appointment.getAppointmentsID() + appointmentTitle);
        }
    }

    public void listViewChange() {
        ERRORText.setText("");
        String appointmentID = appointmentListView.getSelectionModel().getSelectedItem().toString();
        appointmentID = appointmentID.substring( appointmentID.indexOf("ID : ") + 5 , appointmentID.indexOf(" Patient"));
        setAllVisible();
        for (Appointment appointment:appointments){
            if (appointmentID.equals(Integer.toString(appointment.getAppointmentsID()))){
                String patientName = null;
                for (Patient patient : patients){
                    if (appointment.getTo().equals(patient.getEmail())){
                        patientName = patient.getFirstName() + " " + patient.getLastName() ;
                        break;
                    }
                }
                patientNameText.setText(patientName);
                reasonTextArea.setText(appointment.getContent());
                dateRequestedDatePicker.setValue(appointment.getTimeRequested());
                if (appointment.getAcceptation()==0){
                    statusChoiceBox.getSelectionModel().select(0);
                    goToChatButton.setDisable(true);
                    goToInformationButton.setDisable(true);
                }
                else if (appointment.getAcceptation()==1){
                    statusChoiceBox.getSelectionModel().select(1);
                    goToChatButton.setDisable(false);
                    goToInformationButton.setDisable(false);
                }
                else if (appointment.getAcceptation()==-1){
                    statusChoiceBox.getSelectionModel().select(2);
                    goToChatButton.setDisable(true);
                    goToInformationButton.setDisable(true);
                }
                doctorAnswerTextArea.setText(appointment.getDoctorResponse());
                filePath=appointment.getFilePath();

            }
        }
        saveChangesButton.setText("Save Changes");
    }

    private void setAllVisible(){
        patientText.setVisible(true);
        patientNameText.setVisible(true);
        reasonText.setVisible(true);
        reasonTextArea.setVisible(true);
        dateRequestedText.setVisible(true);
        dateRequestedDatePicker.setVisible(true);
        openFileButton.setVisible(true);
        appointmentStatusText.setVisible(true);
        statusChoiceBox.setVisible(true);
        doctorAnswerText.setVisible(true);
        saveChangesButton.setVisible(true);
        goToChatButton.setVisible(true);
        goToInformationButton.setVisible(true);
        doctorAnswerTextArea.setVisible(true);
    }
    private void setAllUnVisible(){
        patientText.setVisible(false);
        patientNameText.setVisible(false);
        reasonText.setVisible(false);
        reasonTextArea.setVisible(false);
        dateRequestedText.setVisible(false);
        dateRequestedDatePicker.setVisible(false);
        openFileButton.setVisible(false);
        appointmentStatusText.setVisible(false);
        statusChoiceBox.setVisible(false);
        doctorAnswerText.setVisible(false);
        saveChangesButton.setVisible(false);
        goToChatButton.setVisible(false);
        goToInformationButton.setVisible(false);
        doctorAnswerTextArea.setVisible(false);
    }

    public void openFile(ActionEvent event) throws IOException {
        Runtime.getRuntime().exec(new String[]
                {"rundll32", "url.dll,FileProtocolHandler", filePath});
    }


}
