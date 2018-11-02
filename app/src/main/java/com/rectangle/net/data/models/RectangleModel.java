package com.rectangle.net.data.models;


public class RectangleModel {

    private int id;
    private String color;
    private int x;
    private int y;
    private boolean selected;

    public RectangleModel() {
    }

    public RectangleModel(int id, String color, int x, int y, boolean selected) {
        this.id = id;
        this.color = color;
        this.x = x;
        this.y = y;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "RectangleModel{" +
                "id=" + id +
                ", color='" + color + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", selected=" + selected +
                '}';
    }
}
