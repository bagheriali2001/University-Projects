package Controller;

import Database.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import Class.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PatientMassages {
    public ListView contactsListView;
    public Button goToInformationButton;
    public Button backToMenuButton;
    public Text contactNameText;
    public ListView massagesListView;
    public TextArea massageContentTextArea;
    public Button sendMassageButton;

    private static final String DEFAULT_CONTROL_INNER_BACKGROUND = "yellow";
    private static final String HIGHLIGHTED_CONTROL_INNER_BACKGROUND = "derive(green, 70%)";
    private ArrayList<Massage> massages = new ArrayList<Massage>();
    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private ArrayList<Doctor> doctors = new ArrayList<Doctor>();

    public void initialize() throws SQLException {
        appointments = CRUD.acceptedAppointmentList(CRUD.getCurrentPatient().getEmail(),"Patient");
        doctors = CRUD.doctorsList();
        String contantTitle = null;
        String doctorEmail= null;
        for (Appointment appointment:appointments){
            for (Doctor doc : doctors){
                if (appointment.getTo().equals(doc.getEmail())){
                    contantTitle = doc.getFirstName() + " " + doc.getLastName() ;
                    doctorEmail=doc.getEmail();
                    break;
                }
            }
            contactsListView.getItems().add(contantTitle);
            if (CRUD.chatForce!=null && CRUD.chatForce.equals(doctorEmail)){
                contactsListView.getSelectionModel().select(contantTitle);
                listViewChange();
                CRUD.chatForce="";
            }
        }
    }

    public void listViewChange() throws SQLException {
        massagesListView.getItems().clear();
        String doctorEmail = null;
        String contactName = contactsListView.getSelectionModel().getSelectedItem().toString();
        setAllVisible();
        contactNameText.setText(contactName);
        for (Doctor doctor : doctors){
            if (contactName.equals(doctor.getFirstName() + " " + doctor.getLastName())){
                doctorEmail=doctor.getEmail();
                break;
            }
        }
        massages = CRUD.massageList(CRUD.getCurrentPatient().getEmail());
        for (Massage massage : massages){
            if (massage.getFrom().equals(CRUD.getCurrentPatient().getEmail()) && massage.getTo().equals(doctorEmail)){
                massagesListView.getItems().add("You : " + massage.getContent());
            }
            else if ((massage.getTo().equals(CRUD.getCurrentPatient().getEmail()) && massage.getFrom().equals(doctorEmail))){
                massagesListView.getItems().add("Doctor : " + massage.getContent());

            }
            massagesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                                setStyle("-fx-control-inner-background: #c2c2c2;");
                            } else {
                                setText(item);
                                if (item.startsWith("D")) {
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
    }

    public void handleButtonAction(ActionEvent event) throws Exception{
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("'") + 1);
        eventName = eventName.substring(0, eventName.indexOf("'"));

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fxmlLoader = null;
        if(eventName.equals("Return to Menu")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Go To Information")){
            String to = null;
            for (Doctor doctor : doctors){
                if (contactNameText.getText().equals(doctor.getFirstName() + " " + doctor.getLastName())){
                    to=doctor.getEmail();
                    break;
                }
            }
            CRUD.informationForce=to;
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientDoctorsInformationView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Send")){
            if (massageContentTextArea.getText().equals("")){
                massageContentTextArea.setStyle("-fx-text-box-border: #B22222;");
            }
            else{
                massageContentTextArea.setStyle("-fx-text-box-border: #000000;");
                System.out.println("Send");
                String to = null;
                for (Doctor doctor : doctors){
                    if (contactNameText.getText().equals(doctor.getFirstName() + " " + doctor.getLastName())){
                        to=doctor.getEmail();
                        break;
                    }
                }
                long date=System.currentTimeMillis();
                Date dateConverted = new Date(date);
                boolean result = CRUD.sendMassage(CRUD.getCurrentPatient().getEmail(),to,dateConverted,massageContentTextArea.getText());
                System.out.println(result);
                massagesListView.getItems().add("You : " + massageContentTextArea.getText());
                massageContentTextArea.setText("");
                massagesListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        return new ListCell<String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                    setStyle("-fx-control-inner-background:  #c2c2c2;");
                                } else {
                                    setText(item);
                                    if (item.startsWith("D")) {
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
        }


    }

    private void setAllVisible(){
        goToInformationButton.setVisible(true);
        contactNameText.setVisible(true);
        massageContentTextArea.setVisible(true);
        sendMassageButton.setVisible(true);
    }
}
