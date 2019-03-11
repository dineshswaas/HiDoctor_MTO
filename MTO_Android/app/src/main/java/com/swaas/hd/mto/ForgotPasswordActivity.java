package com.swaas.hd.mto;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.swaas.hd.mto.RetrofitBuilder.NetworkAPIClass;
import com.swaas.hd.mto.Utils.AppUtils;
import com.swaas.hd.mto.Utils.NetworkUtils;
import com.swaas.hd.mto.Utils.PreferenceUtils;

import java.util.regex.Pattern;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends RootActivity implements View.OnClickListener {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9]{1,30}");

    @Nullable
    @BindView(R.id.editUserID)
    EditText userIdEditText;
    @Nullable
    @BindView(R.id.edit_company_url)
    EditText editCompanyUrlEditText;
    @Nullable
    @BindView(R.id.btn_request_password)
    Button requestPasswordButton;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        setUpToolBar();
        if (!TextUtils.isEmpty(PreferenceUtils.getCompanyUrl(ForgotPasswordActivity.this))) {
            editCompanyUrlEditText.setText(PreferenceUtils.getCompanyUrl(ForgotPasswordActivity.this));
        }
        setOnClickListener();
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.forgot_password_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.mipmap.ic_arrow_back_white_24dp));
    }

    private void setOnClickListener() {
        requestPasswordButton.setOnClickListener(this);
        parentLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_password:
                AppUtils.hideKeyboard(this);
                userIdValidation();
                break;
            case R.id.parent_layout:
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



    private void userIdValidation() {
        String alertMsg = "";
        if (userIdEditText.getText().toString().trim().length() == 0) {
            alertMsg = getResources().getString(R.string.enter_user_Id);
        } else if (!CheckUsername(userIdEditText.getText().toString())) {
            alertMsg = getResources().getString(R.string.enter_valid_user_Id);
        }
        if (!TextUtils.isEmpty(alertMsg)) {
            showMessagebox(ForgotPasswordActivity.this, alertMsg, null, true);
        } else {
            if (NetworkUtils.isNetworkAvailable(ForgotPasswordActivity.this)) {
                checkForgotPassword();
            }
        }

    }

    private boolean CheckUsername(String userNameValue) {

        return USERNAME_PATTERN.matcher(userNameValue).matches();
    }

    private void checkForgotPassword() {
        NetworkAPIClass networkAPIClass = new NetworkAPIClass(this);
        showProgressDialog(getResources().getString(R.string.loading));
        networkAPIClass.setListener(new NetworkAPIClass.ForgotPasswordAPICallbackListener() {
            @Override
            public void getForgotPasswordAPISuccessCB(String message) {
                hideProgressDialog();
                if (!TextUtils.isEmpty(message)){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.alert_text));
                    alertDialogBuilder.setMessage(getResources().getString(R.string.forgot_password_sent_message));
                    alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok_string), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intentForgotPassword = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentForgotPassword);
                            finish();
                        }
                    });

                    alertDialogBuilder.create();
                    alertDialogBuilder.show();
                }
            }

            @Override
            public void getForgotPasswordAPIFailureCB(String message) {
                hideProgressDialog();
                Toast.makeText(ForgotPasswordActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
        networkAPIClass.CheckForgotPassword(userIdEditText.getText().toString().trim());


    }

}
