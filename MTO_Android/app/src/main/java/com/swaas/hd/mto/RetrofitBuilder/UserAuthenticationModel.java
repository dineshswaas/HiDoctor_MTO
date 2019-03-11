package com.swaas.hd.mto.RetrofitBuilder;

public class UserAuthenticationModel {

    public String CompanyCode;
    public String UserName;
    public String Password;
    public String DeviceType; // Android, IOS , Tab etc.
    public String DeviceOSVersion;
    public String Key;
    public String VersionCode;
    public String VersionName;
    public String Device_Model;
    public String Device_Name;
    public String Device_Release_Version;
    public String Ref_Number;

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getDeviceOSVersion() {
        return DeviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        DeviceOSVersion = deviceOSVersion;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String versionCode) {
        VersionCode = versionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public String getDevice_Model() {
        return Device_Model;
    }

    public void setDevice_Model(String device_Model) {
        Device_Model = device_Model;
    }

    public String getDevice_Name() {
        return Device_Name;
    }

    public void setDevice_Name(String device_Name) {
        Device_Name = device_Name;
    }

    public String getDevice_Release_Version() {
        return Device_Release_Version;
    }

    public void setDevice_Release_Version(String device_Release_Version) {
        Device_Release_Version = device_Release_Version;
    }

    public String getRef_Number() {
        return Ref_Number;
    }

    public void setRef_Number(String ref_Number) {
        Ref_Number = ref_Number;
    }
}
