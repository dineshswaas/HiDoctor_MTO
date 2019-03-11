package com.swaas.hd.mto.RetrofitBuilder;

import android.content.Context;
import android.util.Log;

import com.swaas.hd.mto.R;
import com.swaas.hd.mto.Utils.NetworkUtils;
import com.swaas.hd.mto.Utils.PreferenceUtils;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class NetworkAPIClass {

    private Context mContext;
    private CompanyAPICallBackListener companyAPICallBackListener;
    private LoginAPICallBackListener loginAPICallBackListener;
    private UserAuthenticationAPICallBackListener userAuthenticationAPICallBackListener;
    private ForgotPasswordAPICallbackListener forgotPasswordAPICallbackListener;
    private NavigationMenuAPICallBackListener navigationMenuAPICallBackListener;
    private GetUserTypeMenusFromDB getMenusFromDB;
    public NetworkAPIClass(Context context) {
        mContext = context;
    }


    public void getCompanyDetailsFromAPI(String url) {
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        final WebAPIURLListener webAPIURLListener = retrofit.create(WebAPIURLListener.class);
        Call call = webAPIURLListener.getCompanyDetails(url);
        call.enqueue(new Callback<APIResponse<CompanyDetailsModel>>() {
            @Override
            public void onResponse(Response<APIResponse<CompanyDetailsModel>> response, Retrofit retrofit) {
                APIResponse apiResponse = response.body();

                if (apiResponse.getStatus() == 1) {
                    List<CompanyDetailsModel> lstcompany = null;
                    if (apiResponse.getResult().size() > 0) {
                        lstcompany = apiResponse.getResult();
                        companyAPICallBackListener.getCompanyAPISuccessCB(lstcompany);
                    } else {
                        companyAPICallBackListener.getCompanyAPIFailureCB(apiResponse.getMessage());
                    }

                } else if (apiResponse.getStatus() == 0) {
                    companyAPICallBackListener.getCompanyAPIFailureCB(apiResponse.getMessage());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                companyAPICallBackListener.getCompanyAPIFailureCB(mContext.getResources().getString(R.string.something_went_wrong));

            }

        });

    }

    public void checkUserAuthentication(UserAuthenticationModel userAuthenticationModel) {
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        final WebAPIURLListener webAPIURLListener = retrofit.create(WebAPIURLListener.class);
        Call call = webAPIURLListener.CheckUserAuthentication(userAuthenticationModel);
        call.enqueue(new Callback<APIResponse<UserDetailsModel>>() {
                         @Override
                         public void onResponse(Response<APIResponse<UserDetailsModel>> response, Retrofit retrofit) {
                             APIResponse apiResponse = response.body();
                             if (apiResponse != null) {
                                 if (apiResponse.getStatus() == 1) {
                                     List<UserDetailsModel> lstuser = (List<UserDetailsModel>) apiResponse.getResult();
                                     if (lstuser.size() > 0) {
                                         userAuthenticationAPICallBackListener.getusetAuthenticationAPISuccessCB(lstuser);
                                     } else {
                                         userAuthenticationAPICallBackListener.getuserAuthenticationAPIFailureCB(apiResponse.getMessage());
                                     }


                                 } else {
                                     userAuthenticationAPICallBackListener.getuserAuthenticationAPIFailureCB(apiResponse.getMessage());
                                 }
                             } else {
                                 userAuthenticationAPICallBackListener.getuserAuthenticationAPIFailureCB(response.errorBody().toString());
                             }
                         }

                         @Override
                         public void onFailure(Throwable t) {
                             userAuthenticationAPICallBackListener.getuserAuthenticationAPIFailureCB(t.toString());

                         }
                     }

        );
    }


    public void getUserAccountDetailsInfo() {
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        final WebAPIURLListener webAPIURLListener = retrofit.create(WebAPIURLListener.class);
        Call call = webAPIURLListener.getAccountDetails(PreferenceUtils.getUserTypeName(mContext), String.valueOf(PreferenceUtils.getCompanyId(mContext)));
        call.enqueue(new Callback<APIResponse<UserDetailsModel>>() {
            @Override
            public void onResponse(Response<APIResponse<UserDetailsModel>> response, Retrofit retrofit) {
                APIResponse apiResponse = response.body();
                if (apiResponse != null && apiResponse.getStatus() == 1) {
                    List<UserDetailsModel> userList = (List<UserDetailsModel>) apiResponse.getResult();
                    loginAPICallBackListener.getLoginAPISuccessCB(userList);
                } else {
                    loginAPICallBackListener.getLoginAPIFailureCB("");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                loginAPICallBackListener.getLoginAPIFailureCB(t.toString());
            }
        });
    }

    public void getNavigationMenuInfo() {
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        final WebAPIURLListener webAPIURLListener = retrofit.create(WebAPIURLListener.class);
        String userTypeName = PreferenceUtils.getUserTypeName(mContext);
        //String companyId = String.valueOf(PreferenceUtils.getCompanyId(mContext));
        String companyId = "1035";
        Call call = webAPIURLListener.getNavigationMenuDetails(userTypeName, companyId);
        //Call call = webAPIURLListener.getNavigationMenuDetails("DSM", "1035");
        call.enqueue(new Callback<APIResponse<NavigationMenuModel>>() {
            @Override
            public void onResponse(Response<APIResponse<NavigationMenuModel>> response, Retrofit retrofit) {
                APIResponse apiResponse = response.body();
                if (apiResponse != null && apiResponse.getStatus() == 1) {
                    List<NavigationMenuModel> menuModelList = (List<NavigationMenuModel>) apiResponse.getResult();
                    navigationMenuAPICallBackListener.getNavigationMenuAPISuccessCB(menuModelList);
                } else {
                    navigationMenuAPICallBackListener.getNavigationMenuAPIFailureCB("Please Try Again Later");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                navigationMenuAPICallBackListener.getNavigationMenuAPIFailureCB(t.toString());
            }
        });
    }

    public void CheckForgotPassword(String userName) {
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        final WebAPIURLListener webAPIURLListener = retrofit.create(WebAPIURLListener.class);
        Call call = webAPIURLListener.CheckForgotPassword(PreferenceUtils.getCompanyCode(mContext), userName);
        call.enqueue(new Callback<APIResponse<UserDetailsModel>>() {
            @Override
            public void onResponse(Response<APIResponse<UserDetailsModel>> response, Retrofit retrofit) {
                APIResponse apiResponse = response.body();
                if (apiResponse != null && apiResponse.getStatus() == 1) {
                    String message = apiResponse.getMessage();
                    forgotPasswordAPICallbackListener.getForgotPasswordAPISuccessCB(message);
                } else {
                    //String message = apiResponse.getMessage();
                    forgotPasswordAPICallbackListener.getForgotPasswordAPIFailureCB("Please try again later.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                forgotPasswordAPICallbackListener.getForgotPasswordAPIFailureCB(t.toString());
            }
        });
    }



    public void getMenusFromWebService(String companyCode,String userCode,String regionCode){

        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofitAPI = RetrofitAPIBuilder.getInstance();
            final WebAPIURLListener webAPIURLListener = retrofitAPI.create(WebAPIURLListener.class);
            Call call = webAPIURLListener.getUserTypeMenuAccess(companyCode, userCode, regionCode);
            call.enqueue(new retrofit.Callback<APIResponse<UserTypeMenus>>() {
                             @Override
                             public void onResponse(Response<APIResponse<UserTypeMenus>> response, Retrofit retrofit) {
                                 Log.d("SFC API SUCCESS", "SUCCESS");
                                 APIResponse apiResponse = response.body();
                                 if (apiResponse != null && apiResponse.getStatus() == 1) {
                                     List<UserTypeMenus> userTypeMenuses = (List<UserTypeMenus>) apiResponse.getResult();
                                     getMenusFromDB.getMenusFromDBSuccess(userTypeMenuses);
                                 }else{
                                     getMenusFromDB.getMenusFromDBFailure(apiResponse.getMessage());
                                 }
                             }

                             @Override
                             public void onFailure(Throwable t) {
                                 getMenusFromDB.getMenusFromDBFailure(t.getMessage());
                                 Log.d("SFC API ERROR", "API");
                                 Log.d("SFC Erorr Msg", t.getMessage() + "");
                             }
                         }
            );
        }

    }



    public interface CompanyAPICallBackListener {
        void getCompanyAPISuccessCB(List<CompanyDetailsModel> companyDetailModels);

        void getCompanyAPIFailureCB(String message);
    }

    public void setListener(CompanyAPICallBackListener listener) {
        this.companyAPICallBackListener = listener;

    }

    public interface LoginAPICallBackListener {
        void getLoginAPISuccessCB(List<UserDetailsModel> userDetailsModels);

        void getLoginAPIFailureCB(String message);
    }

    public void setListener(LoginAPICallBackListener listener) {
        this.loginAPICallBackListener = listener;

    }


    public interface GetUserTypeMenusFromDB{
        void getMenusFromDBSuccess(List<UserTypeMenus> menuses);
        void getMenusFromDBFailure(String exception);
    }


    public void setUserTypeMenusDB (GetUserTypeMenusFromDB getMenusFromDB){
        this.getMenusFromDB = getMenusFromDB;

    }

    public interface UserAuthenticationAPICallBackListener {
        void getusetAuthenticationAPISuccessCB(List<UserDetailsModel> userDetailsModels);

        void getuserAuthenticationAPIFailureCB(String message);
    }

    public void setListener(UserAuthenticationAPICallBackListener listener) {
        this.userAuthenticationAPICallBackListener = listener;

    }

    public interface ForgotPasswordAPICallbackListener {
        void getForgotPasswordAPISuccessCB(String message);

        void getForgotPasswordAPIFailureCB(String message);
    }

    public void setListener(ForgotPasswordAPICallbackListener listener) {
        this.forgotPasswordAPICallbackListener = listener;

    }

    public interface NavigationMenuAPICallBackListener {
        void getNavigationMenuAPISuccessCB(List<NavigationMenuModel> navigationMenuModels);

        void getNavigationMenuAPIFailureCB(String message);
    }

    public void setListener(NavigationMenuAPICallBackListener listener) {
        this.navigationMenuAPICallBackListener = listener;

    }


}
