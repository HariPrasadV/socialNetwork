package db.socialnetwork;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPost extends Fragment {

    private View rootView;
    public AddPost() {
        // Required empty public constructor
    }
    //refs- http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/
    private static int RESULT_LOAD_IMG = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_add_post, container, false);

        TextView addPost = (TextView)rootView.findViewById(R.id.addpost);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText message = (EditText)rootView.findViewById(R.id.postcontent);
                String content = message.getText().toString();
                ImageView imgView = (ImageView) rootView.findViewById(R.id.imgView);
                BitmapDrawable bitDraw = (BitmapDrawable)imgView.getDrawable();
                Bitmap bitmapImg = null;
                String encodedImg = null;
                if (bitDraw != null) {
                    bitmapImg = bitDraw.getBitmap();

                    int width = bitmapImg.getWidth();
                    int height = bitmapImg.getHeight();

                    if (width > 200) {
                        width = 200;
                    }
                    if (height > 200) {
                        height = 200;
                    }

                    bitmapImg = Bitmap.createScaledBitmap(bitmapImg, width, height, true);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteImg = stream.toByteArray();
                    encodedImg = Base64.encodeToString(byteImg, Base64.DEFAULT);
                }
                if (content.equals("") && bitmapImg == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Content can't be empty", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    new PostCreator().execute(new Posts(content, encodedImg));
                }
            }
        });

        Button loadImage = (Button)rootView.findViewById(R.id.buttonLoadPic);
        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// Create intent to Open Image applications like Gallery, Google Photos
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissions();
                }
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }
        });
        return rootView;
    }

    private void checkPermissions(){

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);

        }

    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1052: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED ){

                    // permission was granted.

                } else {

                    Toast.makeText(getActivity().getApplicationContext(),"Permission needed to add image",Toast.LENGTH_LONG).show();
                    // Permission denied - Show a message to inform the user that this app only works
                    // with these permissions granted

                }
                return;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) rootView.findViewById(R.id.imgView);
                // Set the Image in ImageView after decoding the String
                Bitmap bitmapImg = BitmapFactory.decodeFile(imgDecodableString);
                imgView.setImageBitmap(bitmapImg);

            } else {
                Toast.makeText(getActivity().getApplicationContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }


    private class PostCreator extends AsyncTask<Posts, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Posts... arg) {
            String url = MainActivity.BaseURL+"/CreatePost";
            String content = arg[0].post_content;
            String bmp = arg[0].base64_img;
            ServiceHandler s = new ServiceHandler();
            String msg = "";
            try {
                msg = s.createPost(url, content, bmp);
                return msg;
            } catch (Exception e) {
                Log.v("MyUser:",e.getMessage());
                return "__invalid__";
            }
        }

        protected void onPostExecute(String result) {
            JSONObject auth;
            if(result.equals("Connection Error")){
                Toast.makeText(getActivity().getApplicationContext(),"Network Error, Try again later",Toast.LENGTH_LONG);
                return;
            }
            String authMsg="";
            try{
                auth = new JSONObject(result);
                if(auth.getBoolean("status")){
                    EditText e = (EditText) rootView.findViewById(R.id.postcontent);
                    Toast.makeText(getActivity().getApplicationContext(),"Successfully posted",Toast.LENGTH_LONG);
                    e.setText("");
                    Intent nextScreen = new Intent(getActivity().getApplicationContext(), Main2Activity.class);
                    nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(nextScreen);
                }
                else{
                    authMsg="Couldn't create post";;
                }
                TextView t = (TextView)rootView.findViewById(R.id.erradd);
                t.setText(authMsg);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
