package com.rectangle.net;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rectangle.net.data.db.AppDatabase;
import com.rectangle.net.data.db.AppExecutors;
import com.rectangle.net.data.models.ColorModel;
import com.rectangle.net.data.models.SettingsModel;
import com.rectangle.net.utils.CustomRectangleContainer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rl_rectangle_container)
    CustomRectangleContainer mRectangleContainer;
    @BindView(R.id.btn_replace)
    Button mReplace;
    private AppExecutors appExecutors;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDb = AppDatabase.getInstance(this);
        appExecutors = AppExecutors.getInstance();

        //save board settings when user rotate screen
        if (savedInstanceState == null) {
            chooseOptionDialog();
        } else {
            appExecutors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final SettingsModel settingsModel = mDb.dataDao().getSettings(1);

                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (settingsModel != null) {
                                mRectangleContainer.continuePrevious(settingsModel);
                            } else {
                                chooseOptionDialog();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onPause() {
        saveSettings();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            mRectangleContainer.shuffle();
            return true;
        }

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            mRectangleContainer.moveForward();
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    @OnClick(R.id.btn_replace)
    public void replace() {
        mRectangleContainer.replace();
    }

    //we can consider moving dialog creation into separate class
    private void chooseOptionDialog() {
        String[] items = new String[]{getResources().getString(R.string.start), getResources().getString(R.string.continue_game)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.select_option);
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setRowsAndColumnsNumbDialog();
                        break;
                    case 1:
                        appExecutors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                final SettingsModel settingsModel = mDb.dataDao().getSettings(1);

                                appExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (settingsModel != null) {
                                            mRectangleContainer.continuePrevious(settingsModel);
                                        } else {
                                            setRowsAndColumnsNumbDialog();
                                        }
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        });
        builder.show();
    }

    private void setRowsAndColumnsNumbDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final EditText etRowNumber = (EditText) dialogView.findViewById(R.id.row_number);
        final EditText etColumnNumber = (EditText) dialogView.findViewById(R.id.column_number);

        dialogBuilder.setTitle(R.string.initial_set);

        dialogBuilder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String r = etRowNumber.getText().toString().trim();
                String c = etColumnNumber.getText().toString().trim();

                if (TextUtils.isEmpty(r) || TextUtils.isEmpty(c)) {
                    Toast.makeText(MainActivity.this, R.string.insert_numb, Toast.LENGTH_SHORT).show();
                    return;
                }

                final int rows = Integer.parseInt(r);
                final int columns = Integer.parseInt(c);

                if (rows == 0 || columns == 0) {
                    Toast.makeText(MainActivity.this, R.string.columns_size, Toast.LENGTH_SHORT).show();
                    return;
                }

                appExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        List<ColorModel> colorModelList = mDb.dataDao().getColorModelList();
                        if (colorModelList != null) {
                            final List<String> colors = new ArrayList<>();

                            for (ColorModel colorModel : colorModelList) {
                                colors.add(colorModel.getHex());
                            }
                            Log.d(TAG, "onClick: color list size" + String.valueOf(colors.size()));

                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mRectangleContainer.startNew(colors, rows, columns);
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void saveSettings() {
        if (mRectangleContainer != null) {
            final SettingsModel settingsModel = mRectangleContainer.getSettings();

            // we dont want to save empty model
            if (settingsModel.getRows() != 0) {
                appExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.dataDao().insertSettings(settingsModel);
                    }
                });
            }
        }
    }
}
