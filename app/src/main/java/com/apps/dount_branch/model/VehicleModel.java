package com.apps.dount_branch.model;

import java.io.Serializable;

public class VehicleModel implements Serializable {
    private String id;
    private String title;
    private String icon;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }
}
