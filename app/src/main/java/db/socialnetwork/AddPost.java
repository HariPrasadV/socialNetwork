package db.socialnetwork;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPost extends Fragment {

    private View rootView;
    public AddPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_add_post, container, false);

        TextView addPost = (TextView)rootView.findViewById(R.id.addpost);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText message = (EditText)rootView.findViewById(R.id.postcontent);
                String content = message.getText().toString();
                if (content.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Content can't be empty", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    new PostCreator().execute(content);
                }
            }
        });

        return rootView;
    }

    private class PostCreator extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... arg) {
            String url = MainActivity.BaseURL+"/CreatePost";
            String content = arg[0];
            ServiceHandler s = new ServiceHandler();
            String msg = "";
            try {
                msg = s.createPost(url, content);
                return msg;
            } catch (Exception e) {
                Log.v("MyUser:",e.getMessage());
                return "__invalid__";
            }
        }

        protected void onPostExecute(String result) {
            JSONObject auth;
            if(result.equals("Connection Error")){
                Toast.makeText(getActivity().getApplicationContext(),"Network Error, Try again later",Toast.LENGTH_LONG);
                return;
            }
            String authMsg="";
            try{
                auth = new JSONObject(result);
                if(auth.getBoolean("status")){
                    EditText e = (EditText) rootView.findViewById(R.id.postcontent);
                    Toast.makeText(getActivity().getApplicationContext(),"Successfully posted",Toast.LENGTH_LONG);
                    e.setText("");
                    Intent nextScreen = new Intent(getActivity().getApplicationContext(), Main2Activity.class);
                    nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(nextScreen);
                }
                else{
                    authMsg="Couldn't create post";;
                }
                TextView t = (TextView)rootView.findViewById(R.id.erradd);
                t.setText(authMsg);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
