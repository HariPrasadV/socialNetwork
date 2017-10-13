package db.socialnetwork;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class SeePosts extends Fragment {

    private View rootView;

    public SeePosts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_see_posts, container, false);
        new getFollowedPosts().execute();
        return rootView;
    }

    private class getFollowedPosts extends AsyncTask<Void, Void, String> {
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
            if(result.equals("Connection Error")){
                Toast.makeText(getActivity().getApplicationContext(),"Network Error, Try again later",Toast.LENGTH_LONG);
                return;
            }
            try {
                ListView myListView = (ListView)rootView.findViewById(R.id.posts);
                ArrayList<Posts> myPostArray = new ArrayList<>();
                JSONObject resObj = new JSONObject(result);
                JSONArray resArray = new JSONArray(resObj.getString("data"));
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
                    Posts p = new Posts(
                            resArray.getJSONObject(i).getString("uid"),
                            resArray.getJSONObject(i).getString("text"),
                            resArray.getJSONObject(i).getString("postid"),
                            commentsList
                    );
                    myPostArray.add(p);
                }
                PostsAdapter pAdapter = new PostsAdapter(getActivity().getApplicationContext(),R.layout.post_view, myPostArray);
                myListView.setAdapter(pAdapter);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
