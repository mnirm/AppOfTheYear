package jarno.oussama.com.examenmonitor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private CameraKitView cameraKitView;
    private static final int PERMISSION_REQUEST_CODE = 1717;
    private int studentNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraKitView = findViewById(R.id.camera);
        verifyStoragePremissions();
        Intent AddUserIntent = getIntent();
        studentNumber =  AddUserIntent.getIntExtra("STUDENT_NUMBER",0);
    }

    private void verifyStoragePremissions(){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                ) {

        }else{
            ActivityCompat.requestPermissions(CameraActivity.this, permissions, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verifyStoragePremissions();
    }
    public void takePicture(View view) {
        cameraKitView.captureImage((cameraKitView, photo) -> {

            File savedPhoto = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), studentNumber + ".jpeg");
            try {
                savedPhoto.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!savedPhoto.exists()){
                savedPhoto.mkdir();
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                Bitmap photoBitmap = BitmapFactory.decodeByteArray(photo,0,photo.length);
                photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(savedPhoto) );
                Intent returnIntent = new Intent();
                returnIntent.putExtra("PHOTO_PATH", savedPhoto.getAbsolutePath());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CKDemo", "Exception in photo callback");
            }
        });
    }
}
