package db.socialnetwork;

import java.util.ArrayList;

/**
 * Created by jeyasoorya on 12/10/17.
 */

public class Posts {
    String uid,postid;
    String post_content;
    ArrayList<Comments> post_comments;
    Posts(String uid_, String cont_, String pid,ArrayList<Comments> comments_){
        uid = uid_;
        post_content = cont_;
        postid=pid;
        post_comments = comments_;
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
