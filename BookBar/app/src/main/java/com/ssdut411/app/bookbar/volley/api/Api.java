package com.ssdut411.app.bookbar.volley.api;


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

/**
 * Api接口
 * <p/>
 * Created by yao_han on 2015/11/24.
 */
public interface Api {
    public void login(String url, String reqJson, Object tag, ApiCallbackListener<LoginResp> listener);
    public void forgetPassword(String url, String reqJson, Object tag, ApiCallbackListener<ForgetPasswordResp> listener);
    public void register(String url, String reqJson, Object tag, ApiCallbackListener<RegisterResp> listener);
    public void createBook(String url, String reqJson, Object tag, ApiCallbackListener<CreateBookResp> listener);
    public void getRecommendBooks(String url, String reqJson, Object tag, ApiCallbackListener<GetRecommendBooksResp> listener);
    public void getBookByISBN(String url, String reqJson, Object tag, ApiCallbackListener<GetBookByISBNResp> listener);
    public void getBookById(String url, String reqJson, Object tag, ApiCallbackListener<GetBookByIdResp> listener);
    public void search(String url, String reqJson, Object tag, ApiCallbackListener<SearchResp> listener);
    public void collectionBook(String url, String reqJson, Object tag, ApiCallbackListener<CollectionBookResp> listener);
    public void getCollection(String url, String reqJson, Object tag, ApiCallbackListener<GetCollectionResp> listener);
    public void reservationBook(String url, String reqJson, Object tag, ApiCallbackListener<ReservationBookResp> listener);
    public void getReservation(String url, String reqJson, Object tag, ApiCallbackListener<GetReservationResp> listener);
    public void borrowBook(String url, String reqJson, Object tag, ApiCallbackListener<BorrowBookResp> listener);
    public void getBorrow(String url, String reqJson, Object tag, ApiCallbackListener<GetBorrowResp> listener);
    public void getLibrary(String url, String reqJson, Object tag, ApiCallbackListener<GetLibraryResp> listener);
    public void uploadPrint(String url, String reqJson, Object tag, ApiCallbackListener<UploadPrintResp> listener);
    public void uploadBook(String url, String reqJson, Object tag, ApiCallbackListener<UploadBookResp> listener);
    public void getLocation(String url, String reqJson, Object tag, ApiCallbackListener<GetLocationResp> listener);
    public void getBookLocation(String url, String reqJson, Object tag, ApiCallbackListener<GetBookLocationResp> listener);

}
