package com.ssdut411.app.bookbar.activity.show;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 2016/10/23.
 */
public class DBFile {
    private static List<Location> locationList = new ArrayList<>();
    private static List<BookInfo> bookInfoList = new ArrayList<>();

    public static void addLocation(Location location){
        locationList.add(location);
    }

    public static Location searchLocation(List<WifiInfo> wifiInfoList){
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

    private static int calcDis(List<WifiInfo> list1, List<WifiInfo> list2) {
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

    public static void addBook(String bookName, Location location){
        BookInfo bookInfo = new BookInfo(bookName,location);
        bookInfoList.add(bookInfo);
    }

    public static Location searchBook(String book){
        for(BookInfo bookInfo:bookInfoList){
            if(bookInfo.getBookName().equals(book)){
                return bookInfo.getBookLocation();
            }
        }
        return null;
    }
}
