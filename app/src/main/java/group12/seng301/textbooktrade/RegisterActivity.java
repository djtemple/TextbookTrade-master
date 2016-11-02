package group12.seng301.textbooktrade;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import group12.seng301.textbooktrade.objects.User;

public class RegisterActivity extends AppCompatActivity {

    /**
     * Required input text fields
     */
    private TextView cfmPwdView;
    private TextView nameView;
    private AutoCompleteTextView majorView;
    private View registerForm, progressBar;
    private RegisterTask registerTask = null;
    protected boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        // Put back but on action bar.
   //     ActionBar actionBar = this.getActionBar();
   //     actionBar.setHomeButtonEnabled(true);


        // Set up Autocomplete Text View
        majorView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_major);
        String[] majors = new String[Major.values().length];

        int i = 0; // Counter
        for (Major major : Major.values()) {
            majors[i] = major.alais;
            i++;
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, majors);

        majorView.setAdapter(adapter);


        // Get the email and password of user from previous activity.
        Intent intent = getIntent();
        final String pwd = intent.getStringExtra("group12.seng301.LoginActivity.PASSWORD");
        final String email = intent.getStringExtra("group12.seng301.LoginActivity.EMAIL");

        // Initialize views
        cfmPwdView = (EditText) findViewById(R.id.confirm_pwd);
        nameView = (EditText) findViewById(R.id.name);
        registerForm = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progressBar);

        // Attempt to register user when button pressed.
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister(pwd, email);
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Get current input and check the validity of it. If the attempt meets basic criteria
     * a Register Task will be made, otherwise the user must try again.
     * @param password
     */
    protected void attemptRegister(String password, String email) {

        if (registerTask != null)
            return;

        success = true;
        View focusview = null;

        // Get the input at time of submission
        final String cfmPwd = cfmPwdView.getText().toString();
        final String name = nameView.getText().toString();
        final String major = majorView.getText().toString();

        // Check if user's major exists.
        if (!majorValid(major)) {
            majorView.setError(major + " is not a valid major");
            focusview = majorView;
            success = false;
        }

        // Check if user's name is valid.
        if (!nameValid(name)) {
            nameView.setError("Name not valid");
            focusview = nameView;
            success = false;
        }

        // Check if password matches.
        if (!cfmPwd.equalsIgnoreCase(password)) {
            cfmPwdView.setError("Password does not match");
            focusview = cfmPwdView;
            success = false;
        }

        // Check if everything was successful.
        if (success) {
            loadingScreen(true);
            registerTask = new RegisterTask(email, cfmPwd, name, major, this);
            registerTask.execute((Void) null);

        } else {
            // Some error was detected and the user should be prompt to fix the issue.
            focusview.requestFocus();
        }

    }

    /**
     * Check name for validity
     * @param nameToCheck
     */
    private boolean nameValid(String nameToCheck) {
        // Maybe put some criteria here??
        return true;
    }

    /**
     * Changes the view to show the user they are waiting for the app to register
     * their account.
     * @param visable True if the loadingScreen is visable
     */
    private void loadingScreen(final boolean visable) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerForm.setVisibility(visable ? View.GONE : View.VISIBLE);
            registerForm.animate().setDuration(shortAnimTime).alpha(
                    visable ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerForm.setVisibility(visable ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(visable ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    visable ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(visable ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(visable ? View.VISIBLE : View.GONE);
            registerForm.setVisibility(visable ? View.GONE : View.VISIBLE);

        }
    }

    /**
     * Async task to register the user.
     */
    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private String email, pwd, name, major;
        private final AppCompatActivity activity;
        private long userId;

        public RegisterTask(String email, String pwd, String name, String major, AppCompatActivity activity) {
            this.email = email;
            this.pwd = pwd;
            this.name = name;
            this.major = major;
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            User newUser = new User(email, name, Major.valueFor(major), pwd);
            TextbooksDatabaseHelper databaseHelper = TextbooksDatabaseHelper.getInstance(activity);
            userId = databaseHelper.addOrUpdateUser(newUser);
            if (userId == -1) return false;

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            registerTask = null;
            loadingScreen(false);

            if (success) {
                Intent intent = new Intent(activity, BookListActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                startActivity(intent);
                activity.finish();
            } else {
                loadingScreen(false);
                Log.d(this.getClass().getName(), "Error registering user");
            }

        }
    }

    /**
     * Check to see if major is legit.
     * @param majorToCheck
     * @return
     */
    protected boolean majorValid(String majorToCheck) {
        for (Major major : Major.values()) {
            if (major.alais.equalsIgnoreCase(majorToCheck))
                return true;
        }
        return false;
    }


   public enum Major {

       ANCIENT_AND_MEDIEVAL_HISTORY ("Ancient and Medieval History"),
       ANTHROPOLOGY_SOCIAL_AND_CULTURAL ("Anthropology, Social and Cultural"),
       ANTHROPOLOGY ("Anthropology"),
       ARCHAEOLOGY ("Archaeology"),
       CANADIAN_STUDIES ("Canadian Studies"),
       COMMUNICATION_AND_CULTURE ("Communication and Culture"),
       COMMUNICATION_AND_MEDIA_STUDIES ("Communication and Media Studies"),
       DEVELOPMENT_STUDIES ("Development Studies"),
       EARTH_SCIENCE ("Earth Science"),
       EAST_ASIAN_LANGUAGE_STUDIES ("East Asian Language Studies"),
       EAST_ASIAN_STUDIES ("East Asian Studies"),
       ECONOMICS ("Economics"),
       ENGLISH ("English"),
       FILM_STUDIES ("Film Studies"),
       FRENCH ("French"),
       GEOGRAPHY ("Geography"),
       GERMAN ("German"),
       GREEK_AND_ROMAN_STUDIES ("Greek and Roman Studies"),
       HISTORY ("History"),
       HISTORY_AND_PHILOSOPHY_OF_SCIENCE ("History and Philosophy of Science"),
       INTERNATIONAL_INDIGENOUS_STUDIES ("International Indigenous Studies"),
       INTERNATIONAL_RELATIONS ("International Relations"),
       ITALIAN_STUDIES ("Italian Studies"),
       LATIN_AMERICAN_STUDIES ("Latin American Studies"),
       LAW_AND_SOCIETY ("Law and Society"),
       LINGUISTICS ("Linguistics"),
       LINGUISTICS_AND_LANGUAGE ("Linguistics and Language"),
       PHILOSOPHY ("Philosophy"),
       POLITICAL_SCIENCE ("Political Science"),
       PSYCHOLOGY ("Psychology"),
       RELIGIOUS_STUDIES ("Religious Studies"),
       RELIGIOUS_STUDIES_AND_APPLIED_ETHICS ("Religious Studies and Applied Ethics"),
       RUSSIAN ("Russian"),
       SCIENCE_TECHNOLOGY_AND_SOCIETY ("Science, Technology and Society"),
       SOCIOLOGY ("Sociology"),
       SPANISH ("Spanish"),
       URBAN_STUDIES ("Urban Studies"),
       WOMENS_STUDIES ("Women’s Studies"),
       COMMUNITY_REHABILITATION ("Community Rehabilitation"),
       BIOINFORMATICS ("Bioinformatics"),
       BIOMEDICAL_SCIENCES ("Biomedical Sciences"),
       HEALTH_AND_SOCIETY ("Health and Society"),
       DOCTOR_OF_MEDICINE ("Doctor of Medicine"),
       ART_HISTORY ("Art History"),
       DANCE ("Dance"),
       DRAMA ("Drama"),
       VISUAL_STUDIES ("Visual Studies"),
       MUSIC ("Music"),
       COMMERCE ("Commerce"),
       HOTEL_AND_RESORT_MANAGEMENT ("Hotel and Resort Management"),
       ATHLETIC_THERAPY ("Athletic Therapy"),
       LEADERSHIP_IN_PEDAGOGY_AND_COACHING ("Leadership in Pedagogy and Coaching"),
       BIOMECHANICS ("Biomechanics"),
       EXERCISE_AND_HEALTH_PHYSIOLOGY ("Exercise and Health Physiology"),
       KINESIOLOGY ("Kinesiology"),
       MIND_SCIENCES_IN_KINESIOLOGY ("Mind Sciences in Kinesiology"),
       LAW ("Law"),
       NURSING ("Nursing"),
       CHEMICAL_ENGINEERING ("Chemical Engineering"),
       CIVIL_ENGINEERING ("Civil Engineering"),
       ELECTRICAL_ENGINEERING ("Electrical Engineering"),
       ENERGY_ENGINEERING ("Energy Engineering"),
       GEOMATICS_ENGINEERING ("Geomatics Engineering"),
       MECHANICAL_ENGINEERING ("Mechanical Engineering"),
       OIL_AND_GAS_ENGINEERING ("Oil and Gas Engineering"),
       SOFTWARE_ENGINEERING ("Software Engineering"),
       ACTUARIAL_SCIENCE ("Actuarial Science"),
       APPLIED_AND_ENVIRONMENTAL_GEOLOGY ("Applied and Environmental Geology"),
       APPLIED_CHEMISTRY ("Applied Chemistry"),
       APPLIED_MATHEMATICS ("Applied Mathematics"),
       ASTROPHYSICS ("Astrophysics"),
       BIOCHEMISTRY ("Biochemistry"),
       BIOLOGICAL_SCIENCES ("Biological Sciences"),
       CELLULAR_MOLECULAR_AND_MICROBIAL_BIOLOGY ("Cellular, Molecular and Microbial Biology"),
       CHEMICAL_PHYSICS ("Chemical Physics"),
       CHEMISTRY ("Chemistry"),
       COMPUTER_SCIENCE ("Computer Science"),
       ECOLOGY ("Ecology"),
       ENVIRONMENTAL_SCIENCE ("Environmental Science"),
       GENERAL_MATHEMATICS ("General Mathematics"),
       GEOLOGY ("Geology"),
       GEOPHYSICS ("Geophysics"),
       NATURAL_SCIENCES ("Natural Sciences"),
       NEUROSCIENCE ("Neuroscience"),
       PHYSICS ("Physics"),
       PLANT_BIOLOGY ("Plant Biology"),
       PURE_MATHEMATICS ("Pure Mathematics"),
       STATISTICS ("Statistics"),
       ZOOLOGY ("Zoology"),
       SOCIAL_WORK ("Social Work"),
       DOCTOR_OF_VETERINARY_MEDICINE ("Doctor of Veterinary Medicine"),
       EDUCATION ("Education");


       private final String alais;

       Major(String alais) {
            this.alais = alais;
        }

       public static Major valueFor(String name) {
           for (Major major : Major.values()) {
               if (major.alais.equalsIgnoreCase(name))
                   return major;
           }
           return null;
       }

    }


}
