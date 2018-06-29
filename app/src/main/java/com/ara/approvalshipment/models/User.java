package com.ara.approvalshipment.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private int userId;
    private String loginId;
    private String userName;
    private String password;
    private int godownId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGodownId() {
        return godownId;
    }

    public void setGodownId(int godownId) {
        this.godownId = godownId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User fromGson(String message) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(message, User.class);
    }

    public static User fromJson(String message) {
        try {
            JSONArray jsonArray = new JSONArray(message);
            User user = new User();
            JSONObject reader = jsonArray.getJSONObject(0);

            user.userId = Integer.parseInt(reader.getString("userid"));
            user.userName = reader.getString("username");
            user.godownId = reader.getInt("godown_id");

            return user;
        } catch (JSONException jsonException) {
            return null;
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
