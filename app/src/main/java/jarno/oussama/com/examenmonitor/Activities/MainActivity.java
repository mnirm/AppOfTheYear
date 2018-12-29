package jarno.oussama.com.examenmonitor.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import jarno.oussama.com.examenmonitor.CameraActivity;
import jarno.oussama.com.examenmonitor.R;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import jarno.oussama.com.examenmonitor.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;

    private View view;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MainFragment()).commit();
        }


        view = findViewById(android.R.id.content);
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {

        } else {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), RC_SIGN_IN);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
       }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            boolean emailVerified = user.isEmailVerified();

            String uid = user.getUid();

            TextView textViewName = findViewById(R.id.textView_name);
            textViewName.setText(name);
            TextView textViewEmail = findViewById(R.id.textView_email);
            textViewEmail.setText(email);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                //FirebaseAuth.getInstance().signOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //user signed in
                Snackbar.make(view, "user signed in" , Snackbar.LENGTH_LONG);
            } else {
                // user not authenticated
                Log.i("auth", "not authenticated");
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }


    public void AddUserActivity(View view) {
        Intent IntentAdduser = new Intent(this, AddUserActivity.class);
        startActivity(IntentAdduser);
    }

    public void StudentsListActivity(View view) {
        Intent StudentList = new Intent(this, StudentsListActivity.class);
        startActivity(StudentList);
    }

    public void NewExamActivity(View view) {
        Intent newExam = new Intent(this, NewExamActivity.class);
        startActivity(newExam);
    }

    public void MyExamsActivity(View view) {
        Intent MyExams = new Intent(this, MyExamsActivity.class);
        startActivity(MyExams);
    }
}
