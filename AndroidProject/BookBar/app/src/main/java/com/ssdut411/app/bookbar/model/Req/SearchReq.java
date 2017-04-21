package com.ssdut411.app.bookbar.model.Req;

/**
 * Created by LENOVO on 2016/11/5.
 */
public class SearchReq extends BaseReq {
    private String keyWord;

    public SearchReq() {
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
