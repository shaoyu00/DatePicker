package com.datedialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weigan.loopview.LoopView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.start_time_year)
    TextView startTimeYear;
    @BindView(R.id.start_time_month)
    TextView startTimeMonth;
    @BindView(R.id.start_time_day)
    TextView startTimeDay;
    @BindView(R.id.start_time)
    LinearLayout startTime;
    @BindView(R.id.end_time_year)
    TextView endTimeYear;
    @BindView(R.id.end_time_month)
    TextView endTimeMonth;
    @BindView(R.id.end_time_day)
    TextView endTimeDay;
    @BindView(R.id.end_time)
    LinearLayout endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//绑定ButterKnife
        initData();
    }

    private void initData() {
        //----------初始化日期显示值 --当天年月日
        Calendar calendar = Calendar.getInstance();
        startTimeYear.setText(calendar.get(Calendar.YEAR) + "");
        startTimeMonth.setText((calendar.get(Calendar.MONTH) + 1) + "");
        startTimeDay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
        endTimeYear.setText(calendar.get(Calendar.YEAR) + "");
        endTimeMonth.setText((calendar.get(Calendar.MONTH) + 1) + "");
        endTimeDay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
    }


    @OnClick({R.id.start_time, R.id.end_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_time:
                int id = 1;
                String before_start_year = startTimeYear.getText().toString().trim();
                String before_start_month = startTimeMonth.getText().toString().trim();
                String before_start_day = startTimeDay.getText().toString().trim();
                showTimeDialog(id, before_start_year, before_start_month, before_start_day);
                break;
            case R.id.end_time:
                int idd = 2;
                String before_end_year = endTimeYear.getText().toString().trim();
                String before_end_month = endTimeMonth.getText().toString().trim();
                String before_end_day = endTimeDay.getText().toString().trim();
                showTimeDialog(idd, before_end_year, before_end_month, before_end_day);
                break;
        }
    }

    private void showTimeDialog(final int id, final String before_year, final String before_month, final String before_day) {
        final DateDialog confirmDialog = new DateDialog(MainActivity.this);
        confirmDialog.show();
        confirmDialog.setClicklistener(new DateDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(LoopView loopView_year, LoopView loopView_month, LoopView loopView_day) {
                switch (id) {
                    case 1:
                        startTimeYear.setText(loopView_year.getCurrentIndex() + 1900 + "");
                        startTimeMonth.setText(loopView_month.getCurrentIndex() + 1 + "");
                        startTimeDay.setText(loopView_day.getCurrentIndex() + 1 + "");
                        confirmDialog.dismiss();
                        break;
                    case 2:
                        endTimeYear.setText(loopView_year.getCurrentIndex() + 1900 + "");
                        endTimeMonth.setText(loopView_month.getCurrentIndex() + 1 + "");
                        endTimeDay.setText(loopView_day.getCurrentIndex() + 1 + "");
                        confirmDialog.dismiss();
                        break;
                }

            }

            @Override
            public void doCancel(LoopView loopView_year, LoopView loopView_month, LoopView loopView_day) {
                switch (id) {
                    case 1:
                        startTimeYear.setText(before_year);
                        startTimeMonth.setText(before_month);
                        startTimeDay.setText(before_day);
                        confirmDialog.dismiss();
                        break;
                    case 2:
                        endTimeYear.setText(before_year);
                        endTimeMonth.setText(before_month);
                        endTimeDay.setText(before_day);
                        confirmDialog.dismiss();
                        break;
                }

            }
        });
    }
}
