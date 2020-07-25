package Class;

import java.time.LocalDate;
import java.util.Date;

public class Doctor {
    private String firstName ;
    private String lastName ;
    private String email ;
    private String password ;
    private LocalDate birthday ;
    private String gender ;
    private String speciality ;
    private String medicalLicenseNumber ;
    private String information;
    private String avatarIMGPath ;
    private String licenseIMGPath;

    public Doctor(String firstName, String lastName, String email, String password
            , LocalDate birthday, String gender, String speciality, String medicalLicenseNumber
            , String information, String avatarIMGPath, String licenseIMGPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.speciality = speciality;
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.information=information;
        this.avatarIMGPath = avatarIMGPath;
        this.licenseIMGPath=licenseIMGPath;
    }

    public Doctor(String firstName, String lastName, String email, String speciality, String information){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.speciality = speciality;
        this.information=information;
    }

    public Doctor(String firstName, String lastName, String email, String speciality, String medicalLicenseNumber
            , String information, String avatarIMGPath, String licenseIMGPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.speciality = speciality;
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.information=information;
        this.avatarIMGPath = avatarIMGPath;
        this.licenseIMGPath=licenseIMGPath;
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

    public String getSpeciality() {
        return speciality;
    }

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getAvatarIMGPath() {
        return avatarIMGPath;
    }

    public String getLicenseIMGPath() {
        return licenseIMGPath;
    }

    public void empty(){
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.password = null;
        this.birthday = null;
        this.gender = null;
        this.speciality = null;
        this.medicalLicenseNumber = null;
        this.information = null;
    }
}
