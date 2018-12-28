package jarno.oussama.com.examenmonitor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import jarno.oussama.com.examenmonitor.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class ViewPictureActivity extends AppCompatActivity {
    ImageView imageView;
    Button buttonConfirm, buttonDelete;
    String PhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_picture);
        imageView = findViewById(R.id.imageView);
        buttonConfirm = findViewById(R.id.buttonConfirmPhoto);
        buttonDelete = findViewById(R.id.buttonDeletePhoto);
        Intent intent = getIntent();
        PhotoPath = intent.getStringExtra("PHOTO_PATH");
        Bitmap photo = BitmapFactory.decodeFile(PhotoPath);
        imageView.setImageBitmap(photo);
        buttonConfirm.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ACTION", "IMAGE_CONFIRMED");
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });
        buttonDelete.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("ACTION", "IMAGE_DELETED");
            setResult(Activity.RESULT_OK,returnIntent);
            File file = new File(PhotoPath);
            file.delete();
            finish();
        });
    }
}
