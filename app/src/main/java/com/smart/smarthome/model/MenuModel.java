package com.smart.smarthome.model;

public class MenuModel {
    private String icon,menuad,menuid,hour;
    private int seekbar,onoff;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMenuad() {
        return menuad;
    }

    public void setMenuad(String menuad) {
        this.menuad = menuad;
    }

    public int getSeekbar() {
        return seekbar;
    }

    public void setSeekbar(int seekbar) {
        this.seekbar = seekbar;
    }

    public int getOnoff() {
        return onoff;
    }

    public void setOnoff(int onoff) {
        this.onoff = onoff;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
