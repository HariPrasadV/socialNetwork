package db.socialnetwork;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

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

            TextView t1 = (TextView)v.findViewById(R.id.postHeader);
            TextView textV = (TextView) v.findViewById(R.id.text_content);
            ImageView imgV = (ImageView) v.findViewById(R.id.imgPostView);
            final TextView commentsV = (TextView) v.findViewById(R.id.list_comments);
            final Button moreCommButton = (Button)v.findViewById(R.id.button_more);

            Button addComment = (Button)v.findViewById(R.id.add_comment);
            addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor e = (cont.getApplicationContext()).getSharedPreferences("Myprefs",MODE_PRIVATE).edit();
                    e.putString("pid",i.getPostid());
                    e.commit();
                    Intent nextScreen = new Intent(cont.getApplicationContext(),CommentActivity.class);
                    //nextScreen.putExtra("id",auth.getString("data"));
                    nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    cont.startActivity(nextScreen);
                }
            });

            if (textV != null) {
                t1.setText("Posted By "+i.getUid());
                textV.setText(i.getPost_content());
            }
            if (imgV != null) {
                if (i.getImg() != null) {
                    System.out.println("setting image");
                    imgV.setVisibility(View.VISIBLE);
                    byte[] decodedImg = Base64.decode(i.getImg(), Base64.DEFAULT);
                    Bitmap bmpImg = BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length);
                    imgV.setImageBitmap(bmpImg);
                }
                else {
                    imgV.setVisibility(View.GONE);
                }
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
