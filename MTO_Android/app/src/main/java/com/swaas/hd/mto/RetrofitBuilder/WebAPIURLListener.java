package com.swaas.hd.mto.RetrofitBuilder;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface WebAPIURLListener {

    @GET("CompanyApi/GetCompanyDetails/{subDomainName}")
    Call<APIResponse<CompanyDetailsModel>> getCompanyDetails(@Path("subDomainName") String subDomainName);

    @POST("User/Authentication/V2")
    Call<APIResponse<UserDetailsModel>> CheckUserAuthentication(@Body UserAuthenticationModel userAuthentication);

    @GET("User/AccountDetails/{Company_Code}/{User_Code}")
    Call<APIResponse<UserDetailsModel>> getAccountDetails(@Path("Company_Code") String companyCode,
                                                          @Path("User_Code") String userCode);

    @GET("UserApi/GetandSendPassWord/{companyCode}/{userName}")
    Call<APIResponse<UserDetailsModel>> CheckForgotPassword(@Path("companyCode") String companyCode, @Path("userName") String userName);

    @GET("MenuApi/GetMenuList/{User_Type_Name}/{Company_Id}")
    Call<APIResponse<NavigationMenuModel>> getNavigationMenuDetails(@Path("User_Type_Name") String userTypeName,
                                                                    @Path("Company_Id") String companyId);


    @GET("UserApi/GetUserTypeMenuAccess/{companyCode}/{userCode}/{regionCode}")
    Call<APIResponse<UserTypeMenus>> getUserTypeMenuAccess(@Path("companyCode") String companyCode, @Path("userCode")
            String userCode, @Path("regionCode") String regionCode);
}
