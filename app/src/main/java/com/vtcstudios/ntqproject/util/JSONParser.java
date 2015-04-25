package com.vtcstudios.ntqproject.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url, JSONObject mJson) throws ClientProtocolException, IOException, JSONException {
        // Error UnsupportedEncodingException,ClientProtocolException,IOException
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(mJson.toString());
        //sets the post request as the resulting string
        httpPost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        is = httpEntity.getContent();
        // Error Exception converting result
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, "utf-8"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        json = sb.toString();

        // try parse the string to a JSON object
        jObj = new JSONObject(json);
        // return JSON String
        return jObj;

    }
}
