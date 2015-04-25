package com.vtcstudios.ntqproject.Task;

import android.util.Log;

import com.vtcstudios.ntqproject.Task.CallAPIfinishListener;
import com.vtcstudios.ntqproject.util.AsyncTask;
import com.vtcstudios.ntqproject.util.JSONParser;
import com.vtcstudios.ntqproject.util.Variables;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by HIEPKHACH9X on 3/18/2015.
 */
public class RegisterTask extends AsyncTask<Void, Void, String> {
    private String mEmail, mPassword;
    private static String sRegisterName = "register_version_2";
    private String mUserName = "HungHN";
    private String mBir = "19920403";
    //int: 0: Male | 1: Female | 2: Both
    private int mInters = 1;
    private String mDevice_id = "1081020024";
    private String mNotify_token = "abc-xyz";
    //int : 0 : IOS | 1: Android
    private int mDevice_type = 1;
    //    String: yyyyMMddHHmmss : local time
    private String mLogin_time = "20150301164529";
    //    String: (Optional) use when invite friend
    private String mIvt_code = "hk";
    private String mOriginal_pwd = "123456";
    //    int: 0: Male | 1: Female
    private int mGender = 1;
    //
    private static final String TAG_CODE = "code";
    private static final String TAG_TOKEN = "token";
    private static final String TAG_DATA = "data";
    private CallAPIfinishListener mLs;
    JSONParser parser;
    JSONObject mData;


    public RegisterTask(String strMail, String strPass, CallAPIfinishListener ls) {
        mEmail = strMail;
        mPassword = Variables.convertPassMd5(strPass);
        mOriginal_pwd = strPass;
        mLs = ls;
        parser = new JSONParser();
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        JSONObject mJson = new JSONObject();
        try {
            mJson.put("api", sRegisterName);
            mJson.put("email", mEmail);
            mJson.put("pwd", mPassword);
            mJson.put("user_name", mUserName);
            mJson.put("bir", mBir);
            mJson.put("inters_in", mInters);
            mJson.put("device_id", mDevice_id);
            mJson.put("notify_token", mNotify_token);
            mJson.put("device_type", mDevice_type);
            mJson.put("login_time", mLogin_time);
            mJson.put("ivt_code", mIvt_code);
            mJson.put("original_pwd", mOriginal_pwd);
            mJson.put("gender", mGender);
            if (Variables.ISLOG)
                Log.e("ObjectJson", mJson.toString());
            JSONObject json = parser.getJSONFromUrl(Variables.sUrlAPI, mJson);
            if (Variables.ISLOG)
                Log.e("RESULT JSON", json.toString());
            int code = json.getInt(TAG_CODE);
            String mToken = "";
            if (code == 0) {
                mData = json.getJSONObject(TAG_DATA);
                mToken = mData.getString(TAG_TOKEN);
            }
            result = "" + code + ":" + mToken;
        } catch (JSONException eJP) {
            eJP.printStackTrace();
            result = Variables.ERROR_PARSER_JSON + ":";
        } catch (ClientProtocolException eClien) {
            eClien.printStackTrace();
            result = Variables.ERROR_INTERNET + ":";
        } catch (IOException eIO) {
            eIO.printStackTrace();
            result = Variables.ERROR_CONV_RESULT + ":";
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Variables.ERROR_EX + ":";
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        mLs.onLoadfinish(s);
    }
}
