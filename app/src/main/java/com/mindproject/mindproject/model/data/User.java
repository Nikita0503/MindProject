package com.mindproject.mindproject.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

  @SerializedName("id")
  @Expose
  public Integer id;
  @SerializedName("username")
  @Expose
  public Object username;
  @SerializedName("phone")
  @Expose
  public Object phone;
  @SerializedName("avatar")
  @Expose
  public Object avatar;
  @SerializedName("karma")
  @Expose
  public Integer karma;

}