package com.swaas.hd.mto;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swaas.hd.mto.RetrofitBuilder.NavigationMenuModel;
import com.swaas.hd.mto.RetrofitBuilder.NetworkAPIClass;
import com.swaas.hd.mto.Utils.NetworkUtils;
import com.swaas.hd.mto.Utils.PreferenceUtils;

import java.util.List;

public class HomeScreenActivity extends RootActivity {


    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    public static int navItemIndex = 0;

    private static final String TAG_SCHEME = "SCHEME";
    private static final String TAG_DOCTOR_TAGGING = "DOCTOR_TAGGING";
    private static final String TAG_ORDER_CREATION = "ORDER_CREATION";
    private static final String TAG_ORDER_PROCESSING = "0RDER_PROCESSING";
    private static final String TAG_ORDER_MAINTENANCE = "ORDER_MAINTENANCE";
    private static final String TAG_REPORT_MASTER = "REPORTS_MASTER";
    private static final String TAG_LOG_OUT = "LOG_OUT";
    public static String CURRENT_TAG = TAG_SCHEME;
    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    SwipeRefreshLayout mSwipeRefreshLayout;
    String selectedItem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
      //  getNavigationMenuFromAPI();

        //String[] listItems = {"one", "two", "Three"};

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        //activityTitles = listItems;
        loadNavHeader();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_SCHEME;
            loadHomeFragment();
        }

        /*mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getNavigationMenuFromAPI();
            }
        });*/
    }

    private void loadNavHeader() {
        txtName.setText("HiDoctor");
        Glide.with(this).load(PreferenceUtils.getCompanyLogoFilePath(this))
                .into(imgNavHeaderBg);

    }

    private void loadHomeFragment() {
        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                Fragment fragment = getHomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("key", selectedItem);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
           /* case 0:
                HomeFragment homeFragment1 = new HomeFragment();
                selectedItem = "Scheme Creation";
                return homeFragment1;
            case 1:
                HomeFragment homeFragment2 = new HomeFragment();
                selectedItem = "Doctor Tagging";
                return homeFragment2;
            case 2:
                HomeFragment homeFragment3 = new HomeFragment();
                selectedItem = "Order Creation";
                return homeFragment3;
            case 3:
                HomeFragment homeFragment4 = new HomeFragment();
                selectedItem = "Order Processing";
                return homeFragment4;
            case 4:
                HomeFragment homeFragment5 = new HomeFragment();
                selectedItem = "Order Maintenance";
                return homeFragment5;
            case 5:
                HomeFragment homeFragment6 = new HomeFragment();
                selectedItem = "Reports/Masters";
                return homeFragment6;
            default:
                return new HomeFragment();*/
           default:
               return null;
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    /*case R.id.scheme_creation:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_SCHEME;
                        break;
                    case R.id.doctor_tagging:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_DOCTOR_TAGGING;
                        break;
                    case R.id.order_creation:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ORDER_CREATION;
                        break;
                    case R.id.order_processing:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_ORDER_PROCESSING;
                        break;
                    case R.id.order_maintenance:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ORDER_MAINTENANCE;
                        break;

                    case R.id.report_master:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_REPORT_MASTER;
                        break;
                    case R.id.log_out:
                        Intent intent = new Intent(HomeScreenActivity.this, LogOutActivity.class);
                        startActivity(intent);*/
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_SCHEME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    /*private void getNavigationMenuFromAPI() {
        if (NetworkUtils.isNetworkAvailable(HomeScreenActivity.this)) {

            NetworkAPIClass networkAPIClass = new NetworkAPIClass(this);
            showProgressDialog(getResources().getString(R.string.loading));
            networkAPIClass.setListener(new NetworkAPIClass.NavigationMenuAPICallBackListener() {
                @Override
                public void getNavigationMenuAPISuccessCB(List<NavigationMenuModel> navigationMenuModels) {
                    hideProgressDialog();
                    mSwipeRefreshLayout.setRefreshing(false);
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_SCHEME;
                    loadHomeFragment();
                    if (navigationMenuModels.size() > 0 && navigationMenuModels != null) {

                    }
                }

                @Override
                public void getNavigationMenuAPIFailureCB(String message) {
                    hideProgressDialog();
                    mSwipeRefreshLayout.setRefreshing(false);
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_SCHEME;
                    loadHomeFragment();

                }
            });

            networkAPIClass.getNavigationMenuInfo();
        }

    }*/

}
