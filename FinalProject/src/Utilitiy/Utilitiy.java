package Utilitiy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilitiy {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean emailValidation(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean passwordValidation(String password){
        if (password.length() > 5 && !password.contains("password")){
            return true;
        }
        return false;
    }
}
