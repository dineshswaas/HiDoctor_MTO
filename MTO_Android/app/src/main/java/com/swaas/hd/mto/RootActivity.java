package com.swaas.hd.mto;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.swaas.hd.mto.Utils.MessageDialog;

public class RootActivity extends AppCompatActivity {
    protected static final String TAG = RootActivity.class.getSimpleName();
    protected View mCoordinatorLayout;
    /**
     * Our primary toolbar that is going to be made
     * the ActionBar.
     */
    protected Toolbar mToolbar;

    /**
     * The actual ActionBar instance.
     */
    private ActionBar mActionBar;

    /**
     * The layout holding the ToolBar (ActionBar) and TabLayout if any.
     * This layout allows us to control scroll behavior or the child elements
     * and its motion more easily and gracefully.
     */
    protected AppBarLayout mAppBarLayout;

    /**
     * The layout that is going to hold the actual content of the activity,
     * fragment, tab etc.
     */
    private FrameLayout mFlContentLayout;

    /**
     * Instance of Alert dialog to ensure dismiss of the dialog can be
     * handled separately.
     */
    protected AlertDialog mAlertDialog;

    /**
     * Dialogs are primarily UI components and hence convenience to declare
     * in RootActivity since any screen in Android is going to be under an
     * activity.
     */
    protected ProgressDialog mProgressDialog;

    /**
     * Floating Action button on each screen. To be used if necessary.
     * By default, VISIBILITY of this item is set to GONE since not all
     * screens would actually need this functionality.
     */

    public MessageDialog messageDialog;


    public Snackbar mSnackbar;

    int permissionStatus;


    /**
     * Handler to this activity thread.
     */
    protected final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        // Initialize the toolbar layout and assign it as the Action Bar for
        // all screens.
        mCoordinatorLayout = findViewById(R.id.base_Coordinator);
        mToolbar = (Toolbar) findViewById(R.id.ga_ActionBar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        // Initialize all the parent level views. They will be modified or added
        // by child activities and fragments based on their need.

        mFlContentLayout = (FrameLayout) findViewById(R.id.base_FlContent);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.base_AppBar);

        // Call the ActionBar customizer. We do the basic setup. Child activities
        // can override this function to get better control over the action bar.
        // The action bar and toolbar instances should not be misused anywhere else.
        // Hence made private and setup here. Child activities can save a local instance
        // of action bar from here on. But they cannot manipulate before this level.
        customizeActionBar(mActionBar, mToolbar);

    }

    /**
     * Method to customize the action bar based on each activity's needs.
     */
    protected void customizeActionBar(ActionBar actionBar, Toolbar toolbar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    protected void showProgressDialog(String message) {
        initDialog();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void initDialog() {
        // If a progress dialog instance is not available, create one.
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(RootActivity.this);
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void showMessagebox(Context context, String message, View.OnClickListener onClickListener, boolean isCancelable) {
        MessageDialog messageDialog = new MessageDialog(context);
        messageDialog.showDialog(context, message, onClickListener, isCancelable);
    }
}
