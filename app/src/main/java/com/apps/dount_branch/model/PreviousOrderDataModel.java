package com.apps.dount_branch.model;

import java.io.Serializable;
import java.util.List;

public class PreviousOrderDataModel extends StatusResponse implements Serializable {
    private List<PreviousOrderModel> data;

    public List<PreviousOrderModel> getData() {
        return data;
    }
}
