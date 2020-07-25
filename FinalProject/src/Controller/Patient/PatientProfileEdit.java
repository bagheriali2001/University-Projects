package Controller;

import Database.CRUD;
import Utilitiy.Utilitiy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class PatientProfileEdit {


    public Text firstNameText;
    public TextField firstNameTextField;
    public Text lastNameText;
    public TextField lastNameTextField;
    public Text emailText;
    public TextField emailTextField;
    public Text passwordText;
    public TextField passwordTextField;
    public Text birthdayText;
    public DatePicker birthdayDatePicker;
    public Text genderText;
    public ChoiceBox genderChoiceBox;
    public Text weightText;
    public TextField weightTextField;
    public Text heightText;
    public TextField heightTextField;
    public Button selectAvatarButton;
    public ImageView avatarIMG;
    public Text errorText;
    public Button cancelButton;

    private Date birthdayDateConverted;
    private String searchKey;
    FileChooser fileChooser = new FileChooser();
    private String avatarIMGPath;

    public void initialize() {
        firstNameTextField.setText(CRUD.getCurrentPatient().getFirstName());
        lastNameTextField.setText(CRUD.getCurrentPatient().getLastName());
        emailTextField.setText(CRUD.getCurrentPatient().getEmail());
        passwordTextField.setText(CRUD.getCurrentPatient().getPassword());
        birthdayDatePicker.setValue(CRUD.getCurrentPatient().getBirthday());
        if (CRUD.getCurrentPatient().getGender().equals("Male")){
            genderChoiceBox.getSelectionModel().select(0);
        }
        else if (CRUD.getCurrentPatient().getGender().equals("Female")){
            genderChoiceBox.getSelectionModel().select(1);
        }
        weightTextField.setText(CRUD.getCurrentPatient().getWeight());
        heightTextField.setText(CRUD.getCurrentPatient().getHeight());
        avatarIMGPath=CRUD.getCurrentPatient().getAvatarIMGPath();
        Image avatar = new Image(avatarIMGPath);
        avatarIMG.setImage(avatar);
        searchKey=(CRUD.getCurrentPatient().getEmail());
    }

    public void choosePic(ActionEvent event) throws IOException {
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("'") + 1);
        eventName = eventName.substring(0, eventName.indexOf("'"));
        Window stage = null;
        File file = fileChooser.showOpenDialog(null);
        Image image = new Image(file.toURI().toString());
        if (eventName.equals("Select Avater")){
            avatarIMG.setImage(image);
            avatarIMGPath=file.toURI().toString();
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
        if(eventName.equals("Cancel")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/PatientMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Change")){
            dateChange();
            if (firstNameTextField.getText().equals("") || lastNameTextField.getText().equals("")
                    || emailTextField.getText().equals("") || passwordTextField.getText().equals("")
                    || birthdayDateConverted==null || genderChoiceBox.getSelectionModel().getSelectedItem().toString().equals("")
                    || weightTextField.getText().equals("") || heightTextField.getText().equals("")){
                errorText.setText("Please Compelet All Fields First!");
            }
            else if (!Utilitiy.emailValidation(emailTextField.getText())){
                errorText.setText("Email Is Not Valid !");
            }
            else if (!Utilitiy.passwordValidation(passwordTextField.getText())){
                errorText.setText("Password Is Too Weak !");
            }
            else {
                boolean isChanged = CRUD.editProfile("Patient",searchKey
                        ,firstNameTextField.getText(),lastNameTextField.getText()
                        ,emailTextField.getText(),passwordTextField.getText()
                        ,birthdayDateConverted,genderChoiceBox.getSelectionModel().getSelectedItem().toString()
                        ,weightTextField.getText(),heightTextField.getText()
                        ,avatarIMGPath,null);
                if (isChanged){
                    fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/PatientMenu.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    scene.setRoot(root);
                }else{
                    errorText.setText("Sorry , I couldn't change that !");
                }
            }
        }
    }

    public void dateChange(){
        LocalDate localDate = birthdayDatePicker.getValue();
        birthdayDateConverted = Date.valueOf(localDate);
    }

}
