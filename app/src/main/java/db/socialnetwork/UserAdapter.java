package db.socialnetwork;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jeyasoorya on 11/10/17.
 */

public class UserAdapter extends ArrayAdapter {

    public UserAdapter(Context c, List<SiteUser> l){
        super(c,0,l);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View list_view = convertView;
        if(list_view == null){
            list_view = LayoutInflater.from(getContext()).inflate(R.layout.for_autocomplete,parent,false);
        }
        SiteUser currUser = (SiteUser) getItem(position);

        TextView t1 = (TextView)list_view.findViewById(R.id.userid);
        t1.setText(currUser.getUid());

        t1 = (TextView)list_view.findViewById(R.id.username);
        t1.setText(currUser.getName());

        t1= (TextView)list_view.findViewById(R.id.useremail);
        t1.setText(currUser.getEmail());

        return list_view;
    }
}
