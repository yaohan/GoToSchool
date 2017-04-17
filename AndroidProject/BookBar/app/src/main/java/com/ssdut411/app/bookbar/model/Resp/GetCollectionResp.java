package com.ssdut411.app.bookbar.model.Resp;

import com.ssdut411.app.bookbar.model.BookModel;

import java.util.List;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class GetCollectionResp extends BaseResp {
    private List<BookModel> list;
    public GetCollectionResp() {
    }

    public List<BookModel> getList() {
        return list;
    }

    public void setList(List<BookModel> list) {
        this.list = list;
    }
}
