package com.swaas.hd.mto.Utils;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AppUtils {

    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String encodeURL(Activity activity,String url){

        String finalWebUrl=Constants.BASE_WEB_URL;

        StringBuilder mUrlStringBuilder = new StringBuilder();
        StringBuilder mUrlString = new StringBuilder();

        mUrlStringBuilder.append(finalWebUrl);

        mUrlString.append(url+PreferenceUtils.getUserName(activity)+"/"+PreferenceUtils.getUserPassword(activity));

        byte[] encodeValue = Base64.encode(mUrlString.toString().getBytes(), Base64.DEFAULT);
        String newUrl = new String(encodeValue);

        String urlBuilder = mUrlStringBuilder.toString() + newUrl;
        Log.e("urlData",""+urlBuilder);

        return  urlBuilder;
    }
}
