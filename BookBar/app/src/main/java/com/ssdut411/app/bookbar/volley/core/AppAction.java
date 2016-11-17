package com.ssdut411.app.bookbar.volley.core;

import com.ssdut411.app.bookbar.model.Req.BorrowBookReq;
import com.ssdut411.app.bookbar.model.Req.CollectionBookReq;
import com.ssdut411.app.bookbar.model.Req.CreateBookReq;
import com.ssdut411.app.bookbar.model.Req.ForgetPasswordReq;
import com.ssdut411.app.bookbar.model.Req.GetBookByISBNReq;
import com.ssdut411.app.bookbar.model.Req.GetBookByIdReq;
import com.ssdut411.app.bookbar.model.Req.GetBookLocationReq;
import com.ssdut411.app.bookbar.model.Req.GetBorrowReq;
import com.ssdut411.app.bookbar.model.Req.GetCollectionReq;
import com.ssdut411.app.bookbar.model.Req.GetLibraryReq;
import com.ssdut411.app.bookbar.model.Req.GetLocationReq;
import com.ssdut411.app.bookbar.model.Req.GetRecommendBooksReq;
import com.ssdut411.app.bookbar.model.Req.GetReservationReq;
import com.ssdut411.app.bookbar.model.Req.LoginReq;
import com.ssdut411.app.bookbar.model.Req.RegisterReq;
import com.ssdut411.app.bookbar.model.Req.ReservationBookReq;
import com.ssdut411.app.bookbar.model.Req.ReturnBookReq;
import com.ssdut411.app.bookbar.model.Req.SearchReq;
import com.ssdut411.app.bookbar.model.Req.UploadBookReq;
import com.ssdut411.app.bookbar.model.Req.UploadPrintReq;
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
import com.ssdut411.app.bookbar.model.Resp.ReturnBookResp;
import com.ssdut411.app.bookbar.model.Resp.SearchResp;
import com.ssdut411.app.bookbar.model.Resp.UploadBookResp;
import com.ssdut411.app.bookbar.model.Resp.UploadPrintResp;

/**
 * 接收App层的各种Action
 *
 * Created by yao_han on 2015/11/24.
 */
public interface AppAction {
    public void login(LoginReq req, ActionCallbackListener<LoginResp> listener);
    public void forgetPassword(ForgetPasswordReq req, ActionCallbackListener<ForgetPasswordResp> listener);
    public void register(RegisterReq req, ActionCallbackListener<RegisterResp> listener);
    public void createBook(CreateBookReq req, ActionCallbackListener<CreateBookResp> listener);
    public void getRecommendBooks(GetRecommendBooksReq req, ActionCallbackListener<GetRecommendBooksResp> listener);
    public void getBookByISBN(GetBookByISBNReq req, ActionCallbackListener<GetBookByISBNResp> listener);
    public void getBookById(GetBookByIdReq req, ActionCallbackListener<GetBookByIdResp> listener);
    public void search(SearchReq req, ActionCallbackListener<SearchResp> listener);
    public void collectionBook(CollectionBookReq req, ActionCallbackListener<CollectionBookResp> listener);
    public void getCollection(GetCollectionReq req, ActionCallbackListener<GetCollectionResp> listener);
    public void reservationBook(ReservationBookReq req, ActionCallbackListener<ReservationBookResp> listener);
    public void getReservation(GetReservationReq req, ActionCallbackListener<GetReservationResp> listener);
    public void borrowBook(BorrowBookReq req, ActionCallbackListener<BorrowBookResp> listener);
    public void getBorrow(GetBorrowReq req, ActionCallbackListener<GetBorrowResp> listener);
    public void getLibrary(GetLibraryReq req, ActionCallbackListener<GetLibraryResp> listener);
    public void uploadPrint(UploadPrintReq req, ActionCallbackListener<UploadPrintResp> listener);
    public void uploadBook(UploadBookReq req, ActionCallbackListener<UploadBookResp> listener);
    public void getLocation(GetLocationReq req, ActionCallbackListener<GetLocationResp> listener);
    public void getBookLocation(GetBookLocationReq req, ActionCallbackListener<GetBookLocationResp> listener);
    public void returnBook(ReturnBookReq req, ActionCallbackListener<ReturnBookResp> listener);
}
