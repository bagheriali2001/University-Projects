package Controller;

import Database.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DoctorMenu {

    public Text helloMassage;
    public Button editProfileButton;
    public Button appointmentListButton;
    public Button chatWithPatientButton;
    public Button searchMyPatientInformation;
    public Button editYourInformationButton;
    public Button logoutButton;
    public ImageView avatarImage;

    String avatarIMGPath;


    @FXML
    public void initialize() {
        helloMassage.setText("Hello " + Database.CRUD.getCurrentDoctor().getFirstName() + " " + Database.CRUD.getCurrentDoctor().getLastName() );
        avatarIMGPath = CRUD.getCurrentDoctor().getAvatarIMGPath();
        Image avatar = new Image(avatarIMGPath);
        avatarImage.setImage(avatar);
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
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorProfileEdit.fxml"));
                break;
            case "Appointments List":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorAppointmentList.fxml"));

                break;
            case "Chat with Patient":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMassages.fxml"));
                break;
            case "Search My Patient Information":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorSearchPatient.fxml"));
                break;
            case "Edit Your Information Page":
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorEditInformationPage.fxml"));
                break;
            case "Log out":
                CRUD.getCurrentDoctor().empty();
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/login.fxml"));
                break;
        }
        Parent root = (Parent) fxmlLoader.load();
        scene.setRoot(root);
    }
}
