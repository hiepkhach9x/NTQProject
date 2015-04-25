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
import com.vtcstudios.ntqproject.Task.LoginTask;
import com.vtcstudios.ntqproject.util.People;
import com.vtcstudios.ntqproject.util.Variables;

import java.util.ArrayList;
import java.util.Objects;


public class LoginActivity extends Activity implements View.OnClickListener, CallAPIfinishListener {
    Button mLogin;
    ImageButton mBtnNavMess;
    EditText mTextLoginEmail, mTextLoginPass;
    TextView mTextNoti, mTextForgot;
    private ProgressDialog mPdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLogin = (Button) findViewById(R.id.mLogin);
        mLogin.setOnClickListener(this);
        mTextLoginEmail = (EditText) findViewById(R.id.mLogin_text_email);
        mTextLoginPass = (EditText) findViewById(R.id.mLogin_text_pass);
        mTextNoti = (TextView) findViewById(R.id.cv_navigation_bar_txt_notification);
        mTextNoti.setVisibility(View.GONE);
        mTextForgot = (TextView) findViewById(R.id.cv_navigation_bar_tv_right);
        mTextForgot.setVisibility(View.VISIBLE);
        mBtnNavMess = (ImageButton) findViewById(R.id.cv_navigation_bar_btn_right);
        mBtnNavMess.setVisibility(View.GONE);
        mPdialog = new ProgressDialog(this);
        mPdialog.setTitle(R.string.notification_title);
        mPdialog.setMessage(getString(R.string.common_logging_in));
        mPdialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mLogin:
                if (Variables.checkNetworkStatus(this)) {
                    if (mTextLoginEmail.getText().toString().equals("")) {
                        Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.email_is_empty)).show();
                    } else if (mTextLoginPass.getText().toString().equals("")) {
                        Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.password_length_must_than)).show();
                    } else {
                        mPdialog.show();
                        new LoginTask(mTextLoginEmail.getText().toString(), mTextLoginPass.getText().toString(), this).execute();
                    }
                } else
                    Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.no_internet)).show();
                break;
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
            case 10:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.msg_common_email_not_found)).show();
                break;
            case 20:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.msg_common_password_is_incorrect)).show();
                break;
            case 81:
                Variables.sCreateDialogOK(this, getString(R.string.login_failed_dialog_title), getString(R.string.user_blocked)).show();
                break;
            default:
                if (Variables.ISLOG) Log.e("LoginActivity", "" + Result);
                break;
        }
    }

    @Override
    public void onErrorCall(String Result) {

    }

    @Override
    public void onLoadPeoplefinish(int Code, ArrayList<People> Result) {

    }
}
