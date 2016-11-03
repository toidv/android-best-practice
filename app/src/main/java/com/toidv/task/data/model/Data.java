package com.toidv.task.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("data")
    @Expose
    private List<Task> data = new ArrayList<Task>();

    /**
     * @return The data
     */
    public List<Task> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Task> data) {
        this.data = data;
    }

}
