package com.swaas.hd.mto.ExpandableListItem;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.swaas.hd.mto.LogOutActivity;
import com.swaas.hd.mto.LoginActivity;
import com.swaas.hd.mto.R;
import com.swaas.hd.mto.RetrofitBuilder.NavigationMenuModel;
import com.swaas.hd.mto.RetrofitBuilder.NetworkAPIClass;
import com.swaas.hd.mto.RetrofitBuilder.UserAuthenticationModel;
import com.swaas.hd.mto.RetrofitBuilder.UserDetailsModel;
import com.swaas.hd.mto.RetrofitBuilder.UserTypeMenus;
import com.swaas.hd.mto.RootActivity;
import com.swaas.hd.mto.Utils.AppUtils;
import com.swaas.hd.mto.Utils.Constants;
import com.swaas.hd.mto.Utils.MTO_Application_Class;
import com.swaas.hd.mto.Utils.NetworkUtils;
import com.swaas.hd.mto.Utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends RootActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private List<MenuModel> headerList = new ArrayList<>();
    private HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView userName;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MenuModel menuModel = new MenuModel();
    private List<NavigationMenuModel> menuModelList = new ArrayList<>();
    private boolean schemeCreationAddition, doctorTaggingAddition, orderCreationAddition = false;
    public static Activity activity;
    boolean showDeleteDCRMenu;
    WebView webView;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    public static final int MY_LOCATION_REQUEST_PERMISSION = 101;
    private final static int FILECHOOSER_RESULTCODE = 1;
    GeolocationPermissions.Callback callbacklocation;
    public static final int REQUEST_PERMISSION_STORAGE_FOR_DCR_ATTACHMENTS = 995;
    String originlocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;
        webView = findViewById(R.id.webView);
        expandableListView = findViewById(R.id.expandableListView);
        mSwipeRefreshLayout = findViewById(R.id.layout_swipe_refresh);
        drawerLayout = findViewById(R.id.drawer_layout);
       // getNavigationMenuFromAPI();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        imgNavHeaderBg = navHeader.findViewById(R.id.img_header_bg);
        imgProfile = navHeader.findViewById(R.id.img_profile);
        userName = navHeader.findViewById(R.id.name);

        userName.setText(PreferenceUtils.getUserName(this) + "(" + PreferenceUtils.getUserTypeName(this) + ")");
        Glide.with(this).load(R.mipmap.ic_hidoc_logo)
                .into(imgProfile);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
               // getNavigationMenuFromAPI();
            }
        });
      //  ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_STORAGE_FOR_DCR_ATTACHMENTS);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 101);

        isStoragePermissionGranted();

        if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            showProgressDialog("Checking User Authentication...");
            CheckUserState();
        }else{
            NetworkUtils.requestNetworkPermission(MainActivity.this).show();
        }



        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                try{
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    request.setMimeType(GetFileExtension(url));
                    //------------------------COOKIE!!------------------------
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    //------------------------COOKIE!!------------------------
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file...");
                    //request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.setTitle(url.substring(url.lastIndexOf("/") + 1));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Application needs to access your storage. " +
                            "Kindly enable the storage access permission under HiDoctor App Info settings.", Toast.LENGTH_LONG).show();
                }

            }
        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
            {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }


            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
    }
    public String GetFileExtension(String urls) {
        String url = urls.toLowerCase();
        String type = "";
        if (url.lastIndexOf(".") != -1) {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(ext);
        } else {
            type = null;
        }
        return type;
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Application needs to access your storage. " +
                        "Kindly enable the storage access permission under HiDoctor App Info settings.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }

    }

    private void CheckUserState() {
        NetworkAPIClass networkAPIClass = new NetworkAPIClass(this);
        // showProgressDialog(getResources().getString(R.string.loading));
        networkAPIClass.setListener(new NetworkAPIClass.UserAuthenticationAPICallBackListener() {
            @Override
            public void getusetAuthenticationAPISuccessCB(List<UserDetailsModel> userDetailsModels) {
                if (userDetailsModels.size() > 0 && userDetailsModels != null) {
                    // hideProgressDialog();
                    getMenuAccess();
                } else {
                    //  hideProgressDialog();
                    hideProgressDialog();
                    Intent i=new Intent(MainActivity.this,LoginActivity.class);
                    logoutFunction();
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void getuserAuthenticationAPIFailureCB(String message) {
                Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                logoutFunction();
                startActivity(i);
                finish();
                // hideProgressDialog();

            }
        });


        UserAuthenticationModel userAuthenticationModel = new UserAuthenticationModel();
        userAuthenticationModel.setUserName(PreferenceUtils.getUserName(MainActivity.this));
        userAuthenticationModel.setPassword(PreferenceUtils.getUserPassword(MainActivity.this));
        userAuthenticationModel.setCompanyCode(PreferenceUtils.getCompanyCode(MainActivity.this));

        networkAPIClass.checkUserAuthentication(userAuthenticationModel);
    }



    void getMenuAccess(){

        NetworkAPIClass networkAPIClass = new NetworkAPIClass(this);
        networkAPIClass.setUserTypeMenusDB(new NetworkAPIClass.GetUserTypeMenusFromDB() {
            @Override
            public void getMenusFromDBSuccess(List<UserTypeMenus> menuses) {
                hideProgressDialog();
                for(UserTypeMenus userTypeMenus : menuses){
                    if(userTypeMenus.getMenu_Id() == Constants.DELETE_DCR_MENU){
                        showDeleteDCRMenu = true;
                        break;
                    }
                }
                prepareMenuData();
            }

            @Override
            public void getMenusFromDBFailure(String exception) {
                hideProgressDialog();
            }
        });

        networkAPIClass.getMenusFromWebService(PreferenceUtils.getCompanyCode(this),
                PreferenceUtils.getUserCode(this),
                PreferenceUtils.getRegionCode(this));


    }


    private  void  logoutFunction(){

        PreferenceUtils.setIsLoggedInStatus(MainActivity.this, false);
        MainActivity.activity.finish();
        Intent intent = new Intent(MainActivity.this, MTO_Application_Class.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Pop the browser back stack or exit the activity
            if (webView.canGoBack()) {
                webView.goBack();
            }else{
                super.onBackPressed();
            }

        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            headerList.clear();
            childList.clear();
           // getNavigationMenuFromAPI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void prepareMenuData() {

        List<MenuModel> schemeChildModelsList = new ArrayList<>();
        List<MenuModel> addDoctorTaggingChildModeList = new ArrayList<>();
        List<MenuModel> addCreateDoctorChildModeList = new ArrayList<>();
        boolean needToAddSchemeCreation = true;
        boolean needToAddDoctorTagging = true;
        boolean needToAddCreateOrder = true;




        /**
         * Menu Configuration Process
         */

        menuModel = new MenuModel("DCR", true, false, "");
        headerList.add(menuModel);
        menuModel = new MenuModel("Reports", true, false, "");
        headerList.add(menuModel);
        if(showDeleteDCRMenu){
            menuModel = new MenuModel("Delete DCR", true, false, "");
            headerList.add(menuModel);
        }
        menuModel = new MenuModel("Log Out", true, false, "");
        headerList.add(menuModel);

        populateExpandableList();

        MenuModel model = headerList.get(0);

        getSupportActionBar().setTitle(model.menuName);

        String domain= PreferenceUtils.getCompanyName(MainActivity.this);
        String Domain = domain.substring(0,domain.length()-2);
        String webURLData = "https://"+ Domain+"me/HiDoctor_Activity/MTO/MTOMobile?Lid=";
        StringBuilder mUrlStringBuilder = new StringBuilder();
        StringBuilder mUrlString = new StringBuilder();

        mUrlStringBuilder.append(webURLData);

        mUrlString.append(Domain+"me");
        mUrlString.append("/");
        mUrlString.append(PreferenceUtils.getCompanyId(MainActivity.this));
        mUrlString.append("/");
        mUrlString.append(PreferenceUtils.getRegionCode(MainActivity.this));
        mUrlString.append("/");
        mUrlString.append(PreferenceUtils.getUserCode(MainActivity.this));

        byte[] encodeValue = Base64.encode(mUrlString.toString().getBytes(), Base64.DEFAULT);
        String newUrl = new String(encodeValue);

        String urlBuilder = mUrlStringBuilder.toString() + newUrl;
        loadWebView(false, urlBuilder);
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                        MenuModel model = headerList.get(groupPosition);
                        if (model.menuName.equalsIgnoreCase("Log Out")) {
                            Intent intent = new Intent(MainActivity.this,LogOutActivity.class);
                            startActivity(intent);
                        }else{
                            checkUserValidation(groupPosition);
                        }

                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {

                    if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                        MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                        getSupportActionBar().setTitle(model.menuName);
                        String webUrlData = AppUtils.encodeURL(MainActivity.this, model.url);
                        loadWebView(true, webUrlData);
                    }

                }

                return false;
            }
        });
    }

    private void checkUserValidation(final int groupPosition) {
        showProgressDialog("Checking User Authentication...");
        NetworkAPIClass networkAPIClass = new NetworkAPIClass(this);
        networkAPIClass.setListener(new NetworkAPIClass.UserAuthenticationAPICallBackListener() {
            @Override
            public void getusetAuthenticationAPISuccessCB(List<UserDetailsModel> userDetailsModels) {
                if (userDetailsModels.size() > 0 && userDetailsModels != null) {
                    hideProgressDialog();
                    mUserIsValid(groupPosition);
                } else {
                    //  hideProgressDialog();
                    hideProgressDialog();
                    Intent i=new Intent(MainActivity.this,LoginActivity.class);
                    logoutFunction();
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void getuserAuthenticationAPIFailureCB(String message) {
                Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                logoutFunction();
                startActivity(i);
                finish();
                 hideProgressDialog();

            }
        });


        UserAuthenticationModel userAuthenticationModel = new UserAuthenticationModel();
        userAuthenticationModel.setUserName(PreferenceUtils.getUserName(MainActivity.this));
        userAuthenticationModel.setPassword(PreferenceUtils.getUserPassword(MainActivity.this));
        userAuthenticationModel.setCompanyCode(PreferenceUtils.getCompanyCode(MainActivity.this));

        networkAPIClass.checkUserAuthentication(userAuthenticationModel);



    }

    private void mUserIsValid(int groupPosition) {

        MenuModel model = headerList.get(groupPosition);
        if (model.menuName.equalsIgnoreCase("DCR")) {

            String domain= PreferenceUtils.getCompanyName(MainActivity.this);
            String Domain = domain.substring(0,domain.length()-2);
            String webURLData = "https://"+ Domain+"me/HiDoctor_Activity/MTO/MTOMobile?Lid=";
            StringBuilder mUrlStringBuilder = new StringBuilder();
            StringBuilder mUrlString = new StringBuilder();

            mUrlStringBuilder.append(webURLData);

            mUrlString.append(Domain+"me");
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getCompanyId(MainActivity.this));
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getRegionCode(MainActivity.this));
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getUserCode(MainActivity.this));


            byte[] encodeValue = Base64.encode(mUrlString.toString().getBytes(), Base64.DEFAULT);
            String newUrl = new String(encodeValue);

            String urlBuilder = mUrlStringBuilder.toString() + newUrl;
                            /*String webUrlData = AppUtils.encodeURL(MainActivity.this, Constants.MENU_CONFIGURATION);
                            loadWebView(true, webUrlData);*/
            loadWebView(true, urlBuilder);
            getSupportActionBar().setTitle(model.menuName);
            drawerLayout.closeDrawers();
        }else if (model.menuName.equalsIgnoreCase("Reports")) {


            // Toast.makeText(MainActivity.this,"Reports Under Construction",Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
            String domain= PreferenceUtils.getCompanyName(MainActivity.this);
            String Domain = domain.substring(0,domain.length()-2);
            String webURLData = "https://"+ Domain+"me/HiDoctor_Activity/MTO/MTOReports?Lid=";
            StringBuilder mUrlStringBuilder = new StringBuilder();
            StringBuilder mUrlString = new StringBuilder();

            mUrlStringBuilder.append(webURLData);

            mUrlString.append(Domain+"me");
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getCompanyId(MainActivity.this));
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getRegionCode(MainActivity.this));
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getUserCode(MainActivity.this));


            byte[] encodeValue = Base64.encode(mUrlString.toString().getBytes(), Base64.DEFAULT);
            String newUrl = new String(encodeValue);

            String urlBuilder = mUrlStringBuilder.toString() + newUrl;
                            /*String webUrlData = AppUtils.encodeURL(MainActivity.this, Constants.MENU_CONFIGURATION);
                            loadWebView(true, webUrlData);*/
            loadWebView(true, urlBuilder);
            getSupportActionBar().setTitle(model.menuName);
            drawerLayout.closeDrawers();
                             /*String webUrlData = AppUtils.encodeURL(MainActivity.this, Constants.MENU_CONFIGURATION);
                            loadWebView(true, webUrlData);*/
            getSupportActionBar().setTitle(model.menuName);
        }else if (model.menuName.equalsIgnoreCase("Delete DCR")) {


            // Toast.makeText(MainActivity.this,"Reports Under Construction",Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
            String domain= PreferenceUtils.getCompanyName(MainActivity.this);
            String Domain = domain.substring(0,domain.length()-2);
            String webURLData = "https://"+ Domain+"me/HiDoctor_Activity/MTO/MTODeleteReports?Lid=";
            StringBuilder mUrlStringBuilder = new StringBuilder();
            StringBuilder mUrlString = new StringBuilder();

            mUrlStringBuilder.append(webURLData);

            mUrlString.append(Domain+"me");
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getCompanyId(MainActivity.this));
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getRegionCode(MainActivity.this));
            mUrlString.append("/");
            mUrlString.append(PreferenceUtils.getUserCode(MainActivity.this));


            byte[] encodeValue = Base64.encode(mUrlString.toString().getBytes(), Base64.DEFAULT);
            String newUrl = new String(encodeValue);

            String urlBuilder = mUrlStringBuilder.toString() + newUrl;
                            /*String webUrlData = AppUtils.encodeURL(MainActivity.this, Constants.MENU_CONFIGURATION);
                            loadWebView(true, webUrlData);*/
            loadWebView(true, urlBuilder);
            getSupportActionBar().setTitle(model.menuName);
            drawerLayout.closeDrawers();
                             /*String webUrlData = AppUtils.encodeURL(MainActivity.this, Constants.MENU_CONFIGURATION);
                            loadWebView(true, webUrlData);*/
            getSupportActionBar().setTitle(model.menuName);
        }


    }

    private void loadWebView(boolean backOptionValue, String url) {


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.loadUrl(url);
        Log.d("parm erl",url);
        //webView.requestFocus(View.FOCUS_DOWN);

        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressDialog("Loading...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgressDialog();
                if(NetworkUtils.isNetworkAvailable(MainActivity.this)){

                }

            }

        };

        webView.setWebViewClient(webViewClient);

        if (backOptionValue) {
            onBackPressed();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_LOCATION_REQUEST_PERMISSION:{
                if(grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    if(callbacklocation != null ){
                        callbacklocation.invoke(originlocation,true,true);
                    }
                }else{
                    if(callbacklocation != null ){
                        callbacklocation.invoke(originlocation,false,false);
                    }
                }
                return;
            }


            case REQUEST_PERMISSION_STORAGE_FOR_DCR_ATTACHMENTS:{
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){

                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
            isStoragePermissionGranted();
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else
            Toast.makeText(this.getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }

}

