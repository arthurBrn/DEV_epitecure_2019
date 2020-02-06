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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Add extends Common {

    final int REQUEST_IMAGE_PICTURE = 1;
    public String keyUserDocOne = "Doc1";
    ImageView IDProf;
    Button btnUpload;
    private String doc1 = "";
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        Common.changeActivity(navigationBar, 2, getApplicationContext());
        overridePendingTransition(0, 0);

        IDProf = (ImageView) findViewById(R.id.IdProf);
        btnUpload = (Button) findViewById(R.id.uploadBtn);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserImage();
            }
        });
    }

    /**
     * Allow user to access his files and chose one.
     */
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
                    {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_PICTURE);
                    }
                }
                if (options[which].equals("Take picture from gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builderAlert.show();
    }


    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICTURE)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            IDProf.setImageBitmap(imageBitmap);
        }
        if (resultCode == RESULT_OK && requestCode == 2) {
            imageUri = data.getData();
            IDProf.setImageURI(imageUri);
        }
    }



    /*
    // Used in OnActivityResult
    public Bitmap getResizedBitmap(Bitmap mPicture, int maxPictureSize) {
        int w = mPicture.getWidth();
        int h = mPicture.getHeight();

        float bitmapRatio = (float) w / (float) h;
        if (bitmapRatio > 1) {
            w = maxPictureSize;
            h = (int) (w / bitmapRatio);
        } else {
            h = maxPictureSize;
            w = (int) (h * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(mPicture, w, h, true);
    }

    public String BitMapToString(Bitmap userImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        doc1 = Base64.encodeToString(b, Base64.DEFAULT);

        return (doc1);
    } */

    /*

            private void SendDetail() {
                final ProgressDialog loading = new ProgressDialog(Uplode_Reg_Photo.this);
                loading.setMessage("Please Wait...");
                loading.show();
                loading.setCanceledOnTouchOutside(false);
                RetryPolicy mRetryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiURL.Registration_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    loading.dismiss();
                                    Log.d("JSON", response);

                                    JSONObject eventObject = new JSONObject(response);
                                    String error_status = eventObject.getString("error");
                                    if (error_status.equals("true")) {
                                        String error_msg = eventObject.getString("msg");
                                        ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
                                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                                        alertDialogBuilder.setTitle("Vendor Detail");
                                        alertDialogBuilder.setCancelable(false);
                                        alertDialogBuilder.setMessage(error_msg);
                                        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                        alertDialogBuilder.show();

                                    } else {
                                        String error_msg = eventObject.getString("msg");
                                        ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
                                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                                        alertDialogBuilder.setTitle("Registration");
                                        alertDialogBuilder.setCancelable(false);
                                        alertDialogBuilder.setMessage(error_msg);
    //                                alertDialogBuilder.setIcon(R.drawable.doubletick);
                                        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent=new Intent(Uplode_Reg_Photo.this,Log_In.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        alertDialogBuilder.show();
                                    }
                                }catch(Exception e){
                                    Log.d("Tag", e.getMessage());

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                                    alertDialogBuilder.setTitle("No connection");
                                    alertDialogBuilder.setMessage(" Connection time out error please try again ");
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                                    alertDialogBuilder.show();
                                } else if (error instanceof AuthFailureError) {
                                    ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                                    alertDialogBuilder.setTitle("Connection Error");
                                    alertDialogBuilder.setMessage(" Authentication failure connection error please try again ");
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                                    alertDialogBuilder.show();
                                    //TODO
                                } else if (error instanceof ServerError) {
                                    ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                                    alertDialogBuilder.setTitle("Connection Error");
                                    alertDialogBuilder.setMessage("Connection error please try again");
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                                    alertDialogBuilder.show();
                                    //TODO
                                } else if (error instanceof NetworkError) {
                                    ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                                    alertDialogBuilder.setTitle("Connection Error");
                                    alertDialogBuilder.setMessage("Network connection error please try again");
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                                    alertDialogBuilder.show();
                                    //TODO
                                } else if (error instanceof ParseError) {
                                    ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                                    alertDialogBuilder.setTitle("Error");
                                    alertDialogBuilder.setMessage("Parse error");
                                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                                    alertDialogBuilder.show();
                                }
    //                        Toast.makeText(Login_Activity.this,error.toString(), Toast.LENGTH_LONG ).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put(KEY_User_Document1,Document_img1);
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                stringRequest.setRetryPolicy(mRetryPolicy);
                requestQueue.add(stringRequest);
            }


            @Override
            public void onClick(View v) {
                if (Document_img1.equals("") || Document_img1.equals(null)) {
                    ContextThemeWrapper ctw = new ContextThemeWrapper( Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
                    alertDialogBuilder.setTitle("Id Prof Can't Empty ");
                    alertDialogBuilder.setMessage("Id Prof Can't empty please select any one document");
                    alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    alertDialogBuilder.show();
                    return;
                }
                else{

                    if (AppStatus.getInstance(this).isOnline()) {
                        SendDetail();


                        //           Toast.makeText(this,"You are online!!!!",Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_LONG).show();
                        Log.v("Home", "############################You are not online!!!!");
                    }

                }
            }
    }


     */

    /*
    @Override
    public void onClick(View v) {
        if (Document_img1.equals("") || Document_img1.equals(null)) {
            ContextThemeWrapper ctw = new ContextThemeWrapper(Uplode_Reg_Photo.this, R.style.Theme_AlertDialog);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
            alertDialogBuilder.setTitle("Id Prof Can't Empty ");
            alertDialogBuilder.setMessage("Id Prof Can't empty please select any one document");
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialogBuilder.show();
            return;
        } else {

            if (AppStatus.getInstance(this).isOnline()) {
                SendDetail();


                //           Toast.makeText(this,"You are online!!!!",Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "You are not online!!!!", Toast.LENGTH_LONG).show();
                // Log.v("Home", "############################You are not online!!!!");
            }

        }
    }
*/
}