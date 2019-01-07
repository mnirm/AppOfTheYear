package jarno.oussama.com.examenmonitor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import jarno.oussama.com.examenmonitor.CameraActivity;
import jarno.oussama.com.examenmonitor.FirebaseDB.Student;
import jarno.oussama.com.examenmonitor.Nfc.NFC;
import jarno.oussama.com.examenmonitor.R;


public class AddUserActivity extends AppCompatActivity {
    TextView nfcStatusTextView;
    EditText nameEditText, studentNumberEditText, lastNameEditText;
    ImageView imageView;
    String name, lastName,pictureDownloadUrl;
    NFC nfc;
    View view;
    Button addButton, cameraButton;
    DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("students");
    String PhotoPath;
    Bitmap photo;
    int studentNumber;
    boolean cameraButtonIsEnabled, addUserButtonIsEnabled;
    private static final int Camera_Intent_Result_Code = 5;
    private static final int Picture_PREVIEW_Result_Code = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        nfcStatusTextView = findViewById(R.id.textViewNfcStatus);
        nameEditText = findViewById(R.id.editTextSurname);
        lastNameEditText = findViewById(R.id.editTextLastname);
        studentNumberEditText = findViewById(R.id.editTextSNummer);
        addButton = findViewById(R.id.buttonAddUser);
        cameraButton = findViewById(R.id.buttonCamera);
        view = findViewById(android.R.id.content);
        imageView = findViewById(R.id.imageViewStudentPhoto);
        nfc = new NFC(this, this, view);
        if (savedInstanceState != null) {
            nfc.nfcID = savedInstanceState.getString("NFC_ID");
            nfcStatusTextView.setText(nfc.nfcID);
            cameraButton.setEnabled(savedInstanceState.getBoolean("CAMERA_BUTTON_STATUS"));
            addButton.setEnabled(savedInstanceState.getBoolean("ADDUSER_BUTTON_STATUS"));
            InitializeImageView( savedInstanceState.getString("PHOTO_PATH"));
        }
        studentNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    cameraButtonIsEnabled = false;
                    cameraButton.setEnabled(false);
                } else {
                    cameraButton.setEnabled(true);
                    cameraButtonIsEnabled = true;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfc.enableForeGroundDispatch();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("ADDUSER_BUTTON_STATUS", addUserButtonIsEnabled);
        outState.putBoolean("CAMERA_BUTTON_STATUS", cameraButtonIsEnabled);
        if (nfc != null) {
            outState.putString("NFC_ID", nfc.nfcID);
        }
        if(PhotoPath != null){
            outState.putString("PHOTO_PATH", PhotoPath);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfc.setResolveIntent(intent, (boolean isValid) -> {
            if (isValid) {
                addUserButtonIsEnabled = true;
                addButton.setEnabled(true);
                nfcStatusTextView.setText(nfc.nfcID);
            } else {
                addUserButtonIsEnabled = false;
                Snackbar.make(view, "ongeldige kaart", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void AddUserToDB(View view) {
        InitializeStudent();
        Student student = new Student();
        if (validateStudent()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] data = baos.toByteArray();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Student_profile_pictures").child(studentNumber + ".jpg");
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(exception -> Snackbar.make(view, "Het uploaden van de foto is mislukt", Snackbar.LENGTH_SHORT).show()).addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    pictureDownloadUrl = uri.toString();
                    student.setFirstName(name);
                    student.setLastName(lastName);
                    student.setStudentNumber(studentNumber);
                    student.setCardIdNumber(nfc.nfcID);
                    student.setProfilePictureUrl(pictureDownloadUrl);
                    studentsRef.child(student.getCardIdNumber()).setValue(student);
                });
            });
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Camera_Intent_Result_Code) {
            if (resultCode == Activity.RESULT_OK) {
                InitializeImageView(data.getStringExtra("PHOTO_PATH"));
            }
        }
        if (requestCode == Picture_PREVIEW_Result_Code) {
            if (resultCode == Activity.RESULT_OK) {
                String action = data.getStringExtra("ACTION");
                if (action.equals("IMAGE_DELETED")) {
                    imageView.setVisibility(View.GONE);
                }
            }
        }
    }

    private boolean validateStudent() {
        if (name.isEmpty()) {
            nameEditText.setError(getResources().getString(R.string.name_required));
            return false;
        }
        if (lastName.isEmpty()) {
            lastNameEditText.setError(getResources().getString(R.string.last_name_required));
            return false;
        }
        if (studentNumberEditText.getText().toString().isEmpty()) {
            studentNumberEditText.setError(getResources().getString(R.string.studentnumber_required));
            return false;
        }
        if (photo == null) {
            Snackbar.make(view, "Neem eerst een foto van de student", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void InitializeStudent() {
        name = nameEditText.getText().toString().trim();
        lastName = lastNameEditText.getText().toString().trim();
        studentNumber = Integer.parseInt(studentNumberEditText.getText().toString());
    }

    public void OpenCameraActivity(View view) {

        startActivityForResult(new Intent(this, CameraActivity.class)
                .putExtra("STUDENT_NUMBER", Integer.parseInt(studentNumberEditText.getText().toString())), Camera_Intent_Result_Code);
    }
    public void InitializeImageView(String photoPath){
        PhotoPath = photoPath;
        photo = BitmapFactory.decodeFile(PhotoPath);
        imageView.setImageBitmap(photo);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(v -> startActivityForResult(new Intent(getApplicationContext(), ViewPictureActivity.class).putExtra("PHOTO_PATH", PhotoPath), Picture_PREVIEW_Result_Code));
    }
}

    

