package db.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;

import static db.socialnetwork.MainActivity.BaseURL;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String uid = (getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).getString("id",null);
        new getFollowedPosts().execute();
    }
    public void AddPost(View view) {
        String uid = (getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).getString("id",null);
        Intent nextScreen = new Intent(getApplicationContext(),CreatePostActivity.class);
        nextScreen.putExtra("id",uid);
        nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(nextScreen);
    }

    public void Logout(View view){
        new siteLogout().execute();
    }

    private class siteLogout extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... voids) {
            String url = MainActivity.BaseURL+"/Logout";
            ServiceHandler s = new ServiceHandler();
            String msg = "";
            try {
                msg = s.logout(url);
                return msg;
            } catch (Exception e) {
                Log.v("MyUser:",e.getMessage());
                return "__invalid__";
            }
        }
        protected void onPostExecute(String result) {
            Log.v("Result",result);
            try{
                JSONObject deauth = new JSONObject(result);
                if(deauth.getBoolean("status")){
                    SharedPreferences.Editor e = (getApplicationContext()).getSharedPreferences("Myprefs", MODE_PRIVATE).edit();
                    e.clear();
                    e.commit();
                    Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
                    nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(nextScreen);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class getFollowedPosts extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... voids) {
            String url = MainActivity.BaseURL+"/SeePosts";
            ServiceHandler s = new ServiceHandler();
            String msg = "";
            try {
                msg = s.seePosts(url);
                return msg;
            } catch (Exception e) {
                Log.v("MyUser:",e.getMessage());
                return "__invalid__";
            }
        }

        protected void onPostExecute(String result) {
            Log.v("Result",result);
            try {
                ListView myListView = (ListView)findViewById(R.id.posts);
                ArrayList<String> myStringArray = new ArrayList<String>();
                JSONObject resObj = new JSONObject(result);
                JSONArray resArray = new JSONArray(resObj.getString("data"));
                for(int i=0; i<resArray.length(); i++){
                    myStringArray.add(resArray.getJSONObject(i).getString("uid")
                   + "\n"+resArray.getJSONObject(i).getString("text")
                   + "\nComments:\n"+resArray.getJSONObject(i).getString("Comment"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, myStringArray);
                myListView.setAdapter(adapter);

                /*JSONObject resArray = new JSONObject(result);
                TextView t = (TextView)findViewById(R.id.posts);
                t.setText(resArray.getString("data"));
                t.setMovementMethod(new ScrollingMovementMethod());
                for (int i=0; i<resArray.length(); i++) {
                    JSONObject userPosts = resArray.getJSONObject(i);
                    t.append(userPosts.getString("text"));
                }*/
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

