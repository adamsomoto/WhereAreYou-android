package com.somoto.whereareyou.internet;

public interface InternetDataListener<T> {

    void handleData(T data);

}