package com.ssdut411.app.bookbarstatic.activity.show;

import android.content.Context;

import com.google.gson.Gson;
import com.ssdut411.app.bookbarstatic.activity.system.MainApplication;
import com.ssdut411.app.bookbarstatic.model.Book;
import com.ssdut411.app.bookbarstatic.model.BookModel;
import com.ssdut411.app.bookbarstatic.utils.GsonUtils;
import com.ssdut411.app.bookbarstatic.utils.L;
import com.ssdut411.app.bookbarstatic.utils.SPUtils;
import com.ssdut411.app.bookbarstatic.utils.T;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/23.
 */
public class DBFile {
    private static DBFile instance;
    private List<Location> locationList;
    private List<BookModel> bookModelList;
    private List<BookModel> borrowList;
    private List<BookModel> collectionList;
    private List<BookModel> reservationList;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:MM:SS");
    private Context context;
    public static DBFile getInstance() {
        if(instance == null){
            instance = new DBFile();
        }
        return instance;
    }

    public void clear(){
        SPUtils.remove(context,"location");
        locationList = new ArrayList<>();
        T.showShort(context,"清除缓存");
    }
    public void init(Context context){
        this.context = context;
        String locations = SPUtils.get(context,"location","").toString();
        if(locations.length() == 0){
            locationList = new ArrayList<>();
        }else{
            locationList = GsonUtils.gsonToList(locations,Location.class);
        }
        String bookModels = SPUtils.get(context,"bookmodel","").toString();
        L.i("bookModels:"+bookModels);
        if(bookModels.length() == 0){
            bookModelList = new ArrayList<>();
            BookModel bookModel = new BookModel();
            bookModel.setTitle("FPA性格色彩入门");
            bookModel.setAuthor("乐嘉");
            bookModel.setPublisher("湖南文艺出版社");
            bookModel.setSummary("《FPA性格色彩入门:跟乐嘉色眼识人》是一本实用的工具书，乐嘉把人分成红、蓝、黄、绿四种颜色，每种颜色代表每种性格，然后进一步进行剖析。本书包括三个部分，第一部分是总论，第二部分是分则。总论部分主要从一个作者潜心研究的测试题入手，快速的让自己判断属于哪种颜色。同时解答了性格色彩与人们常见的内向还是外向，理性还是感性的区别。分则部分则从情感、生活、工作、处事、以及自我提升方面说明红、蓝、黄、绿的优势与过当。同时对一些人生观和价值观的取向给出了背后的性格分析。第三部分作者首次解答性格色彩的应用中所遇到的问题，以及个案的精彩点评。帮助我们找到真正的我自己。");
            bookModel.setUrl("https://img3.doubanio.com/lpic/s10232883.jpg");
            bookModel.setLocationX(551.4894f);
            bookModel.setLocationY(911.4164f);
            bookModel.setIsbn13("9787540454487");
            bookModel.setNumber(1);
            bookModelList.add(bookModel);
        }else{
            bookModelList = GsonUtils.gsonToList(bookModels,BookModel.class);
        }
        String borrows = SPUtils.get(context,"borrow","").toString();
        if(borrows.length() == 0){
            borrowList = new ArrayList<>();
        }else{
            borrowList = GsonUtils.gsonToList(borrows,BookModel.class);
        }

        String collections = SPUtils.get(context,"collection","").toString();
        L.i("get collection:"+collections.length());
        if(collections.length() == 0){
            collectionList = new ArrayList<>();
        }else{
            collectionList = GsonUtils.gsonToList(collections,BookModel.class);
        }
        L.i("get collection:"+GsonUtils.gsonToJsonString(collectionList));

        String reservations = SPUtils.get(context,"reservation","").toString();
        if(reservations.length() == 0){
            reservationList = new ArrayList<>();
        }else{
            reservationList = GsonUtils.gsonToList(reservations,BookModel.class);
        }
    }

    public void addLocation(Location location){
        locationList.add(location);
        SPUtils.put(context,"location",GsonUtils.gsonToJsonString(locationList));
    }

    public Location searchLocation(List<WifiInfo> wifiInfoList){
        Location min = null;
        int minValue = 100000;
        for (int loc = 0; loc < locationList.size(); loc++) {
            Location location = locationList.get(loc);
            int dis = calcDis(location.getWifiInfo(), wifiInfoList);
            if (dis < minValue) {
                minValue = dis;
                min = location;
            }
        }
        return min;
    }

    private int calcDis(List<WifiInfo> list1, List<WifiInfo> list2) {
        int dis = 0;
        for (WifiInfo wifiInfo1 : list1) {
            for (WifiInfo wifiInfo2 : list2) {
                if (wifiInfo1.getBssid().equals(wifiInfo2.getBssid())) {
                    dis += Math.abs(wifiInfo1.getLevel() - wifiInfo2.getLevel());
                }
            }
        }
        return dis;
    }

    public List<BookModel> getRecommend(){
        return bookModelList;
    }

    public void addBook(Book book, float locationX, float locationY){
        BookModel bookModel = getBookByISBN(book.getIsbn13());
        if(bookModel == null){
            bookModel = new BookModel();
            bookModel.setBookId(bookModelList.size()+1);
            bookModel.setTitle(book.getTitle());
            bookModel.setAuthor(getAuthor(book.getAuthor()));
            bookModel.setPublisher(book.getPublisher());
            bookModel.setSummary(book.getSummary());
            bookModel.setNumber(1);
            bookModel.setIsbn13(book.getIsbn13());
            bookModel.setUrl(book.getImages().getLarge());
            bookModelList.add(bookModel);
            bookModel.setLocationX(locationX);
            bookModel.setLocationY(locationY);
        }else{
            bookModel.setNumber(bookModel.getNumber()+1);
        }
        SPUtils.put(context,"bookmodel",GsonUtils.gsonToJsonString(bookModelList));
    }

    private String getAuthor(List<String> author) {
        StringBuffer stringBuffer = new StringBuffer();
        Boolean first = true;
        for (String aut : author) {
            if (!first) {
                first = false;
                stringBuffer.append(",");
            }
            stringBuffer.append(aut);
        }
        return stringBuffer.toString();
    }

    public BookModel getBookByISBN(String isbn){
        for(BookModel bookModel:bookModelList){
            if(bookModel.getIsbn13().equals(isbn)){
                return bookModel;
            }
        }
        return null;
    }

    public void borrowBook(String isbn,float locationX, float locationY){
        BookModel bookModel = getBookByISBN(isbn);
        if(bookModel == null){
            T.showShort(context,"这本书不存在");
        }else if(bookModel.getNumber() > 0){
            bookModel.setNumber(bookModel.getNumber() - 1);
            BookModel newBook = bookModel;
            newBook.setLocationX(locationX);
            newBook.setLocationY(locationY);
            newBook.setTime(simpleDateFormat.format(new Date()));
            borrowList.add(newBook);
            SPUtils.put(context, "bookmodel", GsonUtils.gsonToJsonString(bookModelList));
            SPUtils.put(context,"borrow",GsonUtils.gsonToJsonString(borrowList));
            T.showShort(context,"借阅成功");
        }
    }

    public void returnBook(String isbn){
        BookModel bookModel = getBorrowByISBN(isbn);
        borrowList.remove(bookModel);
        BookModel book = getBookByISBN(isbn);
        book.setNumber(bookModel.getNumber() + 1);
        T.showShort(context,"还书成功");
        SPUtils.put(context, "bookmodel", GsonUtils.gsonToJsonString(bookModelList));
        SPUtils.put(context, "borrow", GsonUtils.gsonToJsonString(borrowList));
    }

    public List<BookModel> getBorrows(){
        return borrowList;
    }

    public BookModel getBorrowByISBN(String isbn){
        for(BookModel bookModel:borrowList){
            if(bookModel.getIsbn13().equals(isbn)){
                return bookModel;
            }
        }
        return null;
    }

    public boolean collectionBook(String isbn){
        BookModel bookModel = getBookByISBN(isbn);
        if(bookModel == null){
            T.showShort(context,"这本书不存在");
            return false;
        }else{
            if(getCollectionsByISBN(isbn) !=null){
                T.showShort(context,"你已经收藏过这本书了");
                return false;
            }else{
                BookModel newBook = bookModel;
                newBook.setTime(simpleDateFormat.format(new Date()));
                collectionList.add(newBook);
                SPUtils.put(context, "collection", GsonUtils.gsonToJsonString(collectionList));
                T.showShort(context, "收藏成功");
                L.i("put collection:" + GsonUtils.gsonToJsonString(collectionList));
                return true;
            }
        }
    }

    public List<BookModel> getCollections(){
        return collectionList;
    }
    private BookModel getCollectionsByISBN(String isbn){
        for(BookModel bookModel:collectionList){
            if(bookModel.getIsbn13().equals(isbn)){
                return bookModel;
            }
        }
        return null;
    }

    public boolean reservationBook(String isbn){
        BookModel bookModel = getBookByISBN(isbn);
        if(bookModel == null){
            T.showShort(context,"这本书不存在");
            return false;
        }else{
            if(getReservatinsByISBN(isbn) !=null){
                T.showShort(context,"你已经预约过这本书了");
                return false;
            }else{
                BookModel newBook = bookModel;
                newBook.setTime(simpleDateFormat.format(new Date()));
                collectionList.add(newBook);
                SPUtils.put(context, "reservation", GsonUtils.gsonToJsonString(reservationList));
                T.showShort(context, "预约成功");
                return true;
            }
        }
    }

    public List<BookModel> getReservations(){
        return reservationList;
    }
    private BookModel getReservatinsByISBN(String isbn){
        for(BookModel bookModel:reservationList){
            if(bookModel.getIsbn13().equals(isbn)){
                return bookModel;
            }
        }
        return null;
    }
    public List<BookModel> searchBook(String keyword){
        List<BookModel> list = new ArrayList<>();
        for(BookModel bookModel:bookModelList){
            String sum = bookModel.getTitle()+bookModel.getAuthor()+bookModel.getAuthor();
            if (sum.contains(keyword)){
                list.add(bookModel);
            }
        }
        return list;
    }

}
