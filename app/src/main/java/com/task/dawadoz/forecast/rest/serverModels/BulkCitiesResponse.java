

package com.task.dawadoz.forecast.rest.serverModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BulkCitiesResponse {

    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<com.task.dawadoz.forecast.rest.serverModels.innerModels.List> list = null;

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public java.util.List<com.task.dawadoz.forecast.rest.serverModels.innerModels.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.task.dawadoz.forecast.rest.serverModels.innerModels.List> list) {
        this.list = list;
    }

}


