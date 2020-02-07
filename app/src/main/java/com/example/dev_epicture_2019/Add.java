package com.example.dev_epicture_2019;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
public class Add extends Common {

    final int REQUEST_IMAGE_PICTURE = 1;
    public String keyUserDocOne = "Doc1";
    ImageView IDProf;
    Button btnUpload;
    private String Document_img1= "";
    Uri imageUri;
    String userAccessToken;
    Bitmap mbitmap;
    String imageBase64="";



    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 2, getApplicationContext());
        overridePendingTransition(0, 0);

        IDProf = (ImageView) findViewById(R.id.IdProf);
        btnUpload = (Button) findViewById(R.id.uploadBtn);
        userAccessToken = getAccesToken();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserImage();
            }
        });
    }

    public void selectUserImage() {
        final CharSequence[] options = {"Take a picture", "Take picture from gallery", "Cancel"};
        AlertDialog.Builder builderAlert = new AlertDialog.Builder(Add.this);
        builderAlert.setTitle("Add a picture");
        builderAlert.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take a picture")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_PICTURE);
                }
                if (options[which].equals("Take picture from gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                if (options[which].equals("Cancel"))
                    dialog.dismiss();
            }
        });
        builderAlert.show();
    }



    /**
     * @param data
     */
    public void displayThumbnailFromCamera(Intent data)
    {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap)extras.get("data");
        IDProf.setImageBitmap(imageBitmap);
    }

    /**
     * @param data
     */
    public void displayThumbnailFromGallery(Intent data)
    {
        imageUri = data.getData();
        IDProf.setImageURI(imageUri);
    }

    public void changeUploadLayout()
    {
        btnUpload.setBackgroundColor(getResources().getColor(R.color.green));
        IDProf.setBackgroundColor(getResources().getColor(R.color.white));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_IMAGE_PICTURE)) {
            changeUploadLayout();
            displayThumbnailFromCamera(data);
            mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
            imageBase64 = turnBitMapObjectIntoBase64String(mbitmap);
            //makeTheRequest(imageBase64);
        }
        if ((resultCode == RESULT_OK) && (requestCode == 2)) {
            changeUploadLayout();
            displayThumbnailFromGallery(data);
            mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
            imageBase64 = turnBitMapObjectIntoBase64String(mbitmap);
            //makeTheRequest(imageBase64);
        }
    }

    public Bitmap turnImageRecoveredIntoBitmap(Intent data, Bitmap bitmap)
    {
        Uri image = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
        } catch (IOException e) {
            e.getStackTrace();
        }
        return (bitmap);
    }

    public String turnBitMapObjectIntoBase64String(Bitmap objBitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        objBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] someByte = baos.toByteArray();
        String someStr = Base64.encodeToString(someByte, Base64.DEFAULT);
        return (someStr);
    }


}