package Class;

import java.time.LocalDate;
import java.util.Date;

public class Patient {
    private String firstName ;
    private String lastName ;
    private String email ;
    private String password ;
    private LocalDate birthday ;
    private String gender ;
    private String weight ;
    private String height ;
    private String avatarIMGPath ;

    public Patient(String firstName, String lastName, String email, String password
            , LocalDate birthday, String gender, String weight, String height, String avatarIMGPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.avatarIMGPath = avatarIMGPath;
    }

    public Patient(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getAvatarIMGPath() {
        return avatarIMGPath;
    }

    public void empty(){
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.password = null;
        this.birthday = null;
        this.gender = null;
        this.weight = null;
        this.height = null;
    }
}
