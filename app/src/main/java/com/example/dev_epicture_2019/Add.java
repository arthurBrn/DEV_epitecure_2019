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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Add extends Common {
    final String AB_TITLE = "Upload a picture";
    final String AB_TAKE_PICTURE = "Take a picture";
    final String AB_CHOSE_GALLERY = "Chose from gallery";
    final String AB_CANCEL = "Cancel";
    final String UPLOAD_SUCCESS = "Uploaded";
    final String URL_UPLOAD = "https://api.imgur.com/3/upload";
    final int REQUEST_PICTURE_CAMERA = 1;
    final int REQUEST_PICTURE_GALLERY = 2;
    ApiHandler apiHandler;
    ImageView IDProf;
    Button btnUpload;
    String userAccessToken;
    Bitmap mbitmap;
    EditText descriptionUploadFile;
    String someText;
    ProgressBar uploadProgress;
    final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    File sourceFile;
    boolean uploadedButtonState = false;

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

        apiHandler = new ApiHandler();
        IDProf = findViewById(R.id.IdProf);
        btnUpload = findViewById(R.id.uploadBtn);
        userAccessToken = getAccesToken();
        descriptionUploadFile = findViewById(R.id.upload_file_description);
        uploadProgress = findViewById(R.id.uploadProgressBar);
        someText = "";

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadedButtonState == false) {
                    uploadedButtonState = true;
                    generateAlertBox();
                }
            }
        });
    }

    public void generateAlertBox() {
        final CharSequence[] options = {AB_TAKE_PICTURE, AB_CHOSE_GALLERY, AB_CANCEL};
        AlertDialog.Builder builderAlert = new AlertDialog.Builder(Add.this);
        builderAlert.setTitle(AB_TITLE);
        builderAlert.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(AB_TAKE_PICTURE)) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(takePictureIntent, REQUEST_PICTURE_CAMERA);
                }
                if (options[which].equals(AB_CHOSE_GALLERY)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_PICTURE_GALLERY);
                }
                if (options[which].equals(AB_CANCEL))
                    dialog.dismiss();
            }
        });
        builderAlert.show();
        uploadedButtonState = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            changeUploadLayout();
            String cleanedPath = "";
            if (requestCode == REQUEST_PICTURE_CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                IDProf.setImageBitmap(imageBitmap);
                cleanedPath = getImageFullPath(getApplicationContext(), getImageUri(imageBitmap));
            }
            if (requestCode == REQUEST_PICTURE_GALLERY) {
                IDProf.setImageURI(data.getData());
                cleanedPath = getImageFullPath(getApplicationContext(), data.getData());
            }

            sourceFile = new File(cleanedPath);
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
                    if (uploadedButtonState == true) {
                        uploadedButtonState = false;
                        startAsyncTask();
                    } else {
                        Toast.makeText(getApplicationContext(), "nop", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public String getImageFullPath(Context context, Uri contentUri)
    {
        String str = "";
        Cursor cursor = null;

        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            str = cursor.getString(column_index);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return (str);
    }

    public Uri getImageUri(Bitmap imageBitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String thePath = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), imageBitmap, "some title", "some description");
        return (Uri.parse(thePath));
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
            if (someText.isEmpty())
                someText=" ";
            OkHttpClient cli = new OkHttpClient.Builder().build();

            RequestBody body = apiHandler.buildPostRequestBody(someText, MEDIA_TYPE_JPG, sourceFile);
            Request req = apiHandler.buildGetRequest(URL_UPLOAD, getAccesToken());
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
            return (UPLOAD_SUCCESS);
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
            btnUpload.setText("Browse");
            descriptionUploadFile.setText("");
            IDProf.setBackgroundColor(getResources().getColor(R.color.white));
            IDProf.setImageURI(null);
        }
    }

    /**
     *
     */
    public void changeUploadLayout() {
        btnUpload.setText("Upload");
        IDProf.setBackgroundColor(getResources().getColor(R.color.white));
        uploadedButtonState = false;
    }
}