package com.swaas.hd.mto.RetrofitBuilder;

public class UserTypeMenus {
    private String Menu_Name;
    private int Menu_Id;
    private String MDM_Menu_Url;


    public String getMenu_Name() {
        return Menu_Name;
    }

    public void setMenu_Name(String menu_Name) {
        Menu_Name = menu_Name;
    }


    public int getMenu_Id() {
        return Menu_Id;
    }

    public void setMenu_Id(int menu_Id) {
        Menu_Id = menu_Id;
    }

    public String getMDM_Menu_Url() {
        return MDM_Menu_Url;
    }

    public void setMDM_Menu_Url(String MDM_Menu_Url) {
        this.MDM_Menu_Url = MDM_Menu_Url;
    }
}

