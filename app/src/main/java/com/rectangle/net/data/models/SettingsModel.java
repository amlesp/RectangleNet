package com.rectangle.net.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.rectangle.net.data.db.DataTypeConverter;

import java.util.List;

@Entity(tableName = "settings")
public class SettingsModel {

    @PrimaryKey
    private int id = 1;
    private int rows;
    private int columns;
    @TypeConverters(DataTypeConverter.class)
    private List<Integer> selectedRect;
    @TypeConverters(DataTypeConverter.class)
    private List<RectangleModel> rectangleModels;

    @Ignore
    public SettingsModel() {
    }

    public SettingsModel(int rows, int columns, List<Integer> selectedRect, List<RectangleModel> rectangleModels) {
        this.rows = rows;
        this.columns = columns;
        this.selectedRect = selectedRect;
        this.rectangleModels = rectangleModels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<Integer> getSelectedRect() {
        return selectedRect;
    }

    public void setSelectedRect(List<Integer> selectedRect) {
        this.selectedRect = selectedRect;
    }

    public List<RectangleModel> getRectangleModels() {
        return rectangleModels;
    }

    public void setRectangleModels(List<RectangleModel> rectangleModels) {
        this.rectangleModels = rectangleModels;
    }
}
