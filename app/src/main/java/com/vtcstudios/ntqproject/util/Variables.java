package com.vtcstudios.ntqproject.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;

import com.vtcstudios.ntqproject.R;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by HIEPKHACH9X on 3/18/2015.
 */
public class Variables {
    public static String sUrlAPI = "http://ntq.ddns.net:9119/api=";
    public static final String TOKEN = "TOKEN";
    public static final String ADAPTER = "ADAPTER";
    public static final String ISSEARCH = "ISSEARCH";
    public static final String ISGRID = "ISGRID";
    public static final String ISCOLECTION = "ISCOLECTION";
    public static final boolean ISLOG = true;
    // Lỗi PARSER APT
    public static final int ERROR_PARSER_JSON = -1;
    public static final int ERROR_INTERNET = -2;
    public static final int ERROR_CONV_RESULT = -3;
    public static final int ERROR_EX = -4;

    public static Boolean displayGpsStatus(Context ctx) {
        ContentResolver contentResolver = ctx
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;
        } else {
            return false;
        }
    }

    public static Dialog sCreateDialogOK(Context ctx, String strTitle, String strMess) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        // Set the dialog title
        builder.setTitle(strTitle)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMessage(strMess)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    public static boolean checkNetworkStatus(Context ctx) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected()) {
            // notify user you are online
            return true;
        } else {
            // notify user you are not online
            return false;

        }

    }

    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }

    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
