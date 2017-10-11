package db.socialnetwork;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by jeyasoorya on 11/10/17.
 */

public class CustomAutoComplete extends AutoCompleteTextView {
    public CustomAutoComplete(Context context){
        super(context);
    }

    public CustomAutoComplete(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public CustomAutoComplete(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        super.performFiltering("", keyCode);
    }
}
