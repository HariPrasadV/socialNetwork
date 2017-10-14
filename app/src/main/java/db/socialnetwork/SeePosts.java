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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeePosts extends Fragment {

    private View rootView;
    private ListView lv;
    private ArrayList<Posts> see_posts;
    private PostsAdapter pa;
    private int offset,limit;
    private boolean more_data_available;

    public SeePosts() {
        // Required empty public constructor
        offset = 0;
        limit = 10;
        more_data_available = true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        offset = 0;
        more_data_available = true;
        if((getActivity().getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).getInt("offset",0)!=0){
            Log.v("Value of Offset",Integer.toString(offset));
            offset = -1;
            SharedPreferences.Editor e = (getActivity().getApplicationContext()).getSharedPreferences("Myprefs", MODE_PRIVATE).edit();
            e.remove("offset");
            e.commit();
        }
        rootView = inflater.inflate(R.layout.fragment_see_posts, container, false);
        lv = (ListView)rootView.findViewById(R.id.posts);
        see_posts = new ArrayList<>();
        pa = new PostsAdapter(getActivity().getApplicationContext(),R.layout.post_view,see_posts,true);
        lv.setAdapter(pa);
        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                new getFollowedPosts().execute(offset,limit);
                return more_data_available;
            }
        });
        new getFollowedPosts().execute(offset,limit);
        return rootView;
    }

    private class getFollowedPosts extends AsyncTask<Integer, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Integer... inte) {
            String url = MainActivity.BaseURL+"/SeePosts";
            ServiceHandler s = new ServiceHandler();
            String msg = "";
            try {
                msg = s.seePosts(url,inte[0],inte[1]);
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
            try {
                Log.v("res",result);
                JSONObject resObj = new JSONObject(result);
                if(!resObj.getBoolean("status")){
                    Log.v("Hello:","hello1");
                    if(resObj.has("message")){
                        if(resObj.getString("message").equals("Invalid session")){
                            Log.v("Hello:","hello1");
                            Intent nextScreen = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                            nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(nextScreen);
                        }
                    }
                }

                JSONArray resArray = new JSONArray(resObj.getString("data"));
                if(resArray.length()==0)
                    more_data_available=false;
                for(int i=0; i<resArray.length(); i++){
                    JSONArray commentArray = new JSONArray(resArray.getJSONObject(i).getString("Comment"));
                    ArrayList<Comments> commentsList = new ArrayList<>();
                    for(int j=0; j<commentArray.length(); j++){
                        commentsList.add(
                                new Comments(
                                        commentArray.getJSONObject(j).getString("uid"),
                                        commentArray.getJSONObject(j).getString("text")
                                )
                        );
                    }
                    String imgBytes = null;
                    if (resArray.getJSONObject(i).has("image")) {
                        imgBytes = resArray.getJSONObject(i).getString("image");
                    }
                    Posts p = new Posts(
                            resArray.getJSONObject(i).getString("uid"),
                            resArray.getJSONObject(i).getString("text"),
                            imgBytes,
                            resArray.getJSONObject(i).getString("postid"),
                            commentsList
                    );
                    see_posts.add(p);
                }
                if(resObj.has("offset")){
                    offset = resObj.getInt("offset")+resArray.length();
                }
                else{
                    offset = see_posts.size();
                }
                pa.notifyDataSetChanged();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
