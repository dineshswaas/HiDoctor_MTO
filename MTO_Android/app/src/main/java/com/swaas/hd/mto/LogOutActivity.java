package com.swaas.hd.mto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.swaas.hd.mto.ExpandableListItem.MainActivity;
import com.swaas.hd.mto.RetrofitBuilder.NetworkAPIClass;
import com.swaas.hd.mto.RetrofitBuilder.UserAuthenticationModel;
import com.swaas.hd.mto.RetrofitBuilder.UserDetailsModel;
import com.swaas.hd.mto.Utils.AppUtils;
import com.swaas.hd.mto.Utils.NetworkUtils;
import com.swaas.hd.mto.Utils.PreferenceUtils;

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogOutActivity extends RootActivity implements View.OnClickListener {

    private static final String HIDOCTOR = "HIDOCTOR";

    @Nullable
    @BindView(R.id.editPassword)
    EditText edt_password;
    @Nullable
    @BindView(R.id.btn_Logout)
    Button logOutButton;
    @Nullable
    @BindView(R.id.btn_Cancel)
    Button cancelButton;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.parentLayout)
    LinearLayout parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        ButterKnife.bind(this);

        setUpToolBar();
        setOnClickListener();

        edt_password.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {

                        if (cs.equals("")) {
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z0-9-_!@*. ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });

        edt_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_password.getText().toString().trim().length() == 1) {
                    if (edt_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        edt_password.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    } else {
                        edt_password.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    }
                    edt_password.setCompoundDrawablePadding(24);
                    /*txtPassword.setSelection(txtPassword.getText().toString().trim().length());*/
                } else if (edt_password.getText().toString().trim().length() == 0) {
                    edt_password.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, 0, 0);
                } else {
                    if (edt_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        edt_password.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    } else {
                        edt_password.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    }
                    edt_password.setCompoundDrawablePadding(24);
                    /*txtPassword.setSelection(txtPassword.getText().toString().trim().length());*/
                }
            }
        });


        edt_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edt_password.getCompoundDrawables()[2] != null) {
                    boolean tappedX = event.getX() > (edt_password.getWidth() - edt_password.getPaddingRight() - edt_password.getCompoundDrawables()[2].getIntrinsicWidth());
                    if (tappedX) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (edt_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                                edt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            } else {
                                edt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            }

                            edt_password.setSelection(edt_password.getText().toString().trim().length());
                        }
                        return true;
                    }
                }
                return false;
            }
        });


    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.logout_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
    }

    private void setOnClickListener() {

        logOutButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        parentLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Logout:
                AppUtils.hideKeyboard(this);
                passwordValidation();
                //PreferenceUtils.setIsLoggedInStatus(LogOutActivity.this, false);
                break;
            case R.id.btn_Cancel:
                AppUtils.hideKeyboard(this);
                finish();
                break;
            case R.id.parentLayout:
                AppUtils.hideKeyboard(this);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void passwordValidation() {
        String alertMsg = "";
        if (edt_password.getText().toString().trim().length() == 0) {
            alertMsg = getResources().getString(R.string.enter_password);
        }

        if (!TextUtils.isEmpty(alertMsg)) {
            showMessagebox(LogOutActivity.this, alertMsg, null, true);
        } else {
            if (NetworkUtils.isNetworkAvailable(LogOutActivity.this)) {
                logOutFunction();
            }
        }

    }

    private void logOutFunction() {
        NetworkAPIClass networkAPIClass = new NetworkAPIClass(this);
        showProgressDialog(getResources().getString(R.string.loading));
        networkAPIClass.setListener(new NetworkAPIClass.UserAuthenticationAPICallBackListener() {
            @Override
            public void getusetAuthenticationAPISuccessCB(List<UserDetailsModel> userDetailsModels) {
                hideProgressDialog();
                if (userDetailsModels.size() > 0 && userDetailsModels != null) {
                    SharedPreferences share_settings = LogOutActivity.this.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = share_settings.edit();
                    editor.clear();
                    editor.commit();
                    PreferenceUtils.setIsLoggedInStatus(LogOutActivity.this, false);
                    File dir = new File(Environment.getExternalStorageDirectory() + "/HiDoctor");
                    deleteRecursive(dir);
                    MainActivity.activity.finish();
                    Intent intent = new Intent(LogOutActivity.this, CompanyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();

                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "Logout SuccessFully", Toast.LENGTH_SHORT).show();

                } else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "Logout Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void getuserAuthenticationAPIFailureCB(String message) {
                Toast.makeText(LogOutActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                hideProgressDialog();

            }
        });


        UserAuthenticationModel userAuthenticationModel = new UserAuthenticationModel();
        userAuthenticationModel.setUserName(PreferenceUtils.getUserName(LogOutActivity.this));
        userAuthenticationModel.setPassword(edt_password.getText().toString().trim());
        userAuthenticationModel.setCompanyCode(PreferenceUtils.getCompanyCode(LogOutActivity.this));

        networkAPIClass.checkUserAuthentication(userAuthenticationModel);
    }

    public void deleteRecursive(File dir) {

        if (dir.isDirectory()) {
            if (dir.listFiles() != null && dir.listFiles().length > 0) {
                for (File child : dir.listFiles()) {
                    if (child != null) {
                        deleteRecursive(child);
                    }

                }
            }

        }
        // if(dir.isFile())
        dir.delete();

    }

}
