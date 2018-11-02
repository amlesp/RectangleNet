package com.rectangle.net.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "colormodel")
public class ColorModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("hex")
    private String hex;
    @SerializedName("name")
    private String name;
    @SerializedName("rgb")
    private String rgb;

    @Ignore
    public ColorModel() {
    }

    public ColorModel(String hex, String name, String rgb) {
        this.hex = hex;
        this.name = name;
        this.rgb = rgb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }
}
