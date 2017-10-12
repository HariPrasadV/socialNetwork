package db.socialnetwork;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

//refs- https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/

public class PostsAdapter extends ArrayAdapter<Posts> {

    private ArrayList<Posts> objects;
    private Context cont;
    public PostsAdapter(Context context, int textViewResourceId, ArrayList<Posts> objects) {
        super(context, textViewResourceId, objects);
        cont=context;
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

        final View root = v;

        if (i != null) {

            EditText e1 = (EditText)v.findViewById(R.id.new_comment);
            e1.setText("");

            TextView t1 = (TextView)v.findViewById(R.id.postHeader);
            TextView textV = (TextView) v.findViewById(R.id.text_content);

            final TextView commentsV = (TextView) v.findViewById(R.id.list_comments);
            final Button moreCommButton = (Button)v.findViewById(R.id.button_more);

            Button addComment = (Button)v.findViewById(R.id.add_comment);
            addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText e2 = (EditText)root.findViewById(R.id.new_comment);
                    new AddComment().execute(i.getPostid(),e2.getText().toString());
                    e2.clearFocus();
                }
            });

            if (textV != null) {
                t1.setText("Posted By "+i.getUid());
                textV.setText(i.getPost_content());
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

    private class AddComment extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = MainActivity.BaseURL+"/NewComment";
            String pid = strings[0];
            String content = strings[1];
            String msg="";
            ServiceHandler s = new ServiceHandler();
            try{
                msg=s.addComment(url,pid,content);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v("CommentRes",s);
            try{
                JSONObject json = new JSONObject(s);
                if(json.getBoolean("status")){
                    Toast.makeText(cont.getApplicationContext(), "Succesfully commented", Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
