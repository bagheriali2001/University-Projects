package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance = null;
    private static String URL;
    private static final Properties props = new Properties();
    private static Connection connection = null;

    private DatabaseConnection(String URLInput, String userInput, String passwordInput){
        props.setProperty("user", userInput);
        props.setProperty("password", passwordInput);
        props.setProperty("useUnicode", "true");
        props.setProperty("characterEncoding", "utf-8");
        URL = URLInput;
    }

    public static Connection getConnection(String[] input){
        if (instance == null){
            instance = new DatabaseConnection(input[0], input[1], input[2]);
            //Load the driver class
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Unable to load the class. Terminating the program");
                System.exit(-1);
            }
            //get the connection
            try {
                connection = DriverManager.getConnection(URL, props);
            } catch (SQLException ex) {
                System.out.println("Error getting connection: " + ex.getMessage());
                System.exit(-1);
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                System.exit(-1);
            }
        }
        return connection;
    }
}
