package db.socialnetwork;

/**
 * Created by jeyasoorya on 11/10/17.
 */

public class SiteUser {
    private String uid,name,email;

    public SiteUser(){
        uid="";
        name="";
        email="";
    }

    public SiteUser(String id,String Name,String Email){
        uid = id;
        name = Name;
        email = Email;
    }

    public String getUid(){
        return uid;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }
}
