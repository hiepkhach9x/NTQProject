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
 * Created by HIEPKHACH9X on 3/17/2015.
 */
public class GridPeopleAdapter extends BaseAdapter {
    Context mCtx;
    ArrayList<People> mLstPeople;
    private ImageFetcher mImageFetcher;

    public GridPeopleAdapter(Context ctx, ImageFetcher imageFetcher, ArrayList<People> lstPeople) {
        mCtx = ctx;
        mLstPeople = lstPeople;
        mImageFetcher = imageFetcher;
    }

    @Override
    public int getCount() {
        return mLstPeople.size();
    }

    @Override
    public Object getItem(int position) {
        return mLstPeople.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mItem = convertView;
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_grid_meetpeople,
                    null);
            holder = new ViewHolder();
            holder.mItem_grid_meet_people_avatar = (RecyclingImageView) convertView
                    .findViewById(R.id.item_grid_meetpeople_img_avatar);
            holder.mItem_grid_meet_people_status = (ImageView) convertView.findViewById(R.id.item_grid_meetpeople_img_status);
            holder.item_grid_meet_people_message = (ImageView) convertView.findViewById(R.id.item_grid_meetpeople_img_message);
            holder.item_grid_meet_people_notification = (TextView) convertView.findViewById(R.id.item_grid_meetpeople_txt_notification);
            holder.item_grid_meet_people_name = (TextView) convertView.findViewById(R.id.item_grid_meetpeople_txt_name);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.item_grid_meet_people_name.setText(mLstPeople.get(position).getmTextName());
        if (mLstPeople.get(position).getmNotifications() > 0) {
            holder.item_grid_meet_people_notification.setText("" + mLstPeople.get(position).getmNotifications());
            holder.item_grid_meet_people_message.setVisibility(View.VISIBLE);
        } else {
            holder.item_grid_meet_people_message.setVisibility(View.INVISIBLE);
            holder.item_grid_meet_people_notification.setVisibility(View.INVISIBLE);
        }
        if (mLstPeople.get(position).ismIsOnline()) {
            holder.mItem_grid_meet_people_status.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_status_online_grid_view));
        } else {
            holder.mItem_grid_meet_people_status.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_status_offline_grid_view));
        }
        if (mLstPeople.get(position).getmGender() == 0)
            mImageFetcher.setLoadingImage(R.drawable.default_male_bg);
        else if (mLstPeople.get(position).getmGender() == 1)
            mImageFetcher.setLoadingImage(R.drawable.default_female_bg);
        else mImageFetcher.setLoadingImage(R.drawable.default_avata);
        mImageFetcher.loadImage(mLstPeople.get(position).getmAvatar(), holder.mItem_grid_meet_people_avatar);

        return convertView;
    }

    static class ViewHolder {
        public RecyclingImageView mItem_grid_meet_people_avatar;
        public ImageView mItem_grid_meet_people_status, item_grid_meet_people_message;
        public TextView item_grid_meet_people_notification, item_grid_meet_people_name;
    }

}
