package com.vtcstudios.ntqproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vtcstudios.ntqproject.Task.CallAPIfinishListener;
import com.vtcstudios.ntqproject.Task.MeetPeopleTask;
import com.vtcstudios.ntqproject.adapter.GridPeopleAdapter;
import com.vtcstudios.ntqproject.adapter.ListPeopleAdapter;
import com.vtcstudios.ntqproject.util.ImageCache;
import com.vtcstudios.ntqproject.util.ImageFetcher;
import com.vtcstudios.ntqproject.util.People;
import com.vtcstudios.ntqproject.util.Utils;
import com.vtcstudios.ntqproject.util.Variables;

import java.util.ArrayList;
import java.util.Objects;

public class GridPeopleFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, LocationListener, CallAPIfinishListener {

    private static final String sTAG = "MeetPeopleFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private static final int sMINTIME = 1000 * 60 * 1;
    private static final int sMINDISTANCE = 10;
    TextView mViewSwitcher;
    GridView mGridView;
    ImageButton frag_switcher_expanded;
    View collection_button_meet_people;
    ListView mListView;
    private int mImageThumbSize;
    private GridPeopleAdapter mgGridPeopleAdapter;
    private ListPeopleAdapter mListPeopleAdapter;
    private ImageFetcher mImageFetcher;
    private ArrayList<People> mLstPeoples = new ArrayList<>();
    boolean isGrid = true, isCollection = true;
    ProgressDialog pDialog;
    LocationManager mlocationMangaer;
    boolean isSearch = false;
    String mToken = "";

    public GridPeopleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mToken = getArguments().getString(Variables.TOKEN);
        }
        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.default_avata);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

        pDialog = new ProgressDialog(getActivity());

        pDialog.setTitle(R.string.shake_to_chat_footer_message_searching);
        pDialog.setMessage(getResources().getString(R.string.get_location_address));
        pDialog.setCanceledOnTouchOutside(false);
        mlocationMangaer = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_meet_people, container, false);

        isSearch = false;
        frag_switcher_expanded = (ImageButton) v.findViewById(R.id.frag_switcher_expanded);
        frag_switcher_expanded.setOnClickListener(this);
        collection_button_meet_people = (View) v.findViewById(R.id.collection_button_meet_people);
        mViewSwitcher =
                (TextView) v.findViewById(R.id.collection_button_meetpeople_txt_sort);
        mGridView = (GridView) v.findViewById(R.id.fragment_meetpeople_grid_people);
        mListView = (ListView) v.findViewById(R.id.fragment_meetpeople_list_people);
        mViewSwitcher.setOnClickListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    if (!Utils.hasHoneycomb()) {
                        mImageFetcher.setPauseWork(true);
                    }
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
        if (savedInstanceState != null) {
            isSearch = savedInstanceState.getBoolean(Variables.ISSEARCH, false);
            isGrid = !savedInstanceState.getBoolean(Variables.ISGRID, false);
            ChangeSortMeetPeople();
            isCollection = !savedInstanceState.getBoolean(Variables.ISCOLECTION, false);
            fragCollection();
            mLstPeoples = savedInstanceState.getParcelableArrayList(Variables.ADAPTER);
            mgGridPeopleAdapter = new GridPeopleAdapter(getActivity(), mImageFetcher, mLstPeoples);
            mListPeopleAdapter = new ListPeopleAdapter(getActivity(), mImageFetcher, mLstPeoples);
            mGridView.setAdapter(mgGridPeopleAdapter);
            mListView.setAdapter(mListPeopleAdapter);
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isSearch)
            GetLocation();

    }

    private void GetLocation() {
        if (Variables.displayGpsStatus(getActivity())) {
            pDialog.show();
            if (mlocationMangaer.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                mlocationMangaer.requestLocationUpdates(LocationManager
                        .NETWORK_PROVIDER, sMINTIME, sMINDISTANCE, this);
            } else
                mlocationMangaer.requestLocationUpdates(LocationManager
                        .GPS_PROVIDER, sMINTIME, sMINDISTANCE, this);
        } else {
            ShowAlertBox(getString(R.string.notification_title), getString(R.string.chat_share_location_undefined_location_message));
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected void ShowAlertBox(String strTitle, String strMess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(strMess)
                .setCancelable(false)
                .setTitle(strTitle)
                .setPositiveButton(getString(R.string.settings),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(R.string.common_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        if (isSearch) {
            mgGridPeopleAdapter.notifyDataSetChanged();
            mListPeopleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Variables.ISSEARCH, isSearch);
        outState.putParcelableArrayList(Variables.ADAPTER, mLstPeoples);
        outState.putBoolean(Variables.ISGRID, isGrid);
        outState.putBoolean(Variables.ISCOLECTION, isCollection);
    }

    @Override
    public void onLocationChanged(Location location) {
        pDialog.setMessage(getResources().getString(R.string.shake_to_chat_footer_message_searching));
        new MeetPeopleTask(mToken, location.getLongitude(), location.getLatitude(), this).execute();
        if (Variables.ISLOG) Log.e(sTAG, "Longitude: " + location.getLongitude());
        if (Variables.ISLOG) Log.e(sTAG, "Latitude: " + location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (!pDialog.isShowing()) pDialog.cancel();
        if (!isSearch)
            GetLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {
        pDialog.cancel();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    }

    @Override
    public void onLoadfinish(String Result) {

    }

    @Override
    public void onLoadPeoplefinish(int Code, ArrayList<People> Result) {
        if (pDialog != null) pDialog.dismiss();
        mLstPeoples = Result;
        isSearch = true;
        switch (Code) {
            case 0:
                if (Result == null || Result.size() <= 0) {
                    Toast.makeText(getActivity(), R.string.meetpeople_not_found_by_search, Toast.LENGTH_SHORT).show();
                } else {
                    mgGridPeopleAdapter = new GridPeopleAdapter(getActivity(), mImageFetcher, Result);
                    mListPeopleAdapter = new ListPeopleAdapter(getActivity(), mImageFetcher, Result);
                    mGridView.setAdapter(mgGridPeopleAdapter);
                    mListView.setAdapter(mListPeopleAdapter);

                }
                break;
            case 1:
                Toast.makeText(getActivity(), R.string.msg_common_server_unknown_error, Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getActivity(), R.string.msg_common_data_format_wrong, Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getActivity(), R.string.msg_common_invalid_token, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), R.string.msg_common_server_unknown_error, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onErrorCall(String Result) {

    }

    private void ChangeSortMeetPeople() {
        if (isGrid) {
            mGridView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mViewSwitcher.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pannel_grid, 0, 0);
            mViewSwitcher.setText(getActivity().getResources().getString(R.string.grid_view));
            isGrid = false;
        } else {
            mGridView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mViewSwitcher.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pannel_list, 0, 0);
            mViewSwitcher.setText(getActivity().getResources().getString(R.string.list_view));
            isGrid = true;
        }
    }

    private void fragCollection() {
        if (isCollection) {
            collection_button_meet_people.setVisibility(View.GONE);
            frag_switcher_expanded.setImageResource(R.drawable.top_switcher_collapsed_background);
            isCollection = false;
        } else {
            collection_button_meet_people.setVisibility(View.VISIBLE);
            frag_switcher_expanded.setImageResource(R.drawable.top_switcher_expanded_background);
            isCollection = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection_button_meetpeople_txt_sort:
                ChangeSortMeetPeople();
                break;
            case R.id.frag_switcher_expanded:
                fragCollection();
                break;
            default:
                break;
        }
    }
}
