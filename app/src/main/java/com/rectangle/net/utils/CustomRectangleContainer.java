package com.rectangle.net.utils;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rectangle.net.R;
import com.rectangle.net.data.db.AppExecutors;
import com.rectangle.net.data.models.RectangleModel;
import com.rectangle.net.data.models.SettingsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CustomRectangleContainer extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private List<RectangleModel> rectangleList;
    private List<String> colorList;
    private List<Integer> selectedItemList;
    private float density;
    private int rows;
    private int columns;
    private int sideA;
    private int sideB;

    public CustomRectangleContainer(Context context) {
        super(context);
        init(context);
    }

    public CustomRectangleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRectangleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.density = getResources().getDisplayMetrics().density;
        selectedItemList = new ArrayList<>();
    }

    public void startNew(List<String> colorList, int rows, int columns) {
        this.colorList = colorList;
        this.rows = rows;
        this.columns = columns;
        this.rectangleList = generateBlankList(rows, columns);
        this.rectangleList = addColorToList(colorList, rectangleList);
        //remove this if we want array from 0 to n
        Collections.shuffle(rectangleList);
        this.rectangleList = addCordinatesToList(rectangleList, rows, columns);

        drawRectangleListToView(mContext, rectangleList);
    }

    public void continuePrevious(SettingsModel settingsModel) {
        this.rectangleList = settingsModel.getRectangleModels();
        this.rows = settingsModel.getRows();
        this.columns = settingsModel.getColumns();
        this.selectedItemList = settingsModel.getSelectedRect();

        //delay execution 500ms until we get width and height
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                AppExecutors appExecutors = AppExecutors.getInstance();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        rectangleList = addCordinatesToList(rectangleList, rows, columns);
                        drawRectangleListToView(mContext, rectangleList);
                    }
                });

            }
        },500);
    }

    public void shuffle() {
        Collections.shuffle(rectangleList);
        rectangleList = addCordinatesToList(rectangleList, rows, columns);

        drawRectangleListToView(mContext, rectangleList);
    }

    public void replace() {
        if (selectedItemList.size() == 2) {
            int position1 = findItemInArrayById(rectangleList, selectedItemList.get(0));
            int position2 = findItemInArrayById(rectangleList, selectedItemList.get(1));
            RectangleModel object1 = rectangleList.get(position1);
            RectangleModel object2 = rectangleList.get(position2);
            rectangleList.set(position1, object2);
            rectangleList.set(position2, object1);

            rectangleList = addCordinatesToList(rectangleList, rows, columns);

            drawRectangleListToView(mContext, rectangleList);
        } else {
            Toast.makeText(mContext, R.string.rectangle_switch, Toast.LENGTH_SHORT).show();
        }
    }

    public void moveForward() {
        RectangleModel rectangleModel = rectangleList.get(rectangleList.size() - 1);
        rectangleList.remove(rectangleList.size() - 1);
        rectangleList.add(0, rectangleModel);
        rectangleList = addCordinatesToList(rectangleList, rows, columns);
        drawRectangleListToView(mContext, rectangleList);
    }

    public SettingsModel getSettings() {
        return new SettingsModel(rows, columns, selectedItemList, rectangleList);
    }

    /**
     * Generate List with required number of empty rect models based on
     * row and column number
     * **/
    private List<RectangleModel> generateBlankList(int rows, int columns) {
        List<RectangleModel> list = new ArrayList<>();
        for (int i = 0; i < rows * columns; i++) {
            RectangleModel rectangleModel = new RectangleModel();
            rectangleModel.setId(i);
            list.add(rectangleModel);
        }
        return list;
    }

    /**
     * Adding color to each rect Model in List in random order
     * from color List
     * **/
    private List<RectangleModel> addColorToList(List<String> colorList, List<RectangleModel> rectangleList) {
        Random random = new Random();
        for (RectangleModel rectangleModel : rectangleList) {
            rectangleModel.setColor(colorList.get(random.nextInt(colorList.size())));
        }

        return rectangleList;
    }

    /**
     * Calculating Rect sides size and adding cordinates to rect Model,
     * Call before drawing List (drawRectangleListToView()) in case user rotate screen
     * **/
    private List<RectangleModel> addCordinatesToList(List<RectangleModel> rectangleList, int row, int columns) {
        this.sideA = getWidth() / columns;
        this.sideB = getHeight() / row;

        int mRow = 0;
        int mColumn = 0;

        for (int i = 0; i < rectangleList.size(); i++) {

            RectangleModel rectangleModel = rectangleList.get(i);
            rectangleModel.setX(mColumn * sideA);
            rectangleModel.setY(mRow * sideB);
            Log.d("Coordinates", mColumn * sideA + "-" + mRow * sideB);

            if (mColumn == columns - 1) {
                mColumn = 0;
                mRow++;
            } else {
                mColumn++;
            }
        }

        return rectangleList;
    }

    private int findItemInArrayById(List<RectangleModel> rectangleList, int id) {
        for (int i = 0; i < rectangleList.size(); i++) {
            if (rectangleList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void drawRectangleListToView(Context context, List<RectangleModel> rectangleList) {
        removeAllViews();

        int borderWidth = (int)(4 * density);
        for (int i = 0; i < rectangleList.size(); i++) {
            RectangleModel rectangleModel = rectangleList.get(i);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(sideA, sideB);
            params.leftMargin = rectangleModel.getX();
            params.topMargin = rectangleModel.getY();

            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_rectangle, null);
            relativeLayout.setBackgroundColor(Color.parseColor(rectangleModel.getColor()));
            TextView textView = relativeLayout.findViewById(R.id.tv_rectangle_number);
            textView.setText(String.valueOf(rectangleModel.getId()+1));
            if (rectangleModel.isSelected()) {
                textView.setTextColor(Color.RED);
                Utility.drawBorder(relativeLayout, Color.parseColor(rectangleModel.getColor()), borderWidth, Color.RED);

            } else {
                textView.setTextColor(Color.WHITE);
            }
            relativeLayout.setId(rectangleModel.getId());
            relativeLayout.setOnClickListener(this);
            addView(relativeLayout, params);
        }

        invalidate();
    }

    @Override
    public void onClick(View view) {
        //add selecting logic
        int itemPosition = findItemInArrayById(rectangleList, view.getId());
        int itemId = rectangleList.get(itemPosition).getId();

        if (selectedItemList.contains(itemId)) {
            if (selectedItemList.get(0) == itemId) {
                selectedItemList.remove(0);
            } else {
                selectedItemList.remove(1);
            }
            rectangleList.get(itemPosition).setSelected(false);
        } else {
            if (selectedItemList.size() < 2) {
                selectedItemList.add(itemId);
                rectangleList.get(itemPosition).setSelected(true);
            } else {
                Utility.showSimpleDialog(mContext, null, mContext.getString(R.string.selection_rule));
            }
        }

        drawRectangleListToView(mContext, rectangleList);
    }
}
