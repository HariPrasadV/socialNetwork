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

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class MainActivity extends AppCompatActivity {
//      public static final String BaseURL = "http://192.168.0.102:8080/SocialBackend";
    public static final String BaseURL = "http://192.168.0.101:8080/Backend";
//    public static final String BaseURL = "http://10.196.24.98:8080/SocialBackend";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(getApplicationContext()),CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        setContentView(R.layout.activity_main);
    }

    public void printLog(View view){
        EditText name = (EditText) findViewById(R.id.userName);
        String uname = name.getText().toString();
        EditText password = (EditText) findViewById(R.id.pwd);
        String upwd = password.getText().toString();
        if(uname.equals("")||upwd.equals("")){
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
            return;
        }
        new ValidateUser().execute(uname, upwd);
    }

    private class ValidateUser extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... arg) {
            String url = BaseURL+"/Login";
            String uname = arg[0];
            String pwd = arg[1];
            ServiceHandler s = new ServiceHandler();
            String msg = "";
            try {
                msg = s.authorizationCall(url, uname, pwd);
                return msg;
            } catch (Exception e) {
                Log.v("MyUser:",e.getMessage());
                return "__invalid__";
            }


        }


        protected void onPostExecute(String result) {
            JSONObject auth;
            String authMsg="";
            if(result.equals("Connection Error")){
                Toast.makeText(getApplicationContext(),"Network Error, Try again later",Toast.LENGTH_LONG);
                return;
            }
            try{
                auth = new JSONObject(result);
                if(auth.getBoolean("status")){
                    SharedPreferences.Editor e = (getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).edit();
//                    e.putString("id",auth.getString("data"));
                    e.putInt("offset",-1);
                    e.commit();
                    Intent nextScreen = new Intent(getApplicationContext(),Main2Activity.class);
                    nextScreen.putExtra("id",auth.getString("data"));
                    nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(nextScreen);
                }
                else{
                    authMsg="Invalid Credentials";;
                }
                TextView t = (TextView)findViewById(R.id.authM);
                t.setText(authMsg);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}