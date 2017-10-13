package db.socialnetwork;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jeyasoorya on 12/10/17.
 */

public class Posts {
    String uid,postid;
    String post_content;
    String base64_img;
    ArrayList<Comments> post_comments;
    Posts(String uid_, String cont_, String base64Img_, String pid, ArrayList<Comments> comments_){
        uid = uid_;
        post_content = cont_;
        postid = pid;
        post_comments = comments_;
        base64_img = base64Img_;
    }
    Posts(String content_, String img_) {
        post_content = content_;
        base64_img = img_;
    }
    String getImg(){
        return base64_img;
    }

    String getUid(){
        return uid;
    }

    String getPostid(){
        return postid;
    }

    String getPost_content(){
        return post_content;
    }

    ArrayList<Comments> getPost_comments(){
        return post_comments;
    }
}
