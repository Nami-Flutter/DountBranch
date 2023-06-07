package com.apps.dount_branch.model;

import java.io.Serializable;
import java.util.List;

public class VehicleDataModel extends StatusResponse implements Serializable {
    private List<VehicleModel> data;

    public List<VehicleModel> getData() {
        return data;
    }
}
