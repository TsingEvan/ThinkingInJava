package tij.annotations;

import tij.annotations.util.SqlType;
import tij.annotations.util.TableColumn;
import tij.annotations.util.UseCase;

import java.util.List;

public class PasswordUtils {

    @UseCase(id=47,description = "Passwords must contain at least one numeric")
    public boolean validatePassword(String password){
        return (password.matches("\\w*\\d\\w*"));
    }

    @UseCase(id=48)
    public String encryptPassword(String password){
        return new StringBuilder(password).reverse().toString();
    }

    @UseCase(id=49,description = "New passwords can't equals previously password")
    public boolean checkForNewPassword(List<String> prevPasswords, String password){
        return !prevPasswords.contains(password);
    }
}
