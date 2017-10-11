package db.socialnetwork;

/**
 * Created by jeyasoorya on 12/10/17.
 */

public class Comments {

    String uid;
    String comment;

    Comments(String uid_,String comment_){
        uid = uid_;
        comment = comment_;
    }

    String getUid(){
        return uid;
    }

    String getComment(){
        return comment;
    }
}
