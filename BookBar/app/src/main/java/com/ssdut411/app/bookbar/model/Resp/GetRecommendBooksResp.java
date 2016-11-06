package com.ssdut411.app.bookbar.model.Resp;

import com.ssdut411.app.bookbar.model.Book;

import java.util.List;

/**
 * Created by LENOVO on 2016/11/4.
 */
public class GetRecommendBooksResp extends BaseResp {
    private List<Book> list;

    public GetRecommendBooksResp() {
    }

    public List<Book> getList() {
        return list;
    }

    public void setList(List<Book> list) {
        this.list = list;
    }
}
