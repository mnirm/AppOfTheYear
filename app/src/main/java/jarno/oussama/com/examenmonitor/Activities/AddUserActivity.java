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
    String name, lastName;
    String pictureDownloadUrl;
    int studentNumber;
    NFC nfc;
    View view;
    Button addButton, cameraButton;
    DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("students");
    Bitmap photo;
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
            addButton.setEnabled(true);
        }
        studentNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() == 0) {
                    cameraButton.setEnabled(false);
                } else {
                    cameraButton.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
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
        if (nfc != null) {
            outState.putString("NFC_ID", nfc.nfcID);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfc.setResolveIntent(intent, (boolean isValid) -> {
            if (isValid) {
                addButton.setEnabled(true);
                nfcStatusTextView.setText(nfc.nfcID);
            } else {
                Snackbar.make(view, "ongeldige kaart", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void AddUserToDB(View view) {
        Initialize();
        Student student = new Student();
        if (validate()) {
            // uploaden picture to firebase and get url
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Student_profile_pictures").child(studentNumber + ".jpg");
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(exception -> {
                Snackbar.make(view, "Het uploaden van de foto is mislukt", Snackbar.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.d("retrieve_download_url", "onSuccess: uri= " + uri.toString());
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
                String PhotoPath = data.getStringExtra("PHOTO_PATH");
                photo = BitmapFactory.decodeFile(PhotoPath);
                imageView.setImageBitmap(photo);
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(v -> startActivityForResult(new Intent(getApplicationContext(), ViewPictureActivity.class).putExtra("PHOTO_PATH", PhotoPath), Picture_PREVIEW_Result_Code));
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

    private boolean validate() {
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

    public void Initialize() {
        name = nameEditText.getText().toString().trim();
        lastName = lastNameEditText.getText().toString().trim();
        studentNumber = Integer.parseInt(studentNumberEditText.getText().toString());
    }

    public void OpenCameraActivity(View view) {

        startActivityForResult(new Intent(this, CameraActivity.class).putExtra("STUDENT_NUMBER", Integer.parseInt(studentNumberEditText.getText().toString())), Camera_Intent_Result_Code);
    }

}

    

