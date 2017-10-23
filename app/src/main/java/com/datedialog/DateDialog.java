package com.datedialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Tic--------------Dialog
 */
public class DateDialog extends Dialog {

    private Context context;
    private ClickListenerInterface clickListenerInterface;
    //private ItemSelectedListenerInterface itemSelectedListenerInterface;

    LoopView loopView_year;
    LoopView loopView_month;
    LoopView loopView_day;
    //年月日选择器打开是显示的索引位置
    int yearIndex = 0;
    int monthIndex=0;
    int dayIndex=0;
    //----------初始化日期显示值 --当天年月日
    int currentYear;
    int currentMonth;
    int currentDay;
    List<String> list_year;
    List<String> list_month;
    List<String> list_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    @Override
    public void show() {
        super.show();
        // 设置dialog显示动画
        getWindow().setWindowAnimations(R.style.PopupAnimation);
        // 设置显示位置为底部
        getWindow().setGravity(Gravity.BOTTOM);
    }

    public DateDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.picker_date_lay, null);
        setContentView(view);
        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        loopView_year = (LoopView) view.findViewById(R.id.loopView_year);
        loopView_month = (LoopView) view.findViewById(R.id.loopView_month);
        loopView_day = (LoopView) view.findViewById(R.id.loopView_day);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_true);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancle);
        tvConfirm.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());

        //----------初始化日期显示值 --当天年月日
        Calendar calendar = Calendar.getInstance();
        currentYear=calendar.get(Calendar.YEAR);
        currentMonth=calendar.get(Calendar.MONTH)+1;
        currentDay=calendar.get(Calendar.DAY_OF_MONTH);

        //-----------年 月  日  数据
        list_year=new ArrayList<>();
        for (int i = 1900; i <= currentYear; i++) {
            list_year.add(i+"年");
        }
         list_month=new ArrayList<>();
        for (int i = 1; i <=12; i++) {
            list_month.add(i+"月");
        }
        list_day=new ArrayList<>();
        for (int i = 1; i <= getLastDay(currentYear,currentMonth); i++) {
            list_day.add(i+"日");
        }
        for (int i=0;i<list_year.size();i++){
            String year=list_year.get(i);
            if ((currentYear+"").equals(year.substring(0,year.indexOf("年")))){
                yearIndex=i;
            }
        }
        for (int i=0;i<list_month.size();i++){
            String month=list_month.get(i);
            if ((currentMonth+"").equals(month.substring(0,month.indexOf("月")))){
                monthIndex=i;
            }
        }
        for (int i=0;i<list_day.size();i++){
            String day=list_day.get(i);
            if ((currentDay+"").equals(day.substring(0,day.indexOf("日")))){
                dayIndex=i;
            }
        }
        //设置是否循环播放
        //loopView_year.setNotLoop();
        //loopView_month.setNotLoop();
        //loopView_day.setNotLoop();
        //设置原始数据
        loopView_year.setItems(list_year);
        loopView_month.setItems(list_month);
        loopView_day.setItems(list_day);
        //设置初始位置
        loopView_year.setInitPosition(yearIndex);
        loopView_month.setInitPosition(monthIndex);
        loopView_day.setInitPosition(dayIndex);
        //----------------初始化 当前年 月 日位置索引
        loopView_year.setCurrentIndex(yearIndex);
        loopView_month.setCurrentIndex(monthIndex);
        loopView_day.setCurrentIndex(dayIndex);
        //设置字体大小
        loopView_year.setTextSize(18);
        loopView_month.setTextSize(18);
        loopView_day.setTextSize(18);

        loopView_year.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                // 记录当前选择的年
                loopView_year.setCurrentIndex(index);
                int selectedYear=loopView_year.getCurrentIndex()+1900;
                int selectedMonth=loopView_month.getCurrentIndex()+1;
                if (selectedMonth==2){ //----------当滚动时间时  当选中的月份为2月  需要重新计算天数
                    List<String> newlist_day=new ArrayList<>();
                    for (int i = 1; i <= getLastDay(selectedYear,selectedMonth); i++) {
                        newlist_day.add(i+"日");
                    }
                    loopView_day.setItems(newlist_day);
                }
            }
        });
        loopView_month.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                loopView_month.setCurrentIndex(index);
                int selectedYear=loopView_year.getCurrentIndex()+1900;
                int selectedMonth=loopView_month.getCurrentIndex()+1;
                List<String> newlist_day=new ArrayList<>();
                for (int i = 1; i <= getLastDay(selectedYear,selectedMonth); i++) {
                    newlist_day.add(i+"日");
                }
                loopView_day.setItems(newlist_day);
            }
        });
        loopView_day.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                loopView_day.setCurrentIndex(index);
            }
        });

    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }
 /*   public void setItemSelectedListener(ItemSelectedListenerInterface itemSelectedListenerInterface) {//-----------滚动时 在页面刷新日期
        this.itemSelectedListenerInterface = itemSelectedListenerInterface;
    }*/
    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_true:
                    clickListenerInterface.doConfirm(loopView_year,loopView_month,loopView_day);
                    break;
                case R.id.tv_cancle:
                    clickListenerInterface.doCancel(loopView_year,loopView_month,loopView_day);
                    break;
            }
        }
    }
   /* private class itemSelectedListener implements OnItemSelectedListener {
        @Override
        public void onItemSelected(int index) {

        }
    }*/
    public interface ClickListenerInterface {
        void doConfirm(LoopView loopView_year, LoopView loopView_month, LoopView loopView_day);
        void doCancel(LoopView loopView_year, LoopView loopView_month, LoopView loopView_day);
    }
  /*  public interface ItemSelectedListenerInterface {
        void select();
    }
*/
    /**
     * 判断是否闰年
     *
     * @param year
     * @return
     */
    private static boolean isLeapYear(int year) {
        return (year % 100 == 0 && year % 400 == 0) || (year % 100 != 0 && year % 4 == 0);
    }

    /**
     * 获取特定年月对应的天数
     *
     * @param year
     * @param month
     * @return
     */
    private static int getLastDay(int year, int month) {
        if (month == 2) {
            // 2月闰年的话返回29，防止28
            return isLeapYear(year) ? 29 : 28;
        }
        // 一三五七八十腊，三十一天永不差
        return month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12 ? 31 : 30;
    }
}
