package db.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class CreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
    }

    public void createPost(View view) {
        EditText message = (EditText) findViewById(R.id.editContent);
        String content = message.getText().toString();
        if (content.equals("")) {
            Toast.makeText(getApplicationContext(), "Content can't be empty", Toast.LENGTH_LONG).show();
            return;
        } else {
            new CreatePostActivity.PostCreator().execute(content);
        }
    }

    private class PostCreator extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... arg) {
            String url = MainActivity.BaseURL+"/CreatePost";
            String content = arg[0];
            ServiceHandler s = new ServiceHandler();
            String msg = "";
            try {
                msg = s.createPost(url, content);
                return msg;
            } catch (Exception e) {
                Log.v("MyUser:",e.getMessage());
                return "__invalid__";
            }
        }

        protected void onPostExecute(String result) {
            Log.v("Result",result);
            JSONObject auth;
            String authMsg="";
            try{
                auth = new JSONObject(result);
                if(auth.getBoolean("status")){
                    String uid = (getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).getString("id",null);
                    Intent nextScreen = new Intent(getApplicationContext(),Main2Activity.class);
                    nextScreen.putExtra("id",uid);
                    nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(nextScreen);
                }
                else{
                    authMsg="Couldn't create post";;
                }
                TextView t = (TextView)findViewById(R.id.errorCreatePost);
                t.setText(authMsg);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
