package com.example.dev_epicture_2019;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    String pictureTurnedIn64Format = "";
    Uri imageUri;
    EditText descriptionUploadFile;
    String someText;
    ProgressBar uploadProgress;
    final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    File sourceFile;

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
        descriptionUploadFile = findViewById(R.id.upload_file_description);
        uploadProgress = findViewById(R.id.uploadProgressBar);
        someText = "";

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


    public String cursorOnString(Context context, Intent contentUri)
    {
        String str = "";
        Cursor cursor = null;

        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri.getData(),  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            str = cursor.getString(column_index);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return (str);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String cleanedPath = cursorOnString(getApplicationContext(), data);
            Log.d("TAG IMAGE", "FILEPATH " + cleanedPath);
            sourceFile = new File(cleanedPath);
            changeUploadLayout();
            if (requestCode == REQUEST_PICTURE_CAMERA)
                displayThumbnailFromCamera(data);
            if (requestCode == REQUEST_PICTURE_GALLERY)
                displayThumbnailFromGallery(data);

            //mbitmap = turnImageRecoveredIntoBitmap(data, mbitmap);
            //pictureTurnedIn64Format = turnBitMapObjectIntoBase64String(mbitmap);

            descriptionUploadFile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    someText = s.toString();
                }
            });

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAsyncTask();
                }
            });
        }
    }


    public void startAsyncTask()
    {
        uploadingFile asyncUpload = new uploadingFile();
        asyncUpload.execute();
    }

    public class uploadingFile extends AsyncTask<File, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uploadProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(File... files) {
            Log.d("SOURCEFILE","SOURCEFILE " + sourceFile);
            OkHttpClient cli = new OkHttpClient.Builder().build();
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "Some description", RequestBody.create(sourceFile, MEDIA_TYPE_JPG))
                    .build();
            Request req = new Request.Builder()
                    .url("https://api.imgur.com/3/upload")
                    .method("POST", body)
                    .header("Authorization", "Bearer " + getAccesToken())
                    .header("User-agent", "DEV_epicture_2019")
                    .build();
            try {
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
                Thread.sleep(1000);
            } catch (Exception e) {
                e.getStackTrace();
            }
            return ("Uploaded successfully");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            uploadProgress.setProgress(values[0]);
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            uploadProgress.setProgress(0);
            uploadProgress.setVisibility(View.INVISIBLE);
        }
    }

    /**
     *
     */
    public void changeUploadLayout() {
        btnUpload.setText("Upload");
        IDProf.setBackgroundColor(getResources().getColor(R.color.white));
    }

    /**
     * @param data
     */
    public void displayThumbnailFromCamera(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        IDProf.setImageBitmap(imageBitmap);
    }

    /**
     * @param data
     */
    public void displayThumbnailFromGallery(Intent data) {
        imageUri = data.getData();
        IDProf.setImageURI(imageUri);
    }

    /**
     * @param data
     * @param bitmap
     * @return
     */
    public Bitmap turnImageRecoveredIntoBitmap(Intent data, Bitmap bitmap) {
        try {
            mbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
        } catch (IOException e) {
            e.getStackTrace();
        }
        return (mbitmap);
    }

    /**
     * @param objBitmap
     * @return
     */
    public String turnBitMapObjectIntoBase64String(Bitmap objBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        objBitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] someByte = baos.toByteArray();
        String someStr = Base64.encodeToString(someByte, Base64.DEFAULT);
        return (someStr);
    }
}