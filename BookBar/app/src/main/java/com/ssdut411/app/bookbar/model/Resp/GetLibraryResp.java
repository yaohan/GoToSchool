package com.ssdut411.app.bookbar.model.Resp;

import com.ssdut411.app.bookbar.model.Library;

import java.util.List;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetLibraryResp extends BaseResp {
    private List<Library> list;

    public GetLibraryResp() {
    }

    public List<Library> getList() {
        return list;
    }

    public void setList(List<Library> list) {
        this.list = list;
    }
}
