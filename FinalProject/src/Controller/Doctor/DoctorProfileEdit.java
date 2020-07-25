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

public class DoctorProfileEdit {


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
    public Text specialityText;
    public TextField specialityTextField;
    public Text licenseNoText;
    public TextField licenseNoTextField;
    public Button selectAvatarButton;
    public ImageView avatarIMG;
    public Button selectLicenseButton;
    public ImageView licenseIMG;
    public Text errorText;
    public Button cancelButton;

    private Date birthdayDateConverted;
    private String searchKey;
    private String avatarIMGPath;
    private String licenseIMGPath;
    FileChooser fileChooser = new FileChooser();

    public void initialize() {
        firstNameTextField.setText(CRUD.getCurrentDoctor().getFirstName());
        lastNameTextField.setText(CRUD.getCurrentDoctor().getLastName());
        emailTextField.setText(CRUD.getCurrentDoctor().getEmail());
        passwordTextField.setText(CRUD.getCurrentDoctor().getPassword());
        birthdayDatePicker.setValue(CRUD.getCurrentDoctor().getBirthday());
        if (CRUD.getCurrentDoctor().getGender().equals("Male")){
            genderChoiceBox.getSelectionModel().select(0);
        }
        else if (CRUD.getCurrentDoctor().getGender().equals("Female")){
            genderChoiceBox.getSelectionModel().select(1);
        }        specialityTextField.setText(CRUD.getCurrentDoctor().getSpeciality());
        licenseNoTextField.setText(CRUD.getCurrentDoctor().getMedicalLicenseNumber());
        avatarIMGPath=CRUD.getCurrentDoctor().getAvatarIMGPath();
        Image avatar = new Image(avatarIMGPath);
        avatarIMG.setImage(avatar);
        licenseIMGPath=CRUD.getCurrentDoctor().getLicenseIMGPath();
        Image license = new Image(licenseIMGPath);
        licenseIMG.setImage(license);
        searchKey=(CRUD.getCurrentDoctor().getEmail());
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
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Change")){
            dateChange();
            if (firstNameTextField.getText().equals("") || lastNameTextField.getText().equals("")
                    || emailTextField.getText().equals("") || passwordTextField.getText().equals("")
                    || birthdayDateConverted==null || genderChoiceBox.getSelectionModel().getSelectedItem().toString().equals("")
                    || specialityTextField.getText().equals("") || licenseNoTextField.getText().equals("")
                    || avatarIMGPath == null || licenseIMGPath==null){
                errorText.setText("Please Compelet All Fields First!");
            }
            else if (!Utilitiy.emailValidation(emailTextField.getText())){
                errorText.setText("Email Is Not Valid !");
            }
            else if (!Utilitiy.passwordValidation(passwordTextField.getText())){
                errorText.setText("Password Is Too Weak !");
            }
            else {
                boolean isChanged = CRUD.editProfile("Doctor",searchKey
                        ,firstNameTextField.getText(),lastNameTextField.getText()
                        ,emailTextField.getText(),passwordTextField.getText()
                        ,birthdayDateConverted,genderChoiceBox.getSelectionModel().getSelectedItem().toString()
                        ,specialityTextField.getText(),licenseNoTextField.getText()
                        ,avatarIMGPath,licenseIMGPath);
                if (isChanged){
                    fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMenu.fxml"));
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
