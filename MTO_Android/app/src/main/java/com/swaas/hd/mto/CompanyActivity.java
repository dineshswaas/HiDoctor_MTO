package com.swaas.hd.mto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swaas.hd.mto.RetrofitBuilder.CompanyDetailsModel;
import com.swaas.hd.mto.RetrofitBuilder.NetworkAPIClass;
import com.swaas.hd.mto.Utils.AppUtils;
import com.swaas.hd.mto.Utils.NetworkUtils;
import com.swaas.hd.mto.Utils.PreferenceUtils;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyActivity extends RootActivity implements View.OnClickListener {

    int backButtonCount;

    @Nullable
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;
    @Nullable
    @BindView(R.id.edit_company)
    EditText editCompanyEditText;
    @Nullable
    @BindView(R.id.btn_next)
    Button nextButton;
    @Nullable
    @BindView(R.id.txt_help)
    TextView txtHelpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        ButterKnife.bind(this);

        setOnClickListener();
    }

    private void setOnClickListener() {
        parentLayout.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        txtHelpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.parent_layout:
                AppUtils.hideKeyboard(this);
                break;

            case R.id.btn_next:
                companyNameValidation();
                break;

            case R.id.txt_help:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://hdandroidhelp.hidoctor.me/loginhelp.html"));
                startActivity(browserIntent);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            backButtonCount = 0;
            //this.finish();
           // moveTaskToBack(true);
            super.onBackPressed();

        } else {
            Toast.makeText(this, getResources().getString(R.string.app_exit_alert), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }

    }

    private void companyNameValidation() {

        String companyURL = editCompanyEditText.getText().toString().trim();
        String alertMsg = "";

        if (TextUtils.isEmpty(companyURL)) {
            alertMsg = getResources().getString(R.string.please_enter_company_name);
        }
        if (!TextUtils.isEmpty(alertMsg)) {
            showMessagebox(CompanyActivity.this, alertMsg, null, true);
        } else {
            getCompanyDetailsFromAPI();
        }


    }

    private void getCompanyDetailsFromAPI() {
        String companyURL = editCompanyEditText.getText().toString().trim();
        if (NetworkUtils.isNetworkAvailable(CompanyActivity.this)) {
            companyURL = companyURL.concat(getResources().getString(R.string.url_extentions));
            showProgressDialog(getResources().getString(R.string.loading));
            NetworkAPIClass networkAPIClass = new NetworkAPIClass(this);
            final String finalCompanyURL = companyURL;
            networkAPIClass.setListener(new NetworkAPIClass.CompanyAPICallBackListener() {
                @Override
                public void getCompanyAPISuccessCB(List<CompanyDetailsModel> companyDetailModels) {
                    hideProgressDialog();
                    if (companyDetailModels != null && companyDetailModels.size() > 0) {
                        if (!TextUtils.isEmpty(companyDetailModels.get(0).getCompany_Code())) {
                            PreferenceUtils.setCompanyCode(CompanyActivity.this, companyDetailModels.get(0).getCompany_Code());
                        }
                        if (!TextUtils.isEmpty(companyDetailModels.get(0).getCompany_Name())) {
                            PreferenceUtils.setCompanyName(CompanyActivity.this, companyDetailModels.get(0).getCompany_Name());
                        }
                        if (!TextUtils.isEmpty(companyDetailModels.get(0).getCompany_Url())){
                            PreferenceUtils.setCompanyLogoFilePath(CompanyActivity.this,companyDetailModels.get(0).getCompany_Logo_Url());
                        }
                        PreferenceUtils.setCompanyId(CompanyActivity.this, companyDetailModels.get(0).getCompany_Id());
                        PreferenceUtils.setCompanyUrl(CompanyActivity.this, finalCompanyURL);

                        Intent intent = new Intent(CompanyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void getCompanyAPIFailureCB(String message) {
                    hideProgressDialog();
                    Toast.makeText(CompanyActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                }
            });
            networkAPIClass.getCompanyDetailsFromAPI(finalCompanyURL);
        }

    }
}
