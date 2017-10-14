package db.socialnetwork;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_settings, container, false);

        TextView logout = (TextView)rootView.findViewById(R.id.Logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new siteLogout().execute();
            }
        });

        return rootView;
    }

    private class siteLogout extends AsyncTask<Void,Void,String> {
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
            if(result.equals("Connection Error")){
                Toast.makeText(getActivity().getApplicationContext(),"Network Error, Try again later",Toast.LENGTH_LONG);
                return;
            }
            try{
                JSONObject deauth = new JSONObject(result);
                if(deauth.getBoolean("status")){
                    SharedPreferences.Editor e = (getActivity().getApplicationContext()).getSharedPreferences("Myprefs", MODE_PRIVATE).edit();
                    e.clear();
                    e.commit();
                    Intent nextScreen = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(nextScreen);
                }
                else{
                    if(deauth.has("message")){
                        if(deauth.getString("message").equals("Invalid session")){
                            Intent nextScreen = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                            nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(nextScreen);
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
