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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Add extends Common {

    final int REQUEST_PICTURE_CAMERA = 1;
    final int REQUEST_PICTURE_GALLERY = 2;
    // Image view containing the user picture to upload
    ImageView IDProf;
    // The button "browse" or "upload"
    Button btnUpload;
    // user access token
    String userAccessToken;
    // Used in turnRecoveredImageInBitman
    Bitmap mbitmap;
    // String containing image on format 64 byte
    String pictureTurnedIn64Format="";
    Uri imageUri;




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

        IDProf = (ImageView)findViewById(R.id.IdProf);
        btnUpload = (Button)findViewById(R.id.uploadBtn);
        userAccessToken = getAccesToken();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAlertBox();
            }
        });
    }

    public void generateAlertBox() {
        final CharSequence[] options = {"Take a picture", "Take picture from gallery", "Cancel"};
        AlertDialog.Builder builderAlert = new AlertDialog.Builder(Add.this);
        builderAlert.setTitle("Add a picture");
        builderAlert.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take a picture")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(takePictureIntent, REQUEST_PICTURE_CAMERA);
                }
                if (options[which].equals("Take picture from gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_PICTURE_GALLERY);
                }
                if (options[which].equals("Cancel"))
                    dialog.dismiss();
            }
        });
        builderAlert.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == REQUEST_PICTURE_CAMERA)
                displayThumbnailFromCamera(data);
            if (requestCode == REQUEST_PICTURE_GALLERY)
                displayThumbnailFromGallery(data);
            changeUploadLayout();

            mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
            pictureTurnedIn64Format = turnBitMapObjectIntoBase64String(mbitmap);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient cli = new OkHttpClient.Builder().build();
                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("image", pictureTurnedIn64Format)
                            .build();
                    Request req = new Request.Builder()
                            .url("https://api.imgur.com/3/upload")
                            .method("POST", body)
                            .header("Authorization", getAccesToken())
                            .header("User-agent", "DEV_epicture_2019")
                            .build();
                    cli.newCall(req).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            String mMessage = e.getMessage().toString();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String mMessage = response.body().string();
                        }
                    });
                }
            });

            Toast.makeText(getApplicationContext(), "SUCCESSFULLLY UPLOAD", Toast.LENGTH_LONG);
            /*btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
                    pictureTurnedIn64Format = turnBitMapObjectIntoBase64String(mbitmap);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient cli = new OkHttpClient.Builder().build();
                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("image", pictureTurnedIn64Format)
                                    .build();
                            Request req = new Request.Builder()
                                    .url("https://api.imgur.com/3/upload")
                                    .method("POST", body)
                                    .header("Authorization", getAccesToken())
                                    .header("User-agent", "DEV_epicture_2019")
                                    .build();
                            cli.newCall(req).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    String mMessage = e.getMessage().toString();
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String mMessage = response.body().string();
                                }
                            });
                        }
                    });
                }
            });*/
        }
    }

    /**
     *
     */
    public void changeUploadLayout()
    {
        btnUpload.setText("Upload");
        IDProf.setBackgroundColor(getResources().getColor(R.color.white));
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

    /**
     *
     * @param data
     * @param bitmap
     * @return
     */
    public Bitmap turnImageRecoveredIntoBitmap(Intent data, Bitmap bitmap)
    {
        try {
            mbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
        } catch (IOException e) {
            e.getStackTrace();
        }
        return (mbitmap);
    }

    /**
     *
     * @param objBitmap
     * @return
     */
    public String turnBitMapObjectIntoBase64String(Bitmap objBitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        objBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] someByte = baos.toByteArray();
        String someStr = Base64.encodeToString(someByte, Base64.DEFAULT);
        return (someStr);
    }

    /**
     *
     * @param image64Base
     * @throws IOException
     */
    public void uploadThePicture(String image64Base) throws IOException
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient cli = new OkHttpClient.Builder().build();
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image", image64Base)
                        .build();
                Request req = new Request.Builder()
                        .url("https://api.imgur.com/3/upload")
                        .method("POST", body)
                        .header("Authorization", getAccesToken())
                        .header("User-agent", "DEV_epicture_2019")
                        .build();
                cli.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        String mMessage = e.getMessage().toString();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String mMessage = response.body().string();
                    }
                });
            }
        });
    }




    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_IMAGE_PICTURE)) {
            changeUploadLayout();
            askUserToUpload();

            displayThumbnailFromCamera(data);
            mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
            String image46 = turnBitMapObjectIntoBase64String(mbitmap);
        }
        if ((resultCode == RESULT_OK) && (requestCode == 2)) {
            changeUploadLayout(data);
            askUserToUpload();

            displayThumbnailFromGallery(data);
            mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
            String image64 = turnBitMapObjectIntoBase64String(mbitmap);
            try {
                tryingToUpload(image64);
                Toast.makeText(getApplicationContext(), "UPLOADED", Toast.LENGTH_LONG);
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }
    */
}