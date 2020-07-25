package Controller;

import Database.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Class.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientAppointmentList {
    public ListView appointmentListView;
    public Text doctorText;
    public Text doctorNameText;
    public Text specialityText;
    public Text specialityTypeText;
    public Text reasonText;
    public TextArea reasonTextArea;
    public Text dateRequestedText;
    public DatePicker dateRequestedDatePicker;
    public Button openFileButton;
    public Text appointmentStatusText;
    public Text appointmentStatusTextAnswer;
    public Text doctorAnswerText;
    public TextArea doctorAnswerTextArea;
    public Button backToRequestAppointmentPageButton;
    public Button goToChatButton;
    public Button goToInformationButton;
    public Button backToMenuButton;

    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private ArrayList<Doctor> doctors = new ArrayList<Doctor>();
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
        if(eventName.equals("Back to Request Appointment Page")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientRequestAppointment.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Go To Chat")){
            String appointmentID = appointmentListView.getSelectionModel().getSelectedItem().toString();
            appointmentID = appointmentID.substring( appointmentID.indexOf("ID : ") + 5 , appointmentID.indexOf(" Doctor"));
            for (Appointment appointment : appointments){
                if (Integer.parseInt(appointmentID)==appointment.getAppointmentsID()){
                    CRUD.chatForce=appointment.getTo();
                    fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMassages.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    scene.setRoot(root);
                    break;
                }
            }
        }
        else if (eventName.equals("Go To Information")){
            String appointmentID = appointmentListView.getSelectionModel().getSelectedItem().toString();
            appointmentID = appointmentID.substring( appointmentID.indexOf("ID : ") + 5 , appointmentID.indexOf(" Doctor"));
            for (Appointment appointment : appointments){
                if (Integer.parseInt(appointmentID)==appointment.getAppointmentsID()){
                    CRUD.informationForce=appointment.getTo();
                    fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientDoctorsInformationView.fxml"));
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

    public void initialize() throws SQLException {
        appointments = CRUD.appointmentList(CRUD.getCurrentPatient().getEmail(),"Patient");
        doctors = CRUD.doctorsList();
        String appointmentTitle = null;
        for (Appointment appointment:appointments){
            for (Doctor doc : doctors){
                if (appointment.getTo().equals(doc.getEmail())){
                    appointmentTitle =" Doctor Name : " + doc.getFirstName() + " " + doc.getLastName() ;
                    break;
                }
            }
            appointmentListView.getItems().add( "Appointment ID : " + appointment.getAppointmentsID() + appointmentTitle);
        }
    }

    public void listViewChange() {
        String appointmentID = appointmentListView.getSelectionModel().getSelectedItem().toString();
        appointmentID = appointmentID.substring( appointmentID.indexOf("ID : ") + 5 , appointmentID.indexOf(" Doctor"));
        setAllVisible();
        System.out.println(appointmentID);
        for (Appointment appointment:appointments){
            System.out.println(appointment.getAppointmentsID());
            if (appointmentID.equals(Integer.toString(appointment.getAppointmentsID()))){
                String doctorName = null;
                String doctorSpeciality = null;
                for (Doctor doc : doctors){
                    if (appointment.getTo().equals(doc.getEmail())){
                        doctorName = doc.getFirstName() + " " + doc.getLastName() ;
                        doctorSpeciality = doc.getSpeciality();
                        break;
                    }
                }
                doctorNameText.setText(doctorName);
                specialityTypeText.setText(doctorSpeciality);
                reasonTextArea.setText(appointment.getContent());
                dateRequestedDatePicker.setValue(appointment.getTimeRequested());
                if (appointment.getAcceptation()==0){
                    appointmentStatusTextAnswer.setText("Doctor didn't see or answer your request yet ! ");
                    goToChatButton.setDisable(true);
                }
                else if (appointment.getAcceptation()==1){
                    appointmentStatusTextAnswer.setText("Doctor accepted your request ! ");
                    goToChatButton.setDisable(false);
                }
                else if (appointment.getAcceptation()==-1){
                    appointmentStatusTextAnswer.setText("Doctor rejected your request ! ");
                    goToChatButton.setDisable(true);
                }
                doctorAnswerTextArea.setText(appointment.getDoctorResponse());
                filePath=appointment.getFilePath();
            }
        }

    }

    private void setAllVisible(){
        doctorText.setVisible(true);
        doctorNameText.setVisible(true);
        specialityText.setVisible(true);
        specialityTypeText.setVisible(true);
        reasonText.setVisible(true);
        reasonTextArea.setVisible(true);
        dateRequestedText.setVisible(true);
        dateRequestedDatePicker.setVisible(true);
        openFileButton.setVisible(true);
        appointmentStatusText.setVisible(true);
        appointmentStatusTextAnswer.setVisible(true);
        doctorAnswerText.setVisible(true);
        goToChatButton.setVisible(true);
        goToInformationButton.setVisible(true);
        doctorAnswerTextArea.setVisible(true);
    }

    public void openFile(ActionEvent event) throws IOException {
        Runtime.getRuntime().exec(new String[]
                {"rundll32", "url.dll,FileProtocolHandler", filePath});
    }
}
