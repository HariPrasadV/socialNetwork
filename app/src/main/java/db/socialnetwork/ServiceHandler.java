package db.socialnetwork;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by jeyasoorya on 9/10/17.
 */

public class ServiceHandler {


    public HttpURLConnection PostMethod(JSONObject params,URL site)throws Exception{
        HttpURLConnection urlConnection = (HttpURLConnection) site.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        OutputStream out = urlConnection.getOutputStream();
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
        writer.write(getPostDataString(params));
        writer.flush();
        writer.close();
        out.close();
        return urlConnection;
    }


    public String authorizationCall(String url, String uname, String pwd)throws Exception{
        JSONObject params=new JSONObject();
        params.put("id",uname);
        params.put("password",pwd);
        URL site = new URL(url);
        return makeServiceCall(PostMethod(params,site));
    }

    public String createPost(String url, String content, String img)throws Exception{
        JSONObject params=new JSONObject();
        if (content != null)
            params.put("content", content);
        if (img != null)
            params.put("img", img);
        URL site = new URL(url);
        return makeServiceCall(PostMethod(params,site));
    }

    public String addComment(String url,String pid,String content)throws Exception{
        JSONObject params=new JSONObject();
        params.put("postid",pid);
        params.put("content",content);
        URL site = new URL(url);
        return makeServiceCall(PostMethod(params,site));
    }

    public String seePosts(String url,int offset,int limit)throws Exception{
        JSONObject params = new JSONObject();
        params.put("offset",offset);
        params.put("limit",limit);
        URL site = new URL(url);
        return makeServiceCall(PostMethod(params,site));
    }

    public String SearchForUserFollowUnFollow(String url,String searchTerm)throws Exception{
        URL site = new URL(url);
        JSONObject params = new JSONObject();
        params.put("uid",searchTerm);
        return makeServiceCall(PostMethod(params,site));
    }

    public String seeUserPosts(String url,String uid2,int offset,int limit)throws Exception{
        URL site = new URL(url);
        JSONObject params = new JSONObject();
        params.put("uid",uid2);
        params.put("offset",offset);
        params.put("limit",limit);
        return makeServiceCall(PostMethod(params,site));
    }

    public String logout(String url)throws Exception{
        URL site = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) site.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        return makeServiceCall(urlConnection);
    }

    public String makeServiceCall(HttpURLConnection urlConnection)throws Exception{
        urlConnection.connect();
        int response = urlConnection.getResponseCode();
        if(response== HttpURLConnection.HTTP_OK){
            BufferedReader in=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer sb=new StringBuffer("");
            String line="";
            while((line=in.readLine())!=null){
                sb.append(line);
                break;
            }
            in.close();
            urlConnection.disconnect();
            return sb.toString();
        }
        else{
            return new String("Connection Error");
        }
    }

    public String getPostDataString(JSONObject params)throws Exception{
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key=itr.next();
            Object value=params.get(key);
            if(first)
                first=false;
            else
                sb.append("&");
            sb.append(URLEncoder.encode(key,"UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(value.toString(),"UTF-8"));
        }
        return sb.toString();
    }
}
