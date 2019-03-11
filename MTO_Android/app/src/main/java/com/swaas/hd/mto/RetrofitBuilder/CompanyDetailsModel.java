package com.swaas.hd.mto.RetrofitBuilder;

import java.io.Serializable;

public class CompanyDetailsModel implements Serializable {
    private Integer Company_Id;
    private String Company_Code;
    private String Company_Name;
    private String Company_Url;
    private int Geo_location_Support;
    private String Company_Logo_Url;
    private String Display_Name;
    private int Payroll_Integrated;

    public Integer getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(Integer company_Id) {
        Company_Id = company_Id;
    }

    public String getCompany_Code() {
        return Company_Code;
    }

    public void setCompany_Code(String company_Code) {
        Company_Code = company_Code;
    }

    public String getCompany_Name() {
        return Company_Name;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public String getCompany_Url() {
        return Company_Url;
    }

    public void setCompany_Url(String company_Url) {
        Company_Url = company_Url;
    }

    public int getGeo_location_Support() {
        return Geo_location_Support;
    }

    public void setGeo_location_Support(int geo_location_Support) {
        Geo_location_Support = geo_location_Support;
    }

    public String getCompany_Logo_Url() {
        return Company_Logo_Url;
    }

    public void setCompany_Logo_Url(String company_Logo_Url) {
        Company_Logo_Url = company_Logo_Url;
    }

    public String getDisplay_Name() {
        return Display_Name;
    }

    public void setDisplay_Name(String display_Name) {
        Display_Name = display_Name;
    }

    public int getPayroll_Integrated() {
        return Payroll_Integrated;
    }

    public void setPayroll_Integrated(int payroll_Integrated) {
        Payroll_Integrated = payroll_Integrated;
    }
}
