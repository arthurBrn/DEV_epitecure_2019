package com.example.dev_epicture_2019;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.appcompat.view.ContextThemeWrapper;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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


    /*public void makeTheRequest(String str)
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")
                .build();

        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + userAccessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    // Take a bitman object, encode it in a base64 format.
    /* public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    } */



    // OLD ON ACTIVITE RESULT

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Picture from camera
            if (requestCode == 1) {
                // We create a new file object
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = getResizedBitmap(bitmap, 400);
                    IDProf.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // If we take the piture from the gallery
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail=getResizedBitmap(thumbnail, 400);
                //Log.w("path of image from gallery......******************.........", picturePath+"");
                IDProf.setImageBitmap(thumbnail);
                //BitMapToString(thumbnail);
                postThePicture(BitMapToString(thumbnail));
            }
        }
    }
    */

    // Take a bitman object, encode it in a base64 format.
    /* public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    } */

    // HTTP request to send to the imgure client a new image
    /*
    public void postThePicture(String str)
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")
                .build();

        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + userAccessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */





    // ORIGINAL ONACTIVITERESULT

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_IMAGE_PICTURE)) {
            btnUpload.setBackgroundColor(getResources().getColor(R.color.green));
            IDProf.setBackgroundColor(getResources().getColor(R.color.white));
            displayThumbnailFromCamera(data);
        }
        if ((resultCode == RESULT_OK) && (requestCode == 2)) {
            displayThumbnailFromGallery(data);
            btnUpload.setBackgroundColor(getResources().getColor(R.color.green));
            IDProf.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

     */


}