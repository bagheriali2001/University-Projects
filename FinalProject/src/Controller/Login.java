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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login {
    public Text emailText;
    public TextField emailTextField;
    public Text passwordText;
    public TextField passwordTextField;
    public Button loginButton;
    public Text ErrorText;
    public Text signupLink;
    public Text passwordChangeButton;


    @FXML
    public void handleLinkAction(MouseEvent event) throws Exception{
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("id=") + 3);
        eventName = eventName.substring(0, eventName.indexOf(", text="));

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fxmlLoader = null;
        if(eventName.equals("signupLink")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/newUser.fxml"));
        }
        else if(eventName.equals("passwordChangeButton")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/forgetPassword.fxml"));
        }
        Parent root = (Parent) fxmlLoader.load();

        scene.setRoot(root);
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
        if (emailTextField.getText().equals("") || passwordTextField.getText().equals("")){
            ErrorText.setText("Please Compelet All Fields First!");
        }
        else if (!Utilitiy.emailValidation(emailTextField.getText())){
            ErrorText.setText("Email Is Not Valid !");
        }
        else {
            int logNumber = CRUD.login(emailTextField.getText(),passwordTextField.getText());
            if (logNumber==1){
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMenu.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                scene.setRoot(root);
            }
            else if (logNumber==2){
                fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMenu.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                scene.setRoot(root);
            }
            else{
                ErrorText.setText("Sorry ,Email or Password does not match. Please try another one !");
            }
        }

    }

    @FXML
    public void validation(String event){
        System.out.println(event);
    }
}
