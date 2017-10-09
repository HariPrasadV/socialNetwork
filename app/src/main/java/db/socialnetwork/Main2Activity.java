package db.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String uid = (getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).getString("id",null);
        new getFollowedPosts().execute();
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
            String url = "http://192.168.0.102:8080/SocialBackend/Logout";
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
            String url = "http://192.168.0.102:8080/SocialBackend/SeePosts";
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
        }
    }
}

