package com.example.dev_epicture_2019;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        IDProf = (ImageView)findViewById(R.id.IdProf);
        btnUpload = (Button)findViewById(R.id.uploadBtn);
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
            //changeUploadLayout();
            displayThumbnailFromCamera(data);
            mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
            String image46 = turnBitMapObjectIntoBase64String(mbitmap);
            //requestUploadImage(imageBase64);
            try {
                tryingToUpload(image46);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if ((resultCode == RESULT_OK) && (requestCode == 2)) {
            //changeUploadLayout();
            displayThumbnailFromGallery(data);
            mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
            String image64 = turnBitMapObjectIntoBase64String(mbitmap);
            try {
                tryingToUpload(image64);
            } catch (IOException e) {
                e.getStackTrace();
            }
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

    public File turnImageToFile(String image)
    {
        File file = new File(Environment.getExternalStorageState(), image);
        return (file);
    }

    // REQUEST
    public void requestUploadImage(String image64) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", image64, RequestBody.create(mediaType, image64))
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/upload")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + getAccesToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void tryingToUpload(String image64Base) throws IOException
    {
        OkHttpClient cli = new OkHttpClient.Builder().build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", image64Base)
                .build();
        Request req = new Request.Builder()
                .url("https://api.imgur.com/3/upload")
                .method("POST", body)
                .header("Authorization", getAccesToken()) // check for client ID
                .header("User-ageant", "DEV_epicture_2019")
                .build();
        cli.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.d("failure Response", mMessage);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                //Log.e(TAG, mMessage);
            }
        });
    }


    /*

    Recover the user photo address, turns it into a file, then bitmap, then string and send it, might work

    File imgFile = new  File("/sdcard/Images/test_image.jpg");

if(imgFile.exists()){

    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

    ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

    myImage.setImageBitmap(myBitmap);

}

     */

}