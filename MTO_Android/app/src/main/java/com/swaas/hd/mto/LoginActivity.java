package com.swaas.hd.mto;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.swaas.hd.mto.ExpandableListItem.MainActivity;
import com.swaas.hd.mto.RetrofitBuilder.NetworkAPIClass;
import com.swaas.hd.mto.RetrofitBuilder.UserAuthenticationModel;
import com.swaas.hd.mto.RetrofitBuilder.UserDetailsModel;
import com.swaas.hd.mto.Utils.AppUtils;
import com.swaas.hd.mto.Utils.NetworkUtils;
import com.swaas.hd.mto.Utils.PreferenceUtils;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends RootActivity implements View.OnClickListener {


    @Nullable
    @BindView(R.id.parent_layout)
    LinearLayout parentLinearLayout;
    @Nullable
    @BindView(R.id.txt_change)
    TextView changeTextView;
    @Nullable
    @BindView(R.id.edit_username)
    EditText userNameEditText;
    @Nullable
    @BindView(R.id.edit_password)
    EditText txtPassword;
    @Nullable
    @BindView(R.id.btn_login)
    Button loginButton;
    @Nullable
    @BindView(R.id.txt_forgot_password)
    TextView forgotPasswordTextView;
    @Nullable
    @BindView(R.id.txt_companyName)
    TextView companyName;
    @Nullable
    @BindView(R.id.txt_company_url)
    TextView companyUrlView;
    @Nullable
    @BindView(R.id.company_logo)
    ImageView companyLogoImageView;

    private static final Pattern USERNAME_PATTERN = Pattern
            .compile("[a-zA-Z0-9]{1,30}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ButterKnife.bind(this);

        companyName.setText(getResources().getString(R.string.pharma_welcome_you));
        companyUrlView.setText(PreferenceUtils.getCompanyUrl(LoginActivity.this));
        String filePathName = PreferenceUtils.getCompanyLogoFilePath(LoginActivity.this);
        if (!TextUtils.isEmpty(filePathName)) {
            Glide.with(LoginActivity.this).load(R.mipmap.ic_hidoc_logo).into(companyLogoImageView);
        }

        setOnClickListener();

        txtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtPassword.getText().toString().trim().length() == 1) {
                    if (txtPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    } else {
                        txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    }
                    txtPassword.setCompoundDrawablePadding(24);
                    /*txtPassword.setSelection(txtPassword.getText().toString().trim().length());*/
                } else if (txtPassword.getText().toString().trim().length() == 0) {
                    txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, 0, 0);
                } else {
                    if (txtPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, R.mipmap.ic_visibility_off_black_24dp, 0);
                    } else {
                        txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_dialpad_black_24dp, 0, R.mipmap.ic_visibility_black_24dp, 0);
                    }
                    txtPassword.setCompoundDrawablePadding(24);
                    /*txtPassword.setSelection(txtPassword.getText().toString().trim().length());*/
                }
            }
        });


        txtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (txtPassword.getCompoundDrawables()[2] != null) {
                    boolean tappedX = event.getX() > (txtPassword.getWidth() - txtPassword.getPaddingRight() - txtPassword.getCompoundDrawables()[2].getIntrinsicWidth());
                    if (tappedX) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (txtPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                                txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            } else {
                                txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            }

                            txtPassword.setSelection(txtPassword.getText().toString().trim().length());
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        txtPassword.setFilters(new InputFilter[]{
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

    }

    private void setOnClickListener() {
        parentLinearLayout.setOnClickListener(this);
        changeTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.parent_layout:
                AppUtils.hideKeyboard(this);
                break;
            case R.id.txt_change:
                AppUtils.hideKeyboard(this);
                Intent intent = new Intent(LoginActivity.this, CompanyActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_login:
                AppUtils.hideKeyboard(this);
                userCredentialsValidation();
                break;
            case R.id.txt_forgot_password:
                AppUtils.hideKeyboard(this);
                Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent1);
                //finish();
                break;
        }

    }

    private void userCredentialsValidation() {

        String alertMsg = "";
        if (userNameEditText.getText().toString().trim().length() == 0) {
            alertMsg = getResources().getString(R.string.enter_user_name);
        } else if (!CheckUsername(userNameEditText.getText().toString().trim())) {
            alertMsg = getResources().getString(R.string.enter_valid_user_name);
        } else if (txtPassword.getText().toString().trim().length() == 0) {
            alertMsg = getResources().getString(R.string.enter_password);
        }

        if (!TextUtils.isEmpty(alertMsg)) {
            showMessagebox(LoginActivity.this, alertMsg, null, true);
        } else {
            if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
                checkUserAuthentication();

            }
        }

    }

    private boolean CheckUsername(String userNameValue) {

        return USERNAME_PATTERN.matcher(userNameValue).matches();
    }

    private void checkUserAuthentication() {
        NetworkAPIClass networkAPIClass = new NetworkAPIClass(this);
        showProgressDialog(getResources().getString(R.string.loading));
        networkAPIClass.setListener(new NetworkAPIClass.UserAuthenticationAPICallBackListener() {
            @Override
            public void getusetAuthenticationAPISuccessCB(List<UserDetailsModel> userDetailsModels) {
                hideProgressDialog();
                if (userDetailsModels.size() > 0) {
                    if (!TextUtils.isEmpty(userDetailsModels.get(0).getUser_Code())) {
                        PreferenceUtils.setRegionCode(LoginActivity.this,userDetailsModels.get(0).getRegion_Code());
                        PreferenceUtils.setIsLoggedInStatus(LoginActivity.this, true);
                        PreferenceUtils.setUserName(LoginActivity.this, userDetailsModels.get(0).getUser_Name());
                        PreferenceUtils.setUserPassword(LoginActivity.this, txtPassword.getText().toString().trim());
                        PreferenceUtils.setUserCode(LoginActivity.this, userDetailsModels.get(0).getUser_Code());
                        PreferenceUtils.setUserTypeName(LoginActivity.this, userDetailsModels.get(0).getUser_Type_Name());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void getuserAuthenticationAPIFailureCB(String message) {
                hideProgressDialog();
                Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();

            }
        });

        UserAuthenticationModel userAuthenticationModel = new UserAuthenticationModel();
        userAuthenticationModel.setCompanyCode(PreferenceUtils.getCompanyCode(LoginActivity.this));
        userAuthenticationModel.setUserName(userNameEditText.getText().toString().trim());
        userAuthenticationModel.setPassword(txtPassword.getText().toString().trim());
        networkAPIClass.checkUserAuthentication(userAuthenticationModel);
    }

}
