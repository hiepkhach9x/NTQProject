package com.vtcstudios.ntqproject.Task;

import android.util.Log;

import com.vtcstudios.ntqproject.util.AsyncTask;
import com.vtcstudios.ntqproject.util.JSONParser;
import com.vtcstudios.ntqproject.util.People;
import com.vtcstudios.ntqproject.util.Variables;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by HIEPKHACH9X on 3/19/2015.
 */
public class MeetPeopleTask extends AsyncTask<Void, Void, ArrayList<People>> {
    public static final String sMeetPeople = "meet_people";
    Double mlog, mlat;
    CallAPIfinishListener mLs;
    JSONParser parser;
    String mToken;
    int ResultCode;
    JSONArray mData;
    private static final String TAG_CODE = "code";
    private static final String TAG_DATA = "data";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_USERNAME = "user_name";
    private static final String TAG_GNEDER = "gender";
    private static final String TAG_AGE = "age";
    private static final String TAG_AVATAR = "ava_id";
    private static final String TAG_STATUS = "status";
    private static final String TAG_UNREAD = "unread_num";
    private static final String TAG_ISONLINE = "is_online";
    private static final String TAG_LONG = "long";
    private static final String TAG_LAT = "lat";
    private static final String TAG_DIST = "dist";


    public MeetPeopleTask(String token, double log, double lat, CallAPIfinishListener ls) {
        mToken = token;
        mlog = log;
        mlat = lat;
        mLs = ls;
        parser = new JSONParser();
    }

    @Override
    protected ArrayList<People> doInBackground(Void... params) {
        ArrayList<People> mPeoples = new ArrayList<>();
        JSONObject mJson = new JSONObject();
        try {
            mJson.put("api", sMeetPeople);
            mJson.put("token", mToken);
//            mJson.put("token", "abc-xyz");
            mJson.put("show_me", 2);
            mJson.put("inters_in", 2);
            mJson.put("lower_age", 10);
            mJson.put("upper_age", 60);
            mJson.put("ethn", null);
            mJson.put("long", mlog);
            mJson.put("lat", mlat);
            mJson.put("distance", 4);
            mJson.put("skip", 1);
            mJson.put("take", 50);
            if (Variables.ISLOG)
                Log.e("ObjectJson", mJson.toString());
            JSONObject json = parser.getJSONFromUrl(Variables.sUrlAPI, mJson);
            if (Variables.ISLOG)
                Log.e("RESULT JSON", json.toString());
            ResultCode = json.getInt(TAG_CODE);
            mData = json.getJSONArray(TAG_DATA);
            for (int i = 0; i < mData.length(); i++) {
                JSONObject temp = mData.getJSONObject(i);
                String user_id = temp.getString(TAG_USERID);
                String user_name = temp.getString(TAG_USERNAME);
                int gender = temp.getInt(TAG_GNEDER);
                int age = temp.getInt(TAG_AGE);
                String ava_id = temp.optString(TAG_AVATAR, "");
                String status = temp.optString(TAG_STATUS, "");
                int unread_num = temp.optInt(TAG_UNREAD, 0);
                boolean is_online = temp.getBoolean(TAG_ISONLINE);
                double log = temp.getDouble(TAG_LONG);
                double lat = temp.getDouble(TAG_LAT);
                double dist = temp.getDouble(TAG_DIST);

                mPeoples.add(new People(user_name, gender, age, ava_id, status, unread_num, is_online, log, lat, dist));
//                mPeoples.add(new People("hiepkhach9x", 0, 24, "http://cdn.pigeonsandplanes.com/wp-content/uploads/2011/03/first-rate-people-gentlemens-club.jpg", "Xin chao bạn", 2, true, 105.78, 21.0165, 100));
            }
        } catch (JSONException eJP) {
            eJP.printStackTrace();
            ResultCode = Variables.ERROR_PARSER_JSON;
        } catch (ClientProtocolException eClien) {
            eClien.printStackTrace();
            ResultCode = Variables.ERROR_INTERNET;
        } catch (IOException eIO) {
            eIO.printStackTrace();
            ResultCode = Variables.ERROR_CONV_RESULT;
        } catch (Exception ex) {
            ex.printStackTrace();
            ResultCode = Variables.ERROR_EX;
        }
        return mPeoples;
    }

    @Override
    protected void onPostExecute(ArrayList<People> peoples) {
        mLs.onLoadPeoplefinish(ResultCode, peoples);
    }
}
