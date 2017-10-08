package db.socialnetwork;

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
    }
}