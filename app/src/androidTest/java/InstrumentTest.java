package group12.seng301.textbooktrade;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class InstrumentTest extends ActivityInstrumentationTestCase2<LoginActivity>{
    public InstrumentTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    @SmallTest
    public void testbutton() {
        Button bt = (Button)getActivity().findViewById(R.id.email_sign_in_button);
        assertNotNull(bt);
    }

    @SmallTest
    public void testemailtext() {
        AutoCompleteTextView actv = (AutoCompleteTextView)getActivity().findViewById(R.id.email);
        assertNotNull(actv);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}