package com.vtcstudios.ntqproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vtcstudios.ntqproject.Task.CallAPIfinishListener;
import com.vtcstudios.ntqproject.util.People;
import com.vtcstudios.ntqproject.Task.RegisterTask;
import com.vtcstudios.ntqproject.util.Variables;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by HIEPKHACH9X on 3/17/2015.
 */
public class RegisterActivity extends Activity implements View.OnClickListener, CallAPIfinishListener {
    Button mSignUp;
    EditText mTextRegisterEmail, mTextRegisterPass;
    ImageButton mBtnNavMess;
    TextView mTextNoti, mTextForgot;
    ProgressDialog mPdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mSignUp = (Button) findViewById(R.id.mSign_Up);
        mSignUp.setOnClickListener(this);
        mTextRegisterEmail = (EditText) findViewById(R.id.mTextRegisterMail);
        mTextRegisterPass = (EditText) findViewById(R.id.mTextRegisterPass);

        mTextNoti = (TextView) findViewById(R.id.cv_navigation_bar_txt_notification);
        mTextNoti.setVisibility(View.GONE);
        mTextForgot = (TextView) findViewById(R.id.cv_navigation_bar_tv_right);
        mTextForgot.setVisibility(View.VISIBLE);
        mTextForgot.setText(R.string.login);
        mTextForgot.setOnClickListener(this);
        mBtnNavMess = (ImageButton) findViewById(R.id.cv_navigation_bar_btn_right);
        mBtnNavMess.setVisibility(View.GONE);

        mPdialog = new ProgressDialog(this);
        mPdialog.setTitle(R.string.notification_title);
        mPdialog.setMessage(getString(R.string.voip_not_register_sip_server_message));
        mPdialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSign_Up:
                if (Variables.checkNetworkStatus(this)) {
                    if (mTextRegisterEmail.getText().toString().equals("")) {
                        Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.email_is_empty)).show();
                    } else if (mTextRegisterPass.getText().toString().equals("")) {
                        Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.password_length_must_than)).show();
                    } else {
                        mPdialog.show();
                        new RegisterTask(mTextRegisterEmail.getText().toString(), mTextRegisterPass.getText().toString(), this).execute();
                    }
                } else {
                    Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.no_internet)).show();
                }
                break;
            case R.id.cv_navigation_bar_tv_right:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            default:
                break;
        }
    }

    @Override
    public void onLoadfinish(String Result) {
        if (mPdialog != null) mPdialog.dismiss();
        String kq[] = Result.split(":");
        int code;
        try {
            code = Integer.parseInt(kq[0]);
        } catch (Exception ex) {
            code = 9999;
        }
        switch (code) {
            case 0:
                Intent mIntent = new Intent(this, MeetPeopleActivity.class);
                mIntent.putExtra(Variables.TOKEN, kq[1]);
                startActivity(mIntent);
                finish();
                break;
            case 1:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.msg_common_server_unknown_error)).show();
                break;
            case 2:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.msg_common_data_format_wrong)).show();
                break;
            case 11:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.email_invalid_format)).show();
                break;
            case 12:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.msg_common_invalid_email)).show();
                break;
            case 14:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.msg_common_invalid_username)).show();
                break;
            case 21:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.password_invalid)).show();
                break;
            default:
                if (Variables.ISLOG) Log.e("RegisterActivity", "" + Result);
                break;
        }
    }

    @Override
    public void onErrorCall(String Result) {

    }

    @Override
    public void onLoadPeoplefinish(int code, ArrayList<People> Result) {

    }
}
