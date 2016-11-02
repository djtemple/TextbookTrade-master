package group12.seng301.textbooktrade;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Josh on 3/9/2016.
 */
public class RegisterActivityTest {

    private RegisterActivity regAct = new RegisterActivity();

    //Test major is valid works
    @Test
    public void majorValidTest() throws Exception {
        boolean eval = regAct.majorValid("Computer Science");
        assertEquals(true, eval);

    }


}