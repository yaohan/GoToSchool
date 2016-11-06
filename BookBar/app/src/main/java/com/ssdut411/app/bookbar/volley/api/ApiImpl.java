package com.ssdut411.app.bookbar.volley.api;

import com.android.volley.toolbox.Volley;
import com.ssdut411.app.bookbar.model.Resp.BorrowBookResp;
import com.ssdut411.app.bookbar.model.Resp.CollectionBookResp;
import com.ssdut411.app.bookbar.model.Resp.CreateBookResp;
import com.ssdut411.app.bookbar.model.Resp.ForgetPasswordResp;
import com.ssdut411.app.bookbar.model.Resp.GetBookByISBNResp;
import com.ssdut411.app.bookbar.model.Resp.GetBookByIdResp;
import com.ssdut411.app.bookbar.model.Resp.GetBookLocationResp;
import com.ssdut411.app.bookbar.model.Resp.GetBorrowResp;
import com.ssdut411.app.bookbar.model.Resp.GetCollectionResp;
import com.ssdut411.app.bookbar.model.Resp.GetLibraryResp;
import com.ssdut411.app.bookbar.model.Resp.GetLocationResp;
import com.ssdut411.app.bookbar.model.Resp.GetRecommendBooksResp;
import com.ssdut411.app.bookbar.model.Resp.GetReservationResp;
import com.ssdut411.app.bookbar.model.Resp.LoginResp;
import com.ssdut411.app.bookbar.model.Resp.RegisterResp;
import com.ssdut411.app.bookbar.model.Resp.ReservationBookResp;
import com.ssdut411.app.bookbar.model.Resp.SearchResp;
import com.ssdut411.app.bookbar.model.Resp.UploadBookResp;
import com.ssdut411.app.bookbar.model.Resp.UploadPrintResp;
import com.ssdut411.app.bookbar.volley.VolleyUtil;

/**
 * Api接口实现类
 *
 * Created by yao_han on 2015/11/24.
 */
public class ApiImpl implements Api{

    @Override
    public void login(String url, String reqJson, Object tag, ApiCallbackListener<LoginResp> listener) {
        VolleyUtil.doGet(url+ reqJson, LoginResp.class, tag, listener);
    }

    @Override
    public void forgetPassword(String url, String reqJson, Object tag, ApiCallbackListener<ForgetPasswordResp> listener) {
        VolleyUtil.doGet(url+reqJson, ForgetPasswordResp.class, tag, listener);
    }

    @Override
    public void register(String url, String reqJson, Object tag, ApiCallbackListener<RegisterResp> listener) {
        VolleyUtil.doGet(url+reqJson, RegisterResp.class, tag, listener);
    }

    @Override
    public void createBook(String url, String reqJson, Object tag, ApiCallbackListener<CreateBookResp> listener) {
        VolleyUtil.doGet(url+reqJson, CreateBookResp.class, tag, listener);
    }

    @Override
    public void getRecommendBooks(String url, String reqJson, Object tag, ApiCallbackListener<GetRecommendBooksResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetRecommendBooksResp.class, tag, listener);
    }

    @Override
    public void getBookByISBN(String url, String reqJson, Object tag, ApiCallbackListener<GetBookByISBNResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetBookByISBNResp.class, tag, listener);
    }

    @Override
    public void getBookById(String url, String reqJson, Object tag, ApiCallbackListener<GetBookByIdResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetBookByIdResp.class, tag, listener);
    }

    @Override
    public void search(String url, String reqJson, Object tag, ApiCallbackListener<SearchResp> listener) {
        VolleyUtil.doGet(url+reqJson, SearchResp.class, tag, listener);
    }

    @Override
    public void collectionBook(String url, String reqJson, Object tag, ApiCallbackListener<CollectionBookResp> listener) {
        VolleyUtil.doGet(url+reqJson, CollectionBookResp.class, tag, listener);
    }

    @Override
    public void getCollection(String url, String reqJson, Object tag, ApiCallbackListener<GetCollectionResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetCollectionResp.class, tag, listener);
    }

    @Override
    public void reservationBook(String url, String reqJson, Object tag, ApiCallbackListener<ReservationBookResp> listener) {
        VolleyUtil.doGet(url+reqJson, ReservationBookResp.class, tag, listener);
    }

    @Override
    public void getReservation(String url, String reqJson, Object tag, ApiCallbackListener<GetReservationResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetReservationResp.class, tag, listener);
    }

    @Override
    public void borrowBook(String url, String reqJson, Object tag, ApiCallbackListener<BorrowBookResp> listener) {
        VolleyUtil.doGet(url+reqJson, BorrowBookResp.class, tag, listener);
    }

    @Override
    public void getBorrow(String url, String reqJson, Object tag, ApiCallbackListener<GetBorrowResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetBorrowResp.class, tag, listener);
    }

    @Override
    public void getLibrary(String url, String reqJson, Object tag, ApiCallbackListener<GetLibraryResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetLibraryResp.class, tag, listener);
    }

    @Override
    public void uploadPrint(String url, String reqJson, Object tag, ApiCallbackListener<UploadPrintResp> listener) {
        VolleyUtil.doGet(url+reqJson, UploadPrintResp.class, tag, listener);
    }

    @Override
    public void uploadBook(String url, String reqJson, Object tag, ApiCallbackListener<UploadBookResp> listener) {
        VolleyUtil.doGet(url+reqJson, UploadBookResp.class, tag, listener);
    }

    @Override
    public void getLocation(String url, String reqJson, Object tag, ApiCallbackListener<GetLocationResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetLocationResp.class, tag, listener);
    }

    @Override
    public void getBookLocation(String url, String reqJson, Object tag, ApiCallbackListener<GetBookLocationResp> listener) {
        VolleyUtil.doGet(url+reqJson, GetBookLocationResp.class, tag, listener);
    }

}
