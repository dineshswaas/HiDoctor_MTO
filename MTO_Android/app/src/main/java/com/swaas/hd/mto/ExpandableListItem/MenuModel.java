package com.swaas.hd.mto.ExpandableListItem;


public class MenuModel {

    public String menuName, url;
    public boolean hasChildren, isGroup;

    public MenuModel(){

    }

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String url) {

        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
