package com.vtcstudios.ntqproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

public class MeetPeopleActivity extends FragmentActivity {
    private static final String TAG = "MeetPeopleActivity";
    TextView mCv_navigation_bar_text_center;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        mCv_navigation_bar_text_center = (TextView) findViewById(R.id.cv_navigation_bar_text_center);
        mCv_navigation_bar_text_center.setText(getString(R.string.meet_people));
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            GridPeopleFragment mPeoplefragment = new GridPeopleFragment();
            mPeoplefragment.setArguments(getIntent().getExtras());
            ft.add(R.id.mainSearchFriends, mPeoplefragment, TAG);
            ft.commit();
        }
    }
}
