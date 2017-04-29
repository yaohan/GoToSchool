package com.ssdut411.yaoyaoassistant.model.resp;

import com.ssdut411.yaoyaoassistant.model.Account;
import com.ssdut411.yaoyaoassistant.model.Tag;

import java.util.List;

/**
 * Created by yao_han on 2017/4/23.
 */
public class ListResp extends BaseResp {
    private ListInfo data;

    public ListResp() {
    }

    public ListInfo getData() {
        return data;
    }

    public void setData(ListInfo data) {
        this.data = data;
    }

    public class ListInfo{
        private List<Account> account;
        private List<Tag> tag;

        public ListInfo() {
        }

        public List<Account> getAccount() {
            return account;
        }

        public void setAccount(List<Account> account) {
            this.account = account;
        }

        public List<Tag> getTag() {
            return tag;
        }

        public void setTag(List<Tag> tag) {
            this.tag = tag;
        }
    }
}
