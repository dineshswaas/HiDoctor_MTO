package com.swaas.hd.mto.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.stetho.Stetho;
import com.swaas.hd.mto.CompanyActivity;
import com.swaas.hd.mto.ExpandableListItem.MainActivity;

/**
 * Created by barath on 11/19/2018.
 */

public class MTO_Application_Class extends Activity
{
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        if (PreferenceUtils.getIsLoggedInStatus(MTO_Application_Class.this)) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, CompanyActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
