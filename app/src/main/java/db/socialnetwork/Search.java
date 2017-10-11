package db.socialnetwork;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {

    private String[] Countries = new String[] {"Belgium","Belfrost","Belfamilia","Belfamily" ,"France", "Italy", "Germany", "Spain"};
    public UserAdapter suggestions;
    public CustomAutoComplete ac;

    public Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ArrayList<SiteUser> users = new ArrayList<>();

        users.add(new SiteUser("00128","zac","abc@gmail.com"));
        users.add(new SiteUser("12345","ilo","bad@gmail.com"));
        users.add(new SiteUser("54321","bil","add@gmail.com"));
        users.add(new SiteUser("45678","pil","zod@gmail.com"));
        users.add(new SiteUser("12346","pac","god@gmail.com"));
        users.add(new SiteUser("23517","mac","vad@gmail.com"));
        users.add(new SiteUser("78901","dre","vro@gmail.com"));
        users.add(new SiteUser("09812","cre","ero@gmail.com"));
        users.add(new SiteUser("12689","dac","opo@gmail.com"));
        users.add(new SiteUser("21221","cac","apa@gmail.com"));

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ac = (CustomAutoComplete) rootView.findViewById(R.id.comp);

        ac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = "";
                TextView t1=(TextView)view.findViewById(R.id.userid);
                text += t1.getText()+",";
                t1=(TextView)view.findViewById(R.id.username);
                text+=t1.getText()+",";
                t1=(TextView)view.findViewById(R.id.useremail);
                text+=t1.getText();
                ac.setText(text);
            }
        });

        ac.addTextChangedListener(new CustomTextChangedListener(this,getActivity()));

        suggestions = new UserAdapter(getActivity(),users);

        ac.setAdapter(suggestions);

        return rootView;
    }

}
