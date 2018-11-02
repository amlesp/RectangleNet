package com.rectangle.net.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.rectangle.net.data.models.ColorModel;
import com.rectangle.net.data.models.SettingsModel;

import java.util.List;

@Dao
public interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertColor(ColorModel colorModel);

    @Query("SELECT * FROM colormodel")
    List<ColorModel> getColorModelList();

    @Query("DELETE FROM colormodel")
    void deleteColorTable();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSettings(SettingsModel settingsModel);

    @Query("SELECT * FROM settings WHERE id=:id")
    SettingsModel getSettings(int id);
}
