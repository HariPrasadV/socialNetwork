package db.socialnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

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
