package com.nanjing.bus.shuttle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nanjing.bus.shuttle.db.SharedTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fylder on 2017/3/28.
 */

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.setting_delay_text)
    TextView delayText;
    @BindView(R.id.setting_delay_edit)
    EditText delayEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        int t = SharedTools.getDelay();
        String str = "设置" + t + "s刷新一次";
        delayText.setText(str);
    }

    @OnClick(R.id.setting_save)
    void save() {
        String s = delayEdit.getText().toString().trim();
        int t;
        try {
            t = Integer.valueOf(s);
        } catch (Exception e) {
            t = 1000;
        }
        SharedTools.setDelay(t);
        String str = "设置" + t + "s刷新一次";
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        delayText.setText(str);
    }
}
