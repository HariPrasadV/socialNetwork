package db.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Log.v("UserName",uname);
        Log.v("Password",upwd);
        new ValidateUser().execute(uname, upwd);
    }

    private class ValidateUser extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... arg) {
            String url = "http://192.168.0.102:8080/SocialBackend/Login";
            String uname = arg[0];
            String pwd = arg[1];
            ServiceHandler s = new ServiceHandler();
            String msg = "";
            try {
                msg = s.authorizationCall(url, uname, pwd);
                if (msg.equals("Invalid")) {
                    return "__invalidcre__";
                }
                else if(msg.equals("Connection Error")) {
                    return "__invalidc__";
                }

                return msg;
                //
            } catch (Exception e) {
                Log.v("MyUser:",e.getMessage());
                return "__invalid__";
            }


        }


        protected void onPostExecute(String result) {
            //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if(result.equals("__invalidcre__")){
                Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("__invalidc__")){
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("__invalid__")){
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            else{
                Log.v("Result",result);
//               SharedPreferences s =(getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE);
//                SharedPreferences.Editor e = s.edit();
//                e.putString("uname",result);
//                e.commit();
//                Toast toast = Toast.makeText(getApplicationContext(), "Welcome" + result, Toast.LENGTH_SHORT);
//                toast.show();
                //Log.v("HariPrasad",result);
//                Intent nextScreen = new Intent(getApplicationContext(), Main2Activity.class);
//                nextScreen.putExtra("uname", result);
//                nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                nextScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                nextScreen.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(nextScreen);
            }

        }

    }
}