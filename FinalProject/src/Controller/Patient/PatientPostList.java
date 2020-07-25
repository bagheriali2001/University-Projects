package Controller;

import Database.CRUD;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import Class.*;

public class PatientPostList {
    public ListView postListView;
    public ChoiceBox postFromChoiceBox;
    public Text titleText;
    public Text contentText;
    public Text postOfText;
    public Text timeText;
    public Text likeCounterText;
    public Button likeButton;
    public Button backToEditInformationPageButton;
    public Button backToMenuButton;

    private ArrayList<Doctor> doctors = new ArrayList<Doctor>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<String> likers = new ArrayList<String>();
    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private ArrayList<String> myDoctors = new ArrayList<String>();
    private ArrayList<Integer> postIDs = new ArrayList<Integer>();
    String postOf = null;
    int currentPostId;

    public void initialize() throws SQLException {
        postFromChoiceBox.getItems().add("All");
        postFromChoiceBox.getItems().add("My Doctors");
        doctors = CRUD.doctorsList();
        for (Doctor doctor : doctors){
            postFromChoiceBox.getItems().add(doctor.getFirstName() + " " + doctor.getLastName());
        }
        posts = CRUD.postList();
    }

    public void choiceBoxChange() throws SQLException {
        postListView.getItems().clear();
        postIDs.clear();
        String selected = postFromChoiceBox.getSelectionModel().getSelectedItem().toString();
        if (selected.equals("All")){
            postOf="all";
            for (Post post : posts){
                postListView.getItems().add(post.getTitle());
                postIDs.add(post.getPostID());
            }
        }
        else if (selected.equals("My Doctors")){///////////////////////////////////////////////////////////////////////////////////////////////
            appointments=CRUD.acceptedAppointmentList(CRUD.getCurrentPatient().getEmail(), "Patient");
            for (Appointment appointment : appointments){
                boolean isDoctorNameEntered = false;
                for (String doctorEmail : myDoctors){
                    if (doctorEmail.equals(appointment.getTo())){
                        isDoctorNameEntered=true;
                        break;
                    }
                }
                if (!isDoctorNameEntered){
                    myDoctors.add(appointment.getTo());
                }
            }
            for (Post post : posts){
                for (String doctorEmail : myDoctors){
                    if (post.getPostOf().equals(doctorEmail)){
                        postListView.getItems().add(post.getTitle());
                        postIDs.add(post.getPostID());
                        break;
                    }
                }
            }
        }
        else {
            for (Doctor doctor : doctors){
                if (postFromChoiceBox.getSelectionModel().getSelectedItem().toString()
                        .equals(doctor.getFirstName() + " " + doctor.getLastName())){
                    postOf=doctor.getEmail();
                    break;
                }
            }
            for (Post post : posts){
                if (post.getPostOf().equals(postOf)){
                    postListView.getItems().add(post.getTitle());
                    postIDs.add(post.getPostID());
                }
            }
        }
    }

    public void listViewChange() throws SQLException {
        setAllVisible();
        String postOfName = null;
        int thisPostID = postIDs.get(postListView.getSelectionModel().getSelectedIndex());
        for (Post post : posts){
            if (post.getPostID()==thisPostID){
                for (Doctor doctor : doctors){
                    if (post.getPostOf().equals(doctor.getEmail())){
                        postOfName=doctor.getFirstName() + " " + doctor.getLastName();
                    }
                }
                titleText.setText(post.getTitle());
                contentText.setText(post.getContent());
                postOfText.setText(postOfName);
                timeText.setText(post.getTimePosted().toString());
                likeCounterText.setText(Integer.toString(post.getLikeCounter()));
                likeButton.setText("Like");
                currentPostId = post.getPostID();
                likers = CRUD.likeList(post.getPostID());

                for (String liker : likers){
                    if (liker.equals(CRUD.getCurrentPatient().getEmail())){
                        likeButton.setText("Unlike");
                        break;
                    }
                }
            }
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
        if (eventName.equals("Like")){
            boolean result = CRUD.likeTable(currentPostId, CRUD.getCurrentPatient().getEmail(), Integer.parseInt(likeCounterText.getText()));
            if (result){
                for (Post post : posts){
                    if (currentPostId==post.getPostID()){
                        int temp = post.getLikeCounter();
                        post.setLikeCounter(temp+1);
                        likeCounterText.setText(temp+1+"");
                        likeButton.setText("Unlike");
                        break;
                    }
                }
            }
        }
        else if (eventName.equals("Unlike")){
            boolean result = CRUD.unLikeTable(currentPostId, CRUD.getCurrentPatient().getEmail(), Integer.parseInt(likeCounterText.getText()));
            if (result){
                for (Post post : posts){
                    if (currentPostId==post.getPostID()){
                        int temp = post.getLikeCounter();
                        post.setLikeCounter(temp-1);
                        likeCounterText.setText(temp-1+"");
                        likeButton.setText("Like");
                        break;
                    }
                }
            }
        }
        else if (eventName.equals("Back to Doctors Information Page")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientDoctorsInformationView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
        else if (eventName.equals("Back to Menu")){
            fxmlLoader   = new FXMLLoader(getClass().getResource("../FXML/Patient/patientMenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            scene.setRoot(root);
        }
    }

    private void setAllVisible(){
        titleText.setVisible(true);
        contentText.setVisible(true);
        postOfText.setVisible(true);
        timeText.setVisible(true);
        likeCounterText.setVisible(true);
        likeButton.setVisible(true);
    }
}
