package db.socialnetwork;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {

//    private String[] Countries = new String[] {"Belgium","Belfrost","Belfamilia","Belfamily" ,"France", "Italy", "Germany", "Spain"};
    public UserAdapter suggestions;
    public CustomAutoComplete ac;
    public SiteUser selectedUser;
    private View rootView;

    public Search() {
        // Required empty public constructor
        selectedUser = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ArrayList<SiteUser> users = new ArrayList<>();

        users.add(new SiteUser("Wait for suggestions","",""));

        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        final View UserOptions = rootView.findViewById(R.id.useroptions);
        UserOptions.setVisibility(View.GONE);

        ListView lv = (ListView) rootView.findViewById(R.id.idposts);
        lv.setVisibility(View.GONE);

        View IdPosts = rootView.findViewById(R.id.idposts);
        IdPosts.setVisibility(View.GONE);

        View UserFollow = rootView.findViewById(R.id.userfollow);
        UserFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedUser == null)
                    return;
                new FollowUnfollow().execute("/Follow",selectedUser.getUid());
            }
        });

        View UserUnfollow = rootView.findViewById(R.id.userunfollow);
        UserUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedUser == null)
                    return;
                new FollowUnfollow().execute("/Unfollow",selectedUser.getUid());
            }
        });

        View UserPosts = rootView.findViewById(R.id.userposts);
        UserPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedUser == null){
                    Log.v("hehe","No way");
                    return;
                }
                new getUserPosts().execute(selectedUser.getUid());
            }
        });

        ac = (CustomAutoComplete) rootView.findViewById(R.id.comp);

        View Cancel = rootView.findViewById(R.id.cancel);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ac.setText("");
                ListView lv = (ListView) rootView.findViewById(R.id.idposts);
                lv.setVisibility(View.GONE);
                selectedUser=null;
                UserOptions.setVisibility(View.GONE);
            }
        });

        ac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = "",suid,sname,semail;
                TextView t1=(TextView)view.findViewById(R.id.userid);
                text += t1.getText()+",";
                suid = t1.getText().toString();
                t1=(TextView)view.findViewById(R.id.username);
                text+=t1.getText()+",";
                sname = t1.getText().toString();
                t1=(TextView)view.findViewById(R.id.useremail);
                text+=t1.getText();
                semail = t1.getText().toString();
                ac.setText(text);
                UserOptions.setVisibility(View.VISIBLE);
                selectedUser = new SiteUser(suid,sname,semail);
            }
        });

        ac.addTextChangedListener(new CustomTextChangedListener(this,getActivity()));

        suggestions = new UserAdapter(getActivity(),users);

        ac.setAdapter(suggestions);

        return rootView;
    }

    private class FollowUnfollow extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = MainActivity.BaseURL + strings[0];
            String uid2 = strings[1];
            ServiceHandler s = new ServiceHandler();
            String msg="";
            try{
                msg = s.SearchForUserFollowUnFollow(url,uid2);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v("FollowUnfollow",s);
            try{
                JSONObject json = new JSONObject(s);
                if(json.getBoolean("status")){
                    Toast.makeText(getActivity().getApplicationContext(), json.getString("data"), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private class getUserPosts extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = MainActivity.BaseURL+"/SeeUserPosts";
            String msg="";
            String uid2 = strings[0];
            ServiceHandler s=new ServiceHandler();
            try{
                msg = s.seeUserPosts(url,uid2);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                ListView lv = (ListView) rootView.findViewById(R.id.idposts);
                ArrayList<Posts> userPosts = new ArrayList<>();
                JSONObject json = new JSONObject(s);
                if(json.getBoolean("status")){
                    JSONArray j = new JSONArray(json.getString("data"));
                    for(int i=0;i<j.length();i++){
                        JSONObject j1 = j.getJSONObject(i);
                        JSONArray commentsList = new JSONArray(j1.getString("Comment"));
                        ArrayList<Comments> c = new ArrayList<>();
                        if(commentsList.length()==0) {
                            for (int k = 0; k < commentsList.length(); k++) {
                                c.add(new Comments(commentsList.getJSONObject(k).getString("uid"), commentsList.getJSONObject(k).getString("text")));
                            }
                        }
                        userPosts.add(new Posts(j1.getString("uid"),j1.getString("text"),j1.getString("postid"),c));
                    }
                    PostsAdapter p = new PostsAdapter(getActivity().getApplicationContext(),R.layout.post_view, userPosts);
                    lv.setVisibility(View.VISIBLE);
                    lv.setAdapter(p);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
