package Controller;

import Database.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.Date;
import java.sql.SQLException;

public class DoctorEditInformationPage {
    public TextArea informationTextArea;
    public TextField newPostTitleTextField;
    public TextArea newPostContentTextArea;
    public Text ERRORText;
    public Button updateInformationButton;
    public Button uploadNewPostButton;
    public Button seeAllThePostButton;
    public Button backToMenuButton;

    public void initialize() throws SQLException {
        informationTextArea.setText(CRUD.getCurrentDoctor().getInformation());
    }

    public void handleButtonAction(ActionEvent event) throws Exception{
        String eventName = event.toString();
        eventName = eventName.substring(eventName.indexOf("'") + 1);
        eventName = eventName.substring(0, eventName.indexOf("'"));

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = stage.getScene();
        FXMLLoader fxmlLoader = null;
        if(eventName.equals("Update Information")){
            ERRORText.setText("");
            if (informationTextArea.getText().equals("")){
                informationTextArea.setStyle("-fx-text-box-border: #B22222;");
            }
            else {
                informationTextArea.setStyle("-fx-text-box-border: #000000;");
                CRUD.getCurrentDoctor().setInformation(informationTextArea.getText());
                boolean result = CRUD.updateInformation(informationTextArea.getText(), CRUD.getCurrentDoctor().getEmail());
                if (result)
                    updateInformationButton.setText("Updated");
            }
        }
        else if (eventName.equals("Updated")){
            ERRORText.setText("");
            informationTextArea.setStyle("-fx-text-box-border: #000000;");
            uploadNewPostButton.setText("Update Information");
        }
        else if (eventName.equals("Upload New Post")){
            informationTextArea.setStyle("-fx-text-box-border: #000000;");
            if (newPostContentTextArea.getText().equals("") || newPostTitleTextField.getText().equals("")){
                ERRORText.setText("Please Compelet All Fields First!");
            }
            else {
                ERRORText.setText("");
                long date=System.currentTimeMillis();
                Date dateConverted = new Date(date);
                boolean result = CRUD.uploadPost(CRUD.getCurrentDoctor().getEmail()
                        , newPostContentTextArea.getText(), dateConverted, newPostTitleTextField.getText());
                if (result)
                    uploadNewPostButton.setText("Uploaded");
            }
        }
        else if (eventName.equals("Uploaded")){
            ERRORText.setText("");
            informationTextArea.setStyle("-fx-text-box-border: #000000;");
            uploadNewPostButton.setText("Upload New Post");
        }
        else if (eventName.equals("Saw All The Post")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorPostList.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Back to Menu")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Doctor/doctorMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }


    }
}
