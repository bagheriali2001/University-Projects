package Controller;

import Database.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import Class.Doctor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PatientRequestAppointment {
    public Text errorText;
    public TextArea appointmentReasonTextArea;
    public Button selectFileButton;
    public Text fileNameText;
    public DatePicker appointmentDatePicker;
    public ChoiceBox<String> doctorSpecialityChoiceBox;
    public ChoiceBox<String> doctorNameChoiceBox;
    public Button previousAppointmentButton;
    public Button sendRequestButtton;
    public Button cancelButton;

    private ArrayList<Doctor> doctors = new ArrayList<Doctor>();
    private ArrayList<String> specialitys = new ArrayList<String>();
    Date appointmentDateConverted;
    FileChooser fileChooser = new FileChooser();
    File file;

    @FXML
    public void initialize() throws SQLException {
        doctors = CRUD.doctorsList();
        for (Doctor doc : doctors){
            if (specialitys.size()==0){
                specialitys.add(doc.getSpeciality());
                doctorSpecialityChoiceBox.getItems().add(doc.getSpeciality());
            }
            else{
                for (String speciality : specialitys){
                    if (!doc.getSpeciality().equals(speciality)){
                        specialitys.add(doc.getSpeciality());
                        doctorSpecialityChoiceBox.getItems().add(doc.getSpeciality());
                        break;
                    }
                }
            }
        }
    }

    public void choiceBoxChange (ActionEvent event) {
        String eventName = doctorSpecialityChoiceBox.getSelectionModel().getSelectedItem();
        doctorNameChoiceBox.getItems().clear();
        for (Doctor doc : doctors) {
            if (eventName.equals(doc.getSpeciality())) {
                doctorNameChoiceBox.getItems().add(doc.getFirstName() + " " + doc.getLastName());
            }
        }
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
        if(eventName.equals("Cancel") || eventName.equals("Return")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Send Request")){
            if (doctorSpecialityChoiceBox.getSelectionModel().getSelectedItem()==null
                    || doctorNameChoiceBox.getSelectionModel().getSelectedItem()==null
                    || appointmentReasonTextArea.getText().equals("") || appointmentDateConverted==null){
                errorText.setText("Please Compelet All Fields First!");
            }
            else{
                String to = null;
                long date=System.currentTimeMillis();
                Date dateConverted = new Date(date);
                for (Doctor doc : doctors){
                    if (doctorNameChoiceBox.getSelectionModel().getSelectedItem().equals(doc.getFirstName() + " " + doc.getLastName())){
                        to = doc.getEmail();
                        break;
                    }
                }
                boolean res;
                if(file!=null){
                    res = CRUD.SendAppointmentRequest(CRUD.getCurrentPatient().getEmail()
                            , to, appointmentReasonTextArea.getText(), dateConverted
                            , appointmentDateConverted, file.toURI().toString());
                }
                else {
                    res = CRUD.SendAppointmentRequest(CRUD.getCurrentPatient().getEmail()
                            , to, appointmentReasonTextArea.getText(), dateConverted
                            , appointmentDateConverted, null);
                }
                if (res){
                    sendRequestButtton.setText("Return");
                }else{
                    errorText.setText("Sorry , We Can not make this appointmetn request");
                }
            }
        }
        else if (eventName.equals("Your Previous Appointment")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientAppointmentList.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }


    }

    public void dateChange (){
        LocalDate localDate = appointmentDatePicker.getValue();
        appointmentDateConverted = Date.valueOf(localDate);
    }

    public void chooseFile(ActionEvent event) throws IOException {
        Window stage = null;
        file = fileChooser.showOpenDialog(null);
        fileNameText.setText(file.getName());

    }
}
