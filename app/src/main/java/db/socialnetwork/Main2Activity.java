package db.socialnetwork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private class Comments{
        String uid;
        String comment;
        Comments(String uid_,String comment_){
            uid = uid_;
            comment = comment_;
        }
    }
    private class Posts {
        String uid;
        String post_content;
        ArrayList<Comments> post_comments;
        Posts(String uid_, String cont_, ArrayList<Comments> comments_){
            uid = uid_;
            post_content = cont_;
            post_comments = comments_;
        }
    }

    //refs- https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/
    public class PostsAdapter extends ArrayAdapter<Posts> {

        private ArrayList<Posts> objects;

        public PostsAdapter(Context context, int textViewResourceId, ArrayList<Posts> objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.post_view, null);
            }
            final Posts i = objects.get(position);

            if (i != null) {
                TextView textV = (TextView) v.findViewById(R.id.text_content);
                final TextView commentsV = (TextView) v.findViewById(R.id.list_comments);
                final Button moreCommButton = (Button)v.findViewById(R.id.button_more);
                if (textV != null) {
                    textV.setText(i.uid + ": " + i.post_content);
                }
                if (commentsV != null) {

                    if (i.post_comments.size() == 0) {
                        commentsV.setVisibility(View.GONE);
                        moreCommButton.setVisibility(View.GONE);
                    }
                    else {
                        commentsV.setVisibility(View.VISIBLE);
                        if (i.post_comments.size() > 3) {
                            String myCommentArray = "";
                            for (int j=0; j<3; j++) {
                                myCommentArray +=
                                        i.post_comments.get(j).uid + ": " +
                                        i.post_comments.get(j).comment + "\n";
                            }
                            commentsV.setText(myCommentArray);

                            moreCommButton.setVisibility(View.VISIBLE);
                            moreCommButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v1) {
                                    String myCommentArray = "";
                                    for (int j=0; j<i.post_comments.size(); j++) {
                                        myCommentArray +=
                                                i.post_comments.get(j).uid + ": " +
                                                i.post_comments.get(j).comment + "\n";
                                    }
                                    commentsV.setText(myCommentArray);
                                    moreCommButton.setVisibility(View.GONE);
                                }
                            });
                        }
                        else {
                            moreCommButton.setVisibility(View.GONE);
                            String myCommentArray = "";
                            for (int j=0; j<i.post_comments.size(); j++) {
                                 myCommentArray +=
                                         i.post_comments.get(j).uid + ": " +
                                         i.post_comments.get(j).comment + "\n";
                            }
                            commentsV.setText(myCommentArray);
                        }
                    }
                }
            }
            return v;
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
                ArrayList<Posts> myPostArray = new ArrayList<>();
                JSONObject resObj = new JSONObject(result);
                JSONArray resArray = new JSONArray(resObj.getString("data"));
                for(int i=0; i<resArray.length(); i++){
                    JSONArray commentArray = new JSONArray(resArray.getJSONObject(i).getString("Comment"));
                    ArrayList<Comments> commentsList = new ArrayList<>();
                    //System.out.println("# commmments = ");
                    //System.out.println(commentArray.length());
                    for(int j=0; j<commentArray.length(); j++){
                        commentsList.add(
                                new Comments(
                                        commentArray.getJSONObject(j).getString("uid"),
                                        commentArray.getJSONObject(j).getString("text")
                                )
                        );
                    }
                    Posts p = new Posts(
                            resArray.getJSONObject(i).getString("uid"),
                            resArray.getJSONObject(i).getString("text"),
                            commentsList
                            );
                    System.out.println(p.post_content);
                    myPostArray.add(p);
                }
                PostsAdapter pAdapter = new PostsAdapter(getApplicationContext(),R.layout.post_view, myPostArray);
                myListView.setAdapter(pAdapter);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

