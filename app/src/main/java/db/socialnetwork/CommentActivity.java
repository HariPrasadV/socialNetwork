package db.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        final String pid = (getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).getString("pid",null);

        Button AddComment = (Button)findViewById(R.id.addc);

        AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pid!=null){
                    EditText e1 = (EditText)findViewById(R.id.newc);
                    String comm = e1.getText().toString();
                    if(comm.equals("")){
                        Toast.makeText(getApplicationContext(),"No empty comments",Toast.LENGTH_SHORT);
                    }
                    else{
                        new AddComment().execute(pid,comm);
                        Toast.makeText(getApplicationContext(),"Press the BACK button to go back",Toast.LENGTH_SHORT);
                    }
                }
            }
        });

        Button Back = (Button)findViewById(R.id.back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor e = (getApplicationContext()).getSharedPreferences("Myprefs", MODE_PRIVATE).edit();
                String uid = (getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).getString("id",null);
                e.clear();
                if(uid!=null){
                    e.putString("id",uid);
                }
                e.commit();
                Intent nextScreen = new Intent(getApplicationContext(), Main2Activity.class);
                nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(nextScreen);
            }
        });
    }

    private class AddComment extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = MainActivity.BaseURL+"/NewComment";
            String pid = strings[0];
            String content = strings[1];
            String msg="";
            ServiceHandler s = new ServiceHandler();
            try{
                msg=s.addComment(url,pid,content);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v("CommentRes",s);
            if(s.equals("Connection Error")){
                Toast.makeText(getApplicationContext(),"Network Error, Try again later",Toast.LENGTH_LONG);
                return;
            }
            try{
                JSONObject json = new JSONObject(s);
                if(json.getBoolean("status")){
                    Toast.makeText(getApplicationContext(), "Succesfully commented", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
