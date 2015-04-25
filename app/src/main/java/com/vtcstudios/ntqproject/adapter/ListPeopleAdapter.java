package com.vtcstudios.ntqproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtcstudios.ntqproject.R;
import com.vtcstudios.ntqproject.View.RecyclingImageView;
import com.vtcstudios.ntqproject.util.ImageFetcher;
import com.vtcstudios.ntqproject.util.People;

import java.util.ArrayList;

/**
 * Created by HIEPKHACH9X on 3/18/2015.
 */
public class ListPeopleAdapter extends BaseAdapter {
    private Context mCtx;
    private ArrayList<People> mLstPeople;
    private ImageFetcher mImageFetcher;

    public ListPeopleAdapter(Context ctx, ImageFetcher imageFetcher, ArrayList<People> peoples) {
        this.mCtx = ctx;
        mLstPeople = peoples;
        this.mImageFetcher = imageFetcher;
    }

    @Override
    public int getCount() {
        return mLstPeople.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mLstPeople.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder mHoder;
        if (convertView == null) {
            mHoder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_list_meetpeople,
                    null);
            mHoder.mItem_list_connection_common_avatar = (RecyclingImageView) convertView.findViewById(R.id.item_list_connection_common_img_avatar);
            mHoder.mItem_list_connection_common_status = (ImageView) convertView.findViewById(R.id.item_list_connection_common_img_status);
            mHoder.mItem_list_connection_common_notification = (TextView) convertView.findViewById(R.id.item_list_connection_common_txt_notification);
            mHoder.mItem_list_connection_common_name = (TextView) convertView.findViewById(R.id.item_list_connection_common_txt_name);
            mHoder.mItem_list_connection_common_age_gender = (TextView) convertView.findViewById(R.id.item_list_connection_common_txt_age_gender);
            mHoder.mItem_list_connection_common_location = (TextView) convertView.findViewById(R.id.item_list_connection_common_txt_location);
            mHoder.mItem_list_connection_common_time = (TextView) convertView.findViewById(R.id.item_list_connection_common_txt_time);
            mHoder.mItem_list_connection_common_txt_status = (TextView) convertView.findViewById(R.id.item_list_connection_common_txt_status);
            convertView.setTag(mHoder);
        } else mHoder = (ViewHolder) convertView.getTag();
        mHoder.mItem_list_connection_common_name.setText(mLstPeople.get(position).getmTextName());
        if (mLstPeople.get(position).getmNotifications() > 0) {
            mHoder.mItem_list_connection_common_notification.setVisibility(View.VISIBLE);
            mHoder.mItem_list_connection_common_notification.setText("" + mLstPeople.get(position).getmNotifications());
        } else {
            mHoder.mItem_list_connection_common_notification.setVisibility(View.GONE);
        }
        if (mLstPeople.get(position).ismIsOnline()) {
            mHoder.mItem_list_connection_common_status.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_status_online_grid_view));
            mHoder.mItem_list_connection_common_time.setText(mCtx.getString(R.string.online));
            mHoder.mItem_list_connection_common_time.setTextColor(mCtx.getResources().getColor(R.color.blue));


        } else {
            mHoder.mItem_list_connection_common_status.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_status_offline_grid_view));
            mHoder.mItem_list_connection_common_time.setText(mCtx.getString(R.string.offline));
            mHoder.mItem_list_connection_common_time.setTextColor(mCtx.getResources().getColor(R.color.white_dark));
        }
        if (mLstPeople.get(position).getmGender() == 0) {
            mImageFetcher.setLoadingImage(R.drawable.default_male_bg);
            mHoder.mItem_list_connection_common_age_gender.setText(mLstPeople.get(position).getmAge() + "yo " + mCtx.getResources().getString(R.string.male));
        } else if (mLstPeople.get(position).getmGender() == 1) {
            mImageFetcher.setLoadingImage(R.drawable.default_female_bg);
            mHoder.mItem_list_connection_common_age_gender.setText(mLstPeople.get(position).getmAge() + "yo " + mCtx.getResources().getString(R.string.female));
        } else {
            mImageFetcher.setLoadingImage(R.drawable.default_avata);
        }
        mImageFetcher.loadImage(mLstPeople.get(position).getmAvatar(), mHoder.mItem_list_connection_common_avatar);
        mHoder.mItem_list_connection_common_txt_status.setText(mLstPeople.get(position).getmStatus());
        mHoder.mItem_list_connection_common_location.setText("" + mLstPeople.get(position).getmDistance() + "mi");
        return convertView;
    }

    static class ViewHolder {
        public RecyclingImageView mItem_list_connection_common_avatar;
        public ImageView mItem_list_connection_common_status;
        public TextView mItem_list_connection_common_notification, mItem_list_connection_common_name,
                mItem_list_connection_common_age_gender, mItem_list_connection_common_location,
                mItem_list_connection_common_time, mItem_list_connection_common_txt_status;
    }
}
