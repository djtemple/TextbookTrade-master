package group12.seng301.textbooktrade;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Josh on 3/9/2016.
 */
public class LoginActivityTest {

    private LoginActivity logAct = new LoginActivity();

    //Test that the email belongs to @ucalgary.ca
    @Test
    public void checkUofCEmail() {
        boolean email = logAct.isEmailValid("test@ucalgary.ca");
        assertEquals(true, email);
    }

    //Test that password length is long enough
    @Test
    public void checkPasswordLength() {
        boolean password = logAct.isPasswordValid("qwerty");
        assertEquals(true, password);
    }

}