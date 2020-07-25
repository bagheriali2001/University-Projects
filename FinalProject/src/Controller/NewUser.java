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
import java.time.LocalDate;
import java.sql.Date;

public class NewUser {

    public ChoiceBox typeChoiceBox;
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
    public Text weight_specialityText;
    public TextField weight_specialityTextField;
    public Text height_licenseNoText;
    public TextField height_licenseNoTextField;
    public Button selectAvatarButton;
    public ImageView avatarIMG;
    public Button selectLicenseButton;
    public ImageView licenseIMG;
    public Button createButton;
    public Button cancelButton;
    public Text errorText;

    private String typeSelected;
    private Date birthdayDateConverted;
    private String avatarIMGPath;
    private String licenseIMGPath;

    FileChooser fileChooser = new FileChooser();


    public void choiceBoxChange(ActionEvent actionEvent) {
        typeSelected = typeChoiceBox.getSelectionModel().getSelectedItem().toString();
        if (typeSelected.equals("Docter")) {
            setAllVisible();
            selectLicenseButton.setVisible(true);
            licenseIMG.setVisible(true);
            weight_specialityText.setText("Speciality :");
            height_licenseNoText.setText("License No. :");
        } else if (typeSelected.equals("Patient")) {
            setAllVisible();
            selectLicenseButton.setVisible(false);
            licenseIMG.setVisible(false);
            weight_specialityText.setText("Weight :   ");
            height_licenseNoText.setText("Height :        ");
        }
    }

    private void setAllVisible() {
        firstNameText.setVisible(true);
        firstNameTextField.setVisible(true);
        lastNameText.setVisible(true);
        lastNameTextField.setVisible(true);
        emailText.setVisible(true);
        emailTextField.setVisible(true);
        passwordText.setVisible(true);
        passwordTextField.setVisible(true);
        birthdayText.setVisible(true);
        birthdayDatePicker.setVisible(true);
        genderText.setVisible(true);
        genderChoiceBox.setVisible(true);
        weight_specialityText.setVisible(true);
        weight_specialityTextField.setVisible(true);
        height_licenseNoText.setVisible(true);
        height_licenseNoTextField.setVisible(true);
        selectAvatarButton.setVisible(true);
        avatarIMG.setVisible(true);
        createButton.setVisible(true);
        cancelButton.setVisible(true);
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
        else if (eventName.equals("Select License Image")){
            licenseIMG.setImage(image);
            licenseIMGPath=file.toURI().toString();
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
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/login.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Create")){
            if (firstNameTextField.getText().equals("") || lastNameTextField.getText().equals("")
                    || emailTextField.getText().equals("") || passwordTextField.getText().equals("")
                    || birthdayDateConverted==null || genderChoiceBox.getSelectionModel().getSelectedItem().toString().equals("")
                    || weight_specialityTextField.getText().equals("") || height_licenseNoTextField.getText().equals("")
                    || avatarIMGPath == null || (licenseIMGPath==null && typeChoiceBox.getSelectionModel().getSelectedItem().equals("Doctor"))){
                errorText.setText("Please Compelet All Fields First!");
            }
            else if (!Utilitiy.emailValidation(emailTextField.getText())){
                errorText.setText("Email Is Not Valid !");
            }
            else if (!Utilitiy.passwordValidation(passwordTextField.getText())){
                errorText.setText("Password Is Too Weak !");
            }
            else {
                if (typeSelected.equals("Docter")) {
                    boolean isCreated = CRUD.createDocterUser(firstNameTextField.getText(),lastNameTextField.getText()
                            ,emailTextField.getText(),passwordTextField.getText()
                            ,birthdayDateConverted,genderChoiceBox.getSelectionModel().getSelectedItem().toString()
                            ,weight_specialityTextField.getText(),height_licenseNoTextField.getText()
                            ,avatarIMGPath,licenseIMGPath);
                    if (isCreated){
                        fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/login.fxml"));
                        Parent root = (Parent) fxmlLoader.load();
                        scene.setRoot(root);
                    }else{
                        errorText.setText("Sorry , This Email or License No. has been used . Please choose another one !");
                    }

                }
                else if (typeSelected.equals("Patient")) {
                    boolean isCreated = CRUD.createPatientUser(firstNameTextField.getText(),lastNameTextField.getText()
                            ,emailTextField.getText(),passwordTextField.getText()
                            ,birthdayDateConverted,genderChoiceBox.getSelectionModel().getSelectedItem().toString()
                            ,weight_specialityTextField.getText(),height_licenseNoTextField.getText()
                            ,avatarIMGPath);
                    if (isCreated){
                        fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/login.fxml"));
                        Parent root = (Parent) fxmlLoader.load();
                        scene.setRoot(root);
                    }else{
                        errorText.setText("Sorry , This Email has been used . Please choose another one !");
                    }

                }
            }
        }

    }

    public void dateChange (){
        LocalDate localDate = birthdayDatePicker.getValue();
        birthdayDateConverted = Date.valueOf(localDate);
    }
}
