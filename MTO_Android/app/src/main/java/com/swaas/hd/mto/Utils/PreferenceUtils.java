package com.swaas.hd.mto.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static final String HIDOCTOR = "HIDOCTOR";
    private static final String COMPANY_CODE = "companyCode";
    private static final String COMPANY_URL = "companyURL";
    private static final String COMPANY_NAME = "companyName";
    private static final String COMPANY_ID = "Company_Id";
    private static final String USER_NAME = "userName";
    private static final String USER_TYPE_NAME = "userTypeName";
    private static final String USER_PASSWORD = "userPassword";
    private static final String USER_CODE = "userCode";
    private static final String COMPANY_LOGO_FILE_PATH = "Company_logo_file_path";
    private static final String IS_LOGGED_IN="is_logged_in";
    private static final String REGION_CODE = "regionCode";

    public static void setCompanyCode(Context context, String companyCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMPANY_CODE, companyCode);
        editor.commit();
    }

    public static String getCompanyCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        String regionCode = sharedPreferences.getString(COMPANY_CODE, "-1");
        return regionCode;
    }
    public static void setRegionCode(Context context, String regionCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REGION_CODE, regionCode);
        editor.commit();
    }

    public static String getRegionCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        String companyCode = sharedPreferences.getString(REGION_CODE, "-1");
        return companyCode;
    }

    public static void setCompanyUrl(Context context, String companyurl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMPANY_URL, companyurl);
        editor.commit();
    }

    public static String getCompanyUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        String companyurl = sharedPreferences.getString(COMPANY_URL, null);
        return companyurl;
    }

    public static String getCompanyName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        String companyName = sharedPreferences.getString(COMPANY_NAME, "");
        return companyName;
    }

    public static void setCompanyName(Context context, String companyName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMPANY_NAME, companyName);
        editor.commit();
    }

    public static void setCompanyId(Context context, int companyId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COMPANY_ID, companyId);
        editor.commit();
    }

    public static int getCompanyId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        int companyId = sharedPreferences.getInt(COMPANY_ID, 0);
        return companyId;
    }

    public static String getCompanyLogoFilePath(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        return sharedPreferences.getString(COMPANY_LOGO_FILE_PATH, null);
    }

    public static void setCompanyLogoFilePath(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMPANY_LOGO_FILE_PATH, value);
        editor.commit();
    }

    public static String getUserCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        String userCode = sharedPreferences.getString(USER_CODE, "-1");
        return userCode;
    }

    public static void setUserCode(Context context, String userCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_CODE, userCode);
        editor.commit();
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(USER_NAME, "");
        return userName;
    }

    public static void setUserPassword(Context context, String userName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PASSWORD, userName);
        editor.commit();
    }

    public static String getUserPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(USER_PASSWORD, "");
        return userName;
    }

    public static void setUserTypeName(Context context, String userName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TYPE_NAME, userName);
        editor.commit();
    }

    public static String getUserTypeName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HIDOCTOR, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(USER_TYPE_NAME, "");
        return userName;
    }


    public static boolean getIsLoggedInStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(IS_LOGGED_IN, Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
        return value;
    }


    public static void setIsLoggedInStatus(Context context, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(IS_LOGGED_IN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, value);
        editor.commit();
    }
}
