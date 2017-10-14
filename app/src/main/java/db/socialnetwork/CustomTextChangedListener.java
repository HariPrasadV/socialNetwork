package db.socialnetwork;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jeyasoorya on 12/10/17.
 */


public class CustomTextChangedListener implements TextWatcher {

    Search forAdapter;
    Context c;

    public CustomTextChangedListener(Search s,Context act){
        forAdapter = s;
        c = act;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()<3){
            return;
        }
        new UserSuggestions().execute(charSequence.toString());
    }

    private class UserSuggestions extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = MainActivity.BaseURL+"/SearchUser";
            String searchTerm = strings[0];
            ServiceHandler s = new ServiceHandler();
            String msg="";
            try{
                msg = s.SearchForUserFollowUnFollow(url,searchTerm);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject searchRes;
            if(s.equals("Connection Error")){
                Toast.makeText(c.getApplicationContext(),"Network Error, Try again later",Toast.LENGTH_LONG);
                return;
            }
            try{
                searchRes = new JSONObject(s);
                if(searchRes.getBoolean("status")){
                    try{
                        JSONArray sugg = new JSONArray(searchRes.getString("data"));
                        ArrayList<SiteUser> SUGG = new ArrayList<>();
                        JSONArray json = new JSONArray(sugg.getString(0));
                        for(int i=0;i<json.length();i++){
                            JSONObject j = json.getJSONObject(i);
                            SUGG.add(new SiteUser(j.getString("uid"),j.getString("name"),j.getString("email")));
                        }
                        forAdapter.suggestions.notifyDataSetChanged();
                        forAdapter.suggestions = new UserAdapter(c,SUGG);
                        forAdapter.ac.setAdapter(forAdapter.suggestions);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    if(searchRes.has("message")){
                        if(searchRes.getString("message").equals("Invalid session")){
                            Intent nextScreen = new Intent(c.getApplicationContext(),MainActivity.class);
                            nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            c.startActivity(nextScreen);
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
