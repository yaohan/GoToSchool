package com.ssdut411.app.bookbar.volley.core;

import android.content.Context;

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
import com.ssdut411.app.bookbar.model.Resp.SearchResp;
import com.ssdut411.app.bookbar.model.Resp.UploadBookResp;
import com.ssdut411.app.bookbar.model.Resp.UploadPrintResp;
import com.ssdut411.app.bookbar.utils.GsonUtils;
import com.ssdut411.app.bookbar.volley.api.Api;
import com.ssdut411.app.bookbar.volley.api.ApiCallbackListener;
import com.ssdut411.app.bookbar.volley.api.ApiConfig;
import com.ssdut411.app.bookbar.volley.api.ApiImpl;

/**
 * AppAction接口的实现
 * <p/>
 * Created by yao_han on 2015/11/24.
 */
public class AppActionImpl implements AppAction {
    private Context context;
    private Api api;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void login(LoginReq req, final ActionCallbackListener<LoginResp> listener) {
        String url = ApiConfig.BASE_URL + "/login";
        String reqJson = req.toGetFormat();

        api.login(url, reqJson, context, new ApiCallbackListener<LoginResp>() {
            @Override
            public void onSuccess(LoginResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void forgetPassword(ForgetPasswordReq req, final ActionCallbackListener<ForgetPasswordResp> listener) {
        String url = ApiConfig.BASE_URL + "/forgetPassword";
        String reqJson = req.toGetFormat();

        api.forgetPassword(url, reqJson, context, new ApiCallbackListener<ForgetPasswordResp>() {
            @Override
            public void onSuccess(ForgetPasswordResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void register(RegisterReq req, final ActionCallbackListener<RegisterResp> listener) {
        String url = ApiConfig.BASE_URL + "/register";
        String reqJson = req.toGetFormat();

        api.register(url, reqJson, context, new ApiCallbackListener<RegisterResp>() {
            @Override
            public void onSuccess(RegisterResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void createBook(CreateBookReq req, final ActionCallbackListener<CreateBookResp> listener) {
        String url = ApiConfig.BASE_URL + "/createBook";
        String reqJson = req.toGetFormat();

        api.createBook(url, reqJson, context, new ApiCallbackListener<CreateBookResp>() {
            @Override
            public void onSuccess(CreateBookResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getRecommendBooks(GetRecommendBooksReq req, final ActionCallbackListener<GetRecommendBooksResp> listener) {
        String url = ApiConfig.BASE_URL + "/getRecommendBooks";
        String reqJson = req.toGetFormat();

        api.getRecommendBooks(url, reqJson, context, new ApiCallbackListener<GetRecommendBooksResp>() {
            @Override
            public void onSuccess(GetRecommendBooksResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getBookByISBN(GetBookByISBNReq req, final ActionCallbackListener<GetBookByISBNResp> listener) {
        String url = ApiConfig.BASE_URL + "/getBookByISBN";
        String reqJson = req.toGetFormat();
        api.getBookByISBN(url, reqJson, context, new ApiCallbackListener<GetBookByISBNResp>() {
            @Override
            public void onSuccess(GetBookByISBNResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getBookById(GetBookByIdReq req, final ActionCallbackListener<GetBookByIdResp> listener) {
        String url = ApiConfig.BASE_URL + "/getBookById";
        String reqJson = req.toGetFormat();
        api.getBookById(url, reqJson, context, new ApiCallbackListener<GetBookByIdResp>() {
            @Override
            public void onSuccess(GetBookByIdResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void search(SearchReq req, final ActionCallbackListener<SearchResp> listener) {
        String url = ApiConfig.BASE_URL + "/search";
        String reqJson = req.toGetFormat();
        api.search(url, reqJson, context, new ApiCallbackListener<SearchResp>() {
            @Override
            public void onSuccess(SearchResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void collectionBook(CollectionBookReq req, final ActionCallbackListener<CollectionBookResp> listener) {
        String url = ApiConfig.BASE_URL + "/collectionBook";
        String reqJson = req.toGetFormat();
        api.collectionBook(url, reqJson, context, new ApiCallbackListener<CollectionBookResp>() {
            @Override
            public void onSuccess(CollectionBookResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getCollection(GetCollectionReq req, final ActionCallbackListener<GetCollectionResp> listener) {
        String url = ApiConfig.BASE_URL + "/getCollection";
        String reqJson = req.toGetFormat();
        api.getCollection(url, reqJson, context, new ApiCallbackListener<GetCollectionResp>() {
            @Override
            public void onSuccess(GetCollectionResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void reservationBook(ReservationBookReq req, final ActionCallbackListener<ReservationBookResp> listener) {
        String url = ApiConfig.BASE_URL + "/reservationBook";
        String reqJson = req.toGetFormat();
        api.reservationBook(url, reqJson, context, new ApiCallbackListener<ReservationBookResp>() {
            @Override
            public void onSuccess(ReservationBookResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getReservation(GetReservationReq req, final ActionCallbackListener<GetReservationResp> listener) {
        String url = ApiConfig.BASE_URL + "/getReservation";
        String reqJson = req.toGetFormat();
        api.getReservation(url, reqJson, context, new ApiCallbackListener<GetReservationResp>() {
            @Override
            public void onSuccess(GetReservationResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void borrowBook(BorrowBookReq req, final ActionCallbackListener<BorrowBookResp> listener) {
        String url = ApiConfig.BASE_URL + "/borrowBook";
        String reqJson = req.toGetFormat();
        api.borrowBook(url, reqJson, context, new ApiCallbackListener<BorrowBookResp>() {
            @Override
            public void onSuccess(BorrowBookResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getBorrow(GetBorrowReq req, final ActionCallbackListener<GetBorrowResp> listener) {
        String url = ApiConfig.BASE_URL + "/getBorrow";
        String reqJson = req.toGetFormat();
        api.getBorrow(url, reqJson, context, new ApiCallbackListener<GetBorrowResp>() {
            @Override
            public void onSuccess(GetBorrowResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getLibrary(GetLibraryReq req, final ActionCallbackListener<GetLibraryResp> listener) {
        String url = ApiConfig.BASE_URL + "/getLibrary";
        String reqJson = req.toGetFormat();
        api.getLibrary(url, reqJson, context, new ApiCallbackListener<GetLibraryResp>() {
            @Override
            public void onSuccess(GetLibraryResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void uploadPrint(UploadPrintReq req, final ActionCallbackListener<UploadPrintResp> listener) {
        String url = ApiConfig.BASE_URL + "/uploadPrint";
        String reqJson = req.toGetFormat();
        api.uploadPrint(url, reqJson, context, new ApiCallbackListener<UploadPrintResp>() {
            @Override
            public void onSuccess(UploadPrintResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void uploadBook(UploadBookReq req, final ActionCallbackListener<UploadBookResp> listener) {
        String url = ApiConfig.BASE_URL + "/uploadBook";
        String reqJson = req.toGetFormat();
        api.uploadBook(url, reqJson, context, new ApiCallbackListener<UploadBookResp>() {
            @Override
            public void onSuccess(UploadBookResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });
    }

    @Override
    public void getLocation(GetLocationReq req, final ActionCallbackListener<GetLocationResp> listener) {
        String url = ApiConfig.BASE_URL + "/getLocation";
        String reqJson = req.toGetFormat();
        api.getLocation(url, reqJson, context, new ApiCallbackListener<GetLocationResp>() {
            @Override
            public void onSuccess(GetLocationResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });

    }

    @Override
    public void getBookLocation(GetBookLocationReq req, final ActionCallbackListener<GetBookLocationResp> listener) {
        String url = ApiConfig.BASE_URL + "/getBookLocation";
        String reqJson = req.toGetFormat();
        api.getBookLocation(url, reqJson, context, new ApiCallbackListener<GetBookLocationResp>() {
            @Override
            public void onSuccess(GetBookLocationResp data) {
                listener.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                listener.onFailure(message);
            }
        });

    }

}



