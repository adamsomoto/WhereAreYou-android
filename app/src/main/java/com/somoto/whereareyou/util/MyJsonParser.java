package com.somoto.whereareyou.util;


import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class MyJsonParser {

    public static <T> List<T> parseJsonArray(String response, Class<T> clazz) {
        try {
            List<T> answer = new ArrayList<>();
            if (response == null || response.equals("[]")) {
                return answer;
            }
            Gson gson = new Gson();
            com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
            JsonArray jsonArr = (JsonArray)jsonParser.parse(response);
            for(int i=0; i<jsonArr.size(); i++){
                T t = gson.fromJson(jsonArr.get(i), clazz);
                answer.add(t);
            }
            return answer;
        }
        catch (Exception e){
        }
        return new ArrayList<>();
    }

}