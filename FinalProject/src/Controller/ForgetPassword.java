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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class ForgetPassword {
    Random random = new Random();

    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public Text captchaText;
    public Text captchaTextgenerated;
    public TextField captchaTextField;
    public Text newPassword;
    public Button doneButton;
    public Button cancelButton;

    private String generatedPassword;
    @FXML
    public void initialize() {
        captchaTextgenerated.setText(generateString(6));
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
        else if(eventName.equals("Change")){
            if (firstNameTextField.getText().equals("") || lastNameTextField.getText().equals("")
                    || emailTextField.getText().equals("") || captchaTextField.getText().equals("")){
                newPassword.setText("Please Complete all fields first ! ");
            }
            else if (!Utilitiy.emailValidation(emailTextField.getText())){
                newPassword.setText("Email Is Not Valid !");
            }
            else if (captchaTextgenerated.equals(captchaTextField.getText())){
                newPassword.setText("CAPTCHA does not match ! ");
                regenerateCaptcha();
            }
            else {
                generatedPassword = generateString(10);
                boolean didReset = CRUD.resetPassword(firstNameTextField.getText(),lastNameTextField.getText()
                        ,emailTextField.getText(),generatedPassword);
                if (didReset){
                    newPassword.setText("Your New Password is : " + generatedPassword );
                    doneButton.setText("Thanks");
                }
                else {
                    newPassword.setText("We don't have any user with this information ! " );
                }
            }
        }
        else if(eventName.equals("Thanks")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/login.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }

    }

    public void regenerateCaptcha() {
        captchaTextgenerated.setText(generateString(6));
    }

    private String generateString (int targetStringLength){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
