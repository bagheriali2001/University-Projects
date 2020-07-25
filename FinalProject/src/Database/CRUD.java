package Database;

import java.sql.*;
import java.util.ArrayList;
import Class.*;

public class CRUD { //Create - Read - Update - Delete
    private static Doctor currentDoctor ;
    private static Patient currentPatient ;
    public static String chatForce;
    public static String informationForce;

    public static Doctor getCurrentDoctor() {
        return currentDoctor;
    }
    public static Patient getCurrentPatient() {
        return currentPatient;
    }

    private static Statement statement;
    static {
        try {
            statement = DatabaseConnection.getConnection(new String[]{
                    "jdbc:postgresql://127.0.0.1:5432/Project",
                    "postgres",
                    "root"
            }).createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean createPatientUser(String firstName , String lastName
            , String email , String password
            , Date birthday , String gender
            , String weight , String height, String avatarIMGPath) throws SQLException {
        try {
            int res = statement.executeUpdate(
                    "INSERT INTO public.\"Patient\"(\n" +
                            "\t\"firstName\", \"lastName\", email, password, birthday, gender, weight, \"height\", \"avatarIMGPath\")\n" +
                            "\tVALUES ('"+firstName+"', '"+lastName+"', '"+email+"', '"+password+"', '"+birthday+"', '"+gender+"', '"+weight+"', '"+height+"', '"+avatarIMGPath+"');");
            if (res==1){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }

    }

    public static boolean createDocterUser(String firstName , String lastName
            , String email , String password
            , Date birthday , String gender
            , String speciality , String medicalLicenseNumber
            , String avatarIMGPath, String licenseIMGPath) throws SQLException {
        try {
            int res = statement.executeUpdate(
                    "INSERT INTO public.\"Doctors\"(\n" +
                            "\t\"firstName\", \"lastName\", email, password, birthday, gender, speciality, \"medicalLicenseNumber\", \"avatarIMGPath\", \"licenseIMGPath\")\n" +
                            "\tVALUES ('"+firstName+"', '"+lastName+"', '"+email+"', '"+password+"', '"+birthday+"', '"+gender+"', '"+speciality+"', '"+medicalLicenseNumber+"', '"+avatarIMGPath+"', '"+licenseIMGPath+"');");
            if (res==1){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }

    }

    public static boolean resetPassword(String firstName, String lastName, String email, String newPassword) throws SQLException {   String sqlQuery[] = new String[2] ;
        sqlQuery[0] = "UPDATE public.\"Doctors\"\n" +
                "\tSET password=? \n" +
                "\tWHERE \"firstName\" =  ? AND \"lastName\" = ? AND \"email\" = ? ;";
        sqlQuery[1] = "UPDATE public.\"Doctors\"\n" +
                "\tSET password=? \n" +
                "\tWHERE \"firstName\" =  ? AND \"lastName\" = ? AND \"email\" = ? ;";
        for (int i = 0; i < 2; i++) {
            PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery[i]);
            preStatementDoc.setString(1,newPassword);
            preStatementDoc.setString(2,firstName);
            preStatementDoc.setString(3,lastName);
            preStatementDoc.setString(4,email);
            int res = preStatementDoc.executeUpdate();
            if (res==1){
                return true;
            }
        }
        return false;
    }

    public static int login(String email, String password) throws SQLException {
        String sqlQuery[] = new String[2] ;
        sqlQuery[0] = "SELECT *  FROM public.\"Doctors\" WHERE  \"email\" =  ? AND \"password\" = ? ;";
        sqlQuery[1] = "SELECT *  FROM public.\"Patient\" WHERE  \"email\" =  ? AND \"password\" = ? ;";
        for (int i = 0; i < 2; i++) {
            PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery[i]);
            preStatementDoc.setString(1,email);
            preStatementDoc.setString(2,password);
            ResultSet res = preStatementDoc.executeQuery();
            if (res.next()){
                if (i==0){
                    currentDoctor= new Doctor(res.getString("firstName"), res.getString("lastName")
                            , res.getString("email"), res.getString("password")
                            , res.getDate("birthday").toLocalDate(), res.getString("gender")
                            , res.getString("speciality"), res.getString("medicalLicenseNumber")
                            , res.getString("information"), res.getString("avatarIMGPath")
                            , res.getString("licenseIMGPath"));
                }else{
                    currentPatient= new Patient(res.getString("firstName"), res.getString("lastName")
                            , res.getString("email"), res.getString("password")
                            , res.getDate("birthday").toLocalDate(), res.getString("gender")
                            , res.getString("weight"), res.getString("height")
                            , res.getString("avatarIMGPath"));
                }
                return i+1;
            }
        }
        return 0;
    }

    public static boolean editProfile(String userType, String searchKey
            , String firstName , String lastName
            , String email , String password
            , Date birthday , String gender
            , String weight_speciality , String height_licenseNo
            , String avatarIMGPath , String licenseIMGPath ) throws SQLException {
        String sqlQuery ;
        if (userType.equals("Doctor")){
            sqlQuery = "UPDATE public.\"Doctors\"\n" +
                    "\tSET \"firstName\"=?, \"lastName\"=?, email=?, password=?, birthday=?, gender=?, speciality=?, \"medicalLicenseNumber\"=?, \"avatarIMGPath\"=?, \"licenseIMGPath\"=?\n" +
                    "\tWHERE \"email\" = ? ;";
        }else {
            sqlQuery = "UPDATE public.\"Patient\"\n" +
                    "\tSET \"firstName\"=?, \"lastName\"=?, email=?, password=?, birthday=?, gender=?, weight=?, height=?, \"avatarIMGPath\"=?\n" +
                    "\tWHERE \"email\" = ? ;";
        }
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,firstName);
        preStatementDoc.setString(2,lastName);
        preStatementDoc.setString(3,email);
        preStatementDoc.setString(4,password);
        preStatementDoc.setDate(5,birthday);
        preStatementDoc.setString(6,gender);
        if (userType.equals("Doctor")){
            preStatementDoc.setString(7,weight_speciality);
            preStatementDoc.setString(8,height_licenseNo);
            preStatementDoc.setString(9,avatarIMGPath);
            preStatementDoc.setString(10,licenseIMGPath);
            preStatementDoc.setString(11,searchKey);
        }else {
            preStatementDoc.setDouble(7,Double.parseDouble(weight_speciality));
            preStatementDoc.setDouble(8,Double.parseDouble(height_licenseNo));
            preStatementDoc.setString(9,avatarIMGPath);
            preStatementDoc.setString(10,searchKey);
        }
        int res = preStatementDoc.executeUpdate();
        if (res==1){
            return true;
        }
        else {
            return false;
        }
    }

    public static ArrayList<Doctor> doctorsList() throws SQLException {
        String sqlQuery = "SELECT \"firstName\", \"lastName\", email, speciality, information\n" +
                "\tFROM public.\"Doctors\";";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Doctor> doctors = new ArrayList<Doctor>();
        while (res.next()){
            Doctor doctor = new Doctor(res.getString("firstName"), res.getString("lastName")
                    , res.getString("email"), res.getString("speciality"), res.getString("information"));
            doctors.add(doctor);
        }
        return doctors;
    }

    public static ArrayList<Patient> patientsList() throws SQLException {
        String sqlQuery = "SELECT \"firstName\", \"lastName\", email\n" +
                "\tFROM public.\"Patient\";";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Patient> patients = new ArrayList<Patient>();
        while (res.next()) {
            Patient patient = new Patient(res.getString("firstName"), res.getString("lastName")
                    , res.getString("email"));
            patients.add(patient);
        }
        return patients;
    }

    public static ArrayList<Doctor> doctorsListWithPath() throws SQLException {
        String sqlQuery = "SELECT \"firstName\", \"lastName\", email, speciality, \"medicalLicenseNumber\"" +
                ", information, \"avatarIMGPath\", \"licenseIMGPath\"\n" +
                "\tFROM public.\"Doctors\";";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Doctor> doctors = new ArrayList<Doctor>();
        while (res.next()){
            Doctor doctor = new Doctor(res.getString("firstName"), res.getString("lastName")
                    , res.getString("email"), res.getString("speciality")
                    , res.getString("medicalLicenseNumber"), res.getString("information")
                    , res.getString("avatarIMGPath"), res.getString("licenseIMGPath"));
            doctors.add(doctor);
        }
        return doctors;
    }

    public static ArrayList<Patient> patientsFullList() throws SQLException {
        String sqlQuery = "SELECT \"firstName\", \"lastName\", email, password, birthday, gender, weight, height, \"avatarIMGPath\"\n" +
                "\tFROM public.\"Patient\";";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Patient> patients = new ArrayList<Patient>();
        while (res.next()) {
            Patient patient = new Patient(res.getString("firstName"), res.getString("lastName")
                    , res.getString("email"), res.getString("password")
                    , res.getDate("birthday").toLocalDate(), res.getString("gender")
                    , res.getString("weight"), res.getString("height")
                    , res.getString("avatarIMGPath"));
            patients.add(patient);
        }
        return patients;
    }

    public static boolean SendAppointmentRequest(String from, String to
            , String content, Date when, Date timeRequested, String filePath) throws SQLException {
        String sqlQuery ;
        sqlQuery = "INSERT INTO public.\"Appointments\"(\n" +
                "\t \"from\", \"to\", content, \"when\", \"timeRequested\", acceptation, \"filePath\")\n" +
                "\tVALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,from);
        preStatementDoc.setString(2,to);
        preStatementDoc.setString(3,content);
        preStatementDoc.setDate(4,when);
        preStatementDoc.setDate(5,timeRequested);
        preStatementDoc.setInt(6,0);
        preStatementDoc.setString(7,filePath);
        int res = preStatementDoc.executeUpdate();
        if (res==1)
            return true;
        else
            return false;
    }

    public static ArrayList<Appointment> appointmentList(String email, String userType) throws SQLException {
        String sqlQuery = null;
        if (userType.equals("Doctor")){
            sqlQuery = "SELECT \"appointmentsID\", \"from\", \"to\", content, \"when\", \"timeRequested\", acceptation, \"doctorResponse\", \"filePath\"\n" +
                    "\tFROM public.\"Appointments\" " +
                    "WHERE \"to\" = ? ;";
        }
        else if (userType.equals("Patient")){
            sqlQuery = "SELECT \"appointmentsID\", \"from\", \"to\", content, \"when\", \"timeRequested\", acceptation, \"doctorResponse\", \"filePath\"\n" +
                    "\tFROM public.\"Appointments\" " +
                    "WHERE \"from\" = ? ;";
        }
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,email);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        while (res.next()){
            Appointment appointment = new Appointment(res.getInt("appointmentsID") ,res.getString("from")
                    ,res.getString("to") ,res.getString("content")
                    ,res.getDate("when").toLocalDate() ,res.getDate("timeRequested").toLocalDate()
                    ,res.getInt("acceptation") ,res.getString("doctorResponse")
                    ,res.getString("filePath"));
            appointments.add(appointment);
        }
        return appointments;
    }

    public static boolean updateAppointment(String appointmentID, String doctorResponse, int acception) throws SQLException {
        String sqlQuery = "UPDATE public.\"Appointments\"\n" +
                "\tSET acceptation=?, \"doctorResponse\"=?\n" +
                "\tWHERE \"appointmentsID\"=?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,acception);
        preStatementDoc.setString(2,doctorResponse);
        preStatementDoc.setInt(3,Integer.parseInt(appointmentID));
        int res = preStatementDoc.executeUpdate();
        if (res==1){
           return true;
        }
        else {
            return false;
        }
    }

    public static ArrayList<Massage> massageList(String email) throws SQLException {
        String sqlQuery = null;
        sqlQuery = "SELECT \"massageID\", \"from\", \"to\", \"time\", content\n" +
                "\tFROM public.\"Messages\"\n" +
                "\tWHERE \"from\" = ? OR \"to\" = ?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,email);
        preStatementDoc.setString(2,email);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Massage> massages = new ArrayList<Massage>();
        while (res.next()){
            Massage massage = new Massage(res.getInt("massageID") ,res.getString("from")
                    ,res.getString("to") ,res.getDate("time").toLocalDate()
                    ,res.getString("content"));
            massages.add(massage);
        }
        return massages;
    }

    public static boolean sendMassage(String from ,String to ,Date time ,String content) throws SQLException {
        System.out.println("Sending");
        String sqlQuery ;
        sqlQuery = "INSERT INTO public.\"Messages\"(\n" +
                "\t\"from\", \"to\", \"time\", content)\n" +
                "\tVALUES (?, ?, ?, ?);";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,from);
        preStatementDoc.setString(2,to);
        preStatementDoc.setDate(3,time);
        preStatementDoc.setString(4,content);
        int res = preStatementDoc.executeUpdate();
        if (res==1)
            return true;
        else
            return false;
    }

    public static boolean updateInformation(String information, String email) throws SQLException {
        String sqlQuery ;
        sqlQuery = "UPDATE public.\"Doctors\"\n" +
                "\tSET Information=?\n" +
                "\tWHERE \"email\" = ? ;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,information);
        preStatementDoc.setString(2,email);
        int res = preStatementDoc.executeUpdate();
        if (res==1){
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean uploadPost(String postOf, String content, Date timePosted, String title) throws SQLException {
        String sqlQuery ;
        sqlQuery = "INSERT INTO public.\"Post\"(\n" +
                "\t\"postOf\", content, \"timePosted\", title)\n" +
                "\tVALUES (?, ?, ?, ?);";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,postOf);
        preStatementDoc.setString(2,content);
        preStatementDoc.setDate(3,timePosted);
        preStatementDoc.setString(4,title);
        int res = preStatementDoc.executeUpdate();
        if (res==1)
            return true;
        else
            return false;
    }

    public static ArrayList<Post> postList() throws SQLException {
        String sqlQuery ;
        sqlQuery = "SELECT \"postID\", \"postOf\", content, \"timePosted\", title, \"likeCounter\"\n" +
                "\tFROM public.\"Post\";";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Post> posts = new ArrayList<Post>();
        while (res.next()){
            Post post = new Post(res.getInt("postID"), res.getString("postOf")
                    , res.getString("title"), res.getString("content")
                    , res.getDate("timePosted").toLocalDate(), res.getInt("likeCounter"));
            posts.add(post);
        }
        return posts;
    }

    public static boolean likeTable(int postID, String liker, int likes) throws SQLException {
        String sqlQuery = "INSERT INTO public.likes(\n" +
                "\t\"postID\", liker)\n" +
                "\tVALUES (?, ?);";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,postID);
        preStatementDoc.setString(2,liker);
        int res = preStatementDoc.executeUpdate();
        if (res==1 && likeCounterUpdate(postID, likes+1)){
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean unLikeTable(int postID, String liker, int likes) throws SQLException {
        String sqlQuery = "DELETE FROM public.likes\n" +
                "\tWHERE \"postID\" = ? AND liker = ?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,postID);
        preStatementDoc.setString(2,liker);
        int res = preStatementDoc.executeUpdate();
        if (res==1 && likeCounterUpdate(postID, likes-1)){
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean likeCounterUpdate(int postID, int likes) throws SQLException {
        String sqlQuery = "UPDATE public.\"Post\"\n" +
                "\tSET \"likeCounter\"=?\n" +
                "\tWHERE \"postID\"=?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,likes);
        preStatementDoc.setInt(2,postID);
        int res = preStatementDoc.executeUpdate();
        if (res==1){
            return true;
        }
        else {
            return false;
        }
    }

    public static ArrayList<String> likeList(int postID) throws SQLException {
        String sqlQuery ;
        sqlQuery = "SELECT liker\n" +
                "\tFROM public.likes\n" +
                "\tWHERE \"postID\"=?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,postID);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<String> likers = new ArrayList<String>();
        while (res.next()){
            likers.add(res.getString("liker"));
        }
        return likers;
    }

    public static boolean newAlarm(int hour, int minute, int delayHour, String title, String alarmOf) throws SQLException {
        String sqlQuery ;
        sqlQuery = "INSERT INTO public.\"Alarm\"(\n" +
                "\t hour, minute, \"delayHour\", title, \"alarmOf\")\n" +
                "\tVALUES (?, ?, ?, ?, ?);";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,hour);
        preStatementDoc.setInt(2,minute);
        preStatementDoc.setInt(3,delayHour);
        preStatementDoc.setString(4,title);
        preStatementDoc.setString(5,alarmOf);
        int res = preStatementDoc.executeUpdate();
        if (res==1)
            return true;
        else
            return false;
    }

    public static ArrayList<Alarm> alarmList(String alarmOf) throws SQLException {
        String sqlQuery ;
        sqlQuery = "SELECT \"alarmID\", hour, minute, \"delayHour\", title, \"alarmOf\", \"isUsed\"\n" +
                "\tFROM public.\"Alarm\"\n" +
                "\tWHERE \"alarmOf\"=?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,alarmOf);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Alarm> alarms = new ArrayList<Alarm>();
        while (res.next()){
            Alarm alarm = new Alarm(res.getInt("alarmID"), res.getInt("hour")
                    , res.getInt("minute"), res.getInt("delayHour")
                    , res.getString("title"), res.getString("alarmOf")
                    ,res.getBoolean("isUsed"));
            alarms.add(alarm);
        }
        return alarms;
    }

    public static boolean updateAlarm(int alarmID, int hour, int minute, int delayHour, String title) throws SQLException {
        String sqlQuery ;
        sqlQuery = "UPDATE public.\"Alarm\"\n" +
                "\tSET hour=?, minute=?, \"delayHour\"=?, title=?\n, \"isUsed\"= true" +
                "\tWHERE  \"alarmID\"=?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,hour);
        preStatementDoc.setInt(2,minute);
        preStatementDoc.setInt(3,delayHour);
        preStatementDoc.setString(4,title);
        preStatementDoc.setInt(5,alarmID);
        int res = preStatementDoc.executeUpdate();
        if (res==1){
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean notUsedAlarm(int alarmID) throws SQLException {
        String sqlQuery ;
        sqlQuery = "UPDATE public.\"Alarm\"\n" +
                "\tSET \"isUsed\"= false " +
                "\tWHERE  \"alarmID\"=?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,alarmID);
        int res = preStatementDoc.executeUpdate();
        if (res==1){
            return true;
        }
        else {
            return false;
        }
    }

    public static ArrayList<Appointment> acceptedAppointmentList(String email, String userType) throws SQLException {
        String sqlQuery = null;
        if (userType.equals("Doctor")){
            sqlQuery = "SELECT \"appointmentsID\", \"from\", \"to\", content, \"when\", \"timeRequested\", acceptation, \"doctorResponse\"\n" +
                    "\tFROM public.\"Appointments\" " +
                    "WHERE \"to\" = ? AND acceptation = ?;";
        }
        else if (userType.equals("Patient")){
            sqlQuery = "SELECT \"appointmentsID\", \"from\", \"to\", content, \"when\", \"timeRequested\", acceptation, \"doctorResponse\"\n" +
                    "\tFROM public.\"Appointments\" " +
                    "WHERE \"from\" = ? AND acceptation = ?;";
        }
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setString(1,email);
        preStatementDoc.setInt(2,1);
        ResultSet res = preStatementDoc.executeQuery();
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        while (res.next()){
            Appointment appointment = new Appointment(res.getInt("appointmentsID") ,res.getString("from")
                    ,res.getString("to") ,res.getString("content")
                    ,res.getDate("when").toLocalDate() ,res.getDate("timeRequested").toLocalDate()
                    ,res.getInt("acceptation") ,res.getString("doctorResponse"));
            appointments.add(appointment);
        }
        return appointments;
    }

    public static boolean deleteAlarm(int alarmID) throws SQLException {
        String sqlQuery ;
        sqlQuery = "DELETE FROM public.\"Alarm\"\n" +
                "\tWHERE \"alarmID\" = ?;";
        PreparedStatement preStatementDoc = DatabaseConnection.getConnection(null).prepareStatement(sqlQuery);
        preStatementDoc.setInt(1,alarmID);
        int res = preStatementDoc.executeUpdate();
        if (res==1){
            return true;
        }
        else {
            return false;
        }
    }
}
