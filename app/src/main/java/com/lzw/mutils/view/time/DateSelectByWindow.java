package com.lzw.mutils.view.time;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lzw.mutils.R;
import com.lzw.mutils.tool.ScreenUtils;
import com.lzw.mutils.view.time.wheel.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author: lzw
 * Date: 2019/7/18
 * Description: This is DateSelectByWindow
 */
public class DateSelectByWindow extends PopupWindow {


    public DateSelectByWindow(Context context) {
        super(context);
    }

    public static final class Builder implements View.OnClickListener {

        private Context mContext;

        /**
         * 数据生产者
         */
        private final ArrayList<String> SECONDS_LIST = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"));

        private final ArrayList<String> HOURS_LIST = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"));

        private final ArrayList<String> MONTH_LIST = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));

        private List<String> YEAR_LIST = new ArrayList<>(Arrays.asList(
                "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999",
                "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
                "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
                "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029",
                "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039",
                "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049",
                "2050", "2051", "2052", "2053", "2054", "2055", "2056", "2057", "2058", "2059",
                "2060", "2061", "2062", "2063", "2064", "2065", "2066", "2067", "2068", "2069",
                "2070", "2071", "2072", "2073", "2074", "2075", "2076", "2077", "2078", "2079",
                "2080", "2081", "2082", "2083", "2084", "2085", "2086", "2087", "2088", "2089",
                "2090", "2091", "2092", "2093", "2094", "2095", "2096", "2097", "2098", "2099",
                "2100"));

        /**
         * 年View
         */
        private WheelView wv_year;

        /**
         * 月View
         */
        private WheelView wv_month;

        /**
         * 日View
         */
        private WheelView wv_day;

        /**
         * 小时View
         */
        private WheelView wv_hour;


        /**
         * 分View
         */
        private WheelView wv_minute;


        /**
         * 年数据的List
         */
        private ArrayList<String> mYearList = new ArrayList<>();

        /**
         * 月数据的List
         */
        private ArrayList<String> mMonthList = new ArrayList<>();

        /**
         * 日数据的List
         */
        private ArrayList<String> mDayList = new ArrayList<>();

        /**
         * 小时数据的List
         */
        private ArrayList<String> mHourList = new ArrayList<>();

        /**
         * 分钟数据的List
         */
        private ArrayList<String> mMinuteList = new ArrayList<>();

        private TextView tvYear;

        private TextView tvMonth;

        private TextView tvDay;

        private TextView tvHour;

        private TextView tvMinute;

        private TextView tvCancel;

        private TextView tvConfirm;

        private TextView tvTitle;

        private String confirmStr;

        private String startTitle = "开始时间", endTitle = "结束时间";

        /**
         * 当前年份
         */
        private String currentYear;

        /**
         * 选择的年份
         */
        private String selectYear;

        /**
         * 选择的月
         */
        private String selectMonth;

        /**
         * 选择的日
         */
        private String selectDay;

        /**
         * 选择的小时
         */
        private String selectHour;

        /**
         * 选择的分钟
         */
        private String selectMinute;

        /**
         * 系统时间
         */
        private long mStartTime;

        /**
         * 通过毫秒获取标准年、月、日、分
         */
        private String yearByMillis, monthByMillis, dayByMillis, minuteByMillis;

        /**
         * 通过毫秒获取标准小时
         */
        private int hourByMillis;

        /**
         * 是否滚动到当前时间
         */
        private boolean scrollToCurrentDate;

        /**
         * 是否显示可以选择下一个时间
         */
        private boolean nextSelectShow;

        /**
         * 显示选择下一个时间的按钮内容
         */
        private String nextSelectText;


        /**
         * 开始选择的时间
         */
        private String startTime;

        /**
         * 下一步选择的时间
         */
        private String endTime;


        /**
         * 开始时间选择的 年、月、日、小时、分
         */
        private String startYear, startMonth, startDay, startHour, startMinute;

        /**
         * 是否显示年，默认为true
         */
        private boolean yearShow = true;


        /**
         * 是否显示月，默认为true
         */
        private boolean monthShow = true;

        /**
         * 是否显示日，默认为true
         */
        private boolean dayShow = true;


        /**
         * 是否显示小时，默认为true
         */
        private boolean hourShow = true;

        /**
         * 是否显示分，默认为true
         */
        private boolean minuteShow = true;


        private DateSelectByWindow dateSelectByWindow;

        private OnSelectTimeListener onSelectTimeListener;

        private OnSelectNextTimeListener onSelectNextTimeListener;

        private String minTime;//最小的时间

        private boolean minTimeShow = false;//是否设置了最小时间

        private boolean maxTimeShow = false;//是否设置了最大时间

        private String maxTime;//最大的时间

        private String canSelectYear;

        public Builder(Context context) {
            this.mContext = context;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dateSelectByWindow = new DateSelectByWindow(mContext);
            if (null != inflater) {
                View dateSelectView = inflater.inflate(R.layout.layout_date_select_pop, null);

                tvTitle = dateSelectView.findViewById(R.id.title);
                tvConfirm = dateSelectView.findViewById(R.id.confirm);
                tvCancel = dateSelectView.findViewById(R.id.cancel);
                tvYear = dateSelectView.findViewById(R.id.tv_year);
                tvMonth = dateSelectView.findViewById(R.id.tv_month);
                tvDay = dateSelectView.findViewById(R.id.tv_day);
                tvHour = dateSelectView.findViewById(R.id.tv_hour);
                tvMinute = dateSelectView.findViewById(R.id.tv_minute);

                wv_year = dateSelectView.findViewById(R.id.wv_year);
                wv_month = dateSelectView.findViewById(R.id.wv_month);
                wv_day = dateSelectView.findViewById(R.id.wv_day);
                wv_hour = dateSelectView.findViewById(R.id.wv_hour);
                wv_minute = dateSelectView.findViewById(R.id.wv_minute);

                setTimeData();

                addWheelViewListener();

                tvCancel.setOnClickListener(this);
                tvConfirm.setOnClickListener(this);
                dateSelectByWindow.setContentView(dateSelectView);
                dateSelectByWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                dateSelectByWindow.setHeight(ScreenUtils.dp2px(250));
                dateSelectByWindow.setFocusable(true);
                dateSelectByWindow.setAnimationStyle(R.style.PopUpWindowAnim);
                ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
                dateSelectByWindow.setBackgroundDrawable(colorDrawable);
            }
        }


        /**
         * 设置时间数据
         */
        private void setTimeData() {
            mStartTime = System.currentTimeMillis();
            /**
             * 获取当前年份
             */
            currentYear = getYearByMillis(mStartTime);
            selectYear = currentYear;

            yearByMillis = getYearByMillis(mStartTime);
            monthByMillis = getMonthByMillis(mStartTime);
            dayByMillis = getDayByMillis(mStartTime);
            hourByMillis = getHourByMillis(mStartTime);
            minuteByMillis = getMinuteByMillis(mStartTime);

            /**
             * 设置当前年份
             */
            setYear(yearByMillis);

            /**
             * 设置当前月份
             */
            setMonth(monthByMillis);

            /**
             * 设置当前日
             */
            setDay(monthByMillis, dayByMillis);

            /**
             * 设置当前小时
             */
            setHour(monthByMillis, dayByMillis, hourByMillis);

            /**
             * 设置当前分钟
             */
            setMinute(monthByMillis, dayByMillis, hourByMillis, minuteByMillis);

            wv_year.setAdapter(new DateSelectAdapter(mYearList));

            if (null == confirmStr || confirmStr.length() <= 0) {
                confirmStr = "确定";
            }
            tvConfirm.setText(confirmStr);
        }

        /**
         * WheelView添加选择监听
         */
        private void addWheelViewListener() {
            wv_year.setOnItemSelectedListener(index -> {
                if (Integer.parseInt(selectYear) > Integer.parseInt(canSelectYear)) {
                    wv_year.setEnabled(true);
                    selectYear = mYearList.get(index);
                    setMonth(monthByMillis);
                    setDay(monthByMillis, dayByMillis);
                    setHour(monthByMillis, dayByMillis, hourByMillis);
                    setMinute(monthByMillis, dayByMillis, hourByMillis, minuteByMillis);
                } else {
                    wv_year.setEnabled(false);
                    Toast.makeText(mContext, "不能选择", Toast.LENGTH_SHORT).show();
                }
            });


            wv_month.setOnItemSelectedListener(index -> {
                selectMonth = mMonthList.get(index);
                setDay(monthByMillis, dayByMillis);
                setHour(monthByMillis, dayByMillis, hourByMillis);
                setMinute(monthByMillis, dayByMillis, hourByMillis, minuteByMillis);
            });


            wv_day.setOnItemSelectedListener(index -> {
                selectDay = mDayList.get(index);
                setHour(monthByMillis, dayByMillis, hourByMillis);
                setMinute(monthByMillis, dayByMillis, hourByMillis, minuteByMillis);
            });


            wv_hour.setOnItemSelectedListener(index -> {
                selectHour = mHourList.get(index);
                setMinute(monthByMillis, dayByMillis, hourByMillis, minuteByMillis);
            });


            wv_minute.setOnItemSelectedListener(index -> {
                selectMinute = mMinuteList.get(index);
            });

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel:
                    dateSelectByWindow.dismiss();
                    break;
                case R.id.confirm:
                    if (nextSelectShow) {
                        tvTitle.setText(endTitle);
                        if (tvConfirm.getText().toString().equals("确定")) {
                            if (Integer.parseInt(startYear) > Integer.parseInt(selectYear)) {
                                Toast.makeText(mContext, "年份不能小于开始选择的年份", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (dayShow) {
                                if (Integer.parseInt(startYear) >= Integer.parseInt(selectYear)) {
                                    if (Integer.parseInt(startMonth) > Integer.parseInt(selectMonth)) {
                                        Toast.makeText(mContext, "月份不能小于开始选择的月份", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            } else {
                                if (Integer.parseInt(startYear) >= Integer.parseInt(selectYear)) {
                                    if (Integer.parseInt(startMonth) >= Integer.parseInt(selectMonth)) {
                                        Toast.makeText(mContext, "月份不能小于开始选择的月份", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            if (hourShow) {
                                if (Integer.parseInt(startYear) >= Integer.parseInt(selectYear)) {
                                    if (Integer.parseInt(startMonth) >= Integer.parseInt(selectMonth)) {
                                        if (Integer.parseInt(startDay) > Integer.parseInt(selectDay)) {
                                            Toast.makeText(mContext, "日不能小于开始选择的日", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }
                            } else {
                                if (Integer.parseInt(startYear) >= Integer.parseInt(selectYear)) {
                                    if (Integer.parseInt(startMonth) >= Integer.parseInt(selectMonth)) {
                                        if (Integer.parseInt(startDay) >= Integer.parseInt(selectDay)) {
                                            Toast.makeText(mContext, "日不能小于开始选择的日", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }
                            }
                            if (hourShow) {//这边只有当小时显示的时候才需要去判断小时
                                if (Integer.parseInt(startYear) >= Integer.parseInt(selectYear)) {
                                    if (Integer.parseInt(startMonth) >= Integer.parseInt(selectMonth)) {
                                        if (Integer.parseInt(startDay) >= Integer.parseInt(selectDay)) {
                                            if (minuteShow) {
                                                if (Integer.parseInt(startHour) > Integer.parseInt(selectHour)) {
                                                    Toast.makeText(mContext, "小时不能小于开始选择的小时", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            } else {
                                                if (Integer.parseInt(startHour) >= Integer.parseInt(selectHour)) {
                                                    Toast.makeText(mContext, "小时不能小于开始选择的小时", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (minuteShow) {//这边只有当分钟显示的时候才需要去判断分钟
                                if (Integer.parseInt(startYear) >= Integer.parseInt(selectYear)) {
                                    if (Integer.parseInt(startMonth) >= Integer.parseInt(selectMonth)) {
                                        if (Integer.parseInt(startDay) >= Integer.parseInt(selectDay)) {
                                            if (Integer.parseInt(startHour) >= Integer.parseInt(selectHour)) {
                                                if (Integer.parseInt(startMinute) >= Integer.parseInt(selectMinute)) {
                                                    Toast.makeText(mContext, "分钟不能小于开始选择的分钟", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                            endTime = selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute;
                            long startMillisecondValue = getMillisecondValue(startTime + ":00");
                            long endMillisecondValue = getMillisecondValue(endTime + ":00");
                            String startCurrentTime = getCurrentTime(startMillisecondValue);
                            String endCurrentTime = getCurrentTime(endMillisecondValue);
                            onSelectNextTimeListener.onNextTimeSelect(startCurrentTime, endCurrentTime);
                            dateSelectByWindow.dismiss();
                        } else {
                            startYear = selectYear;
                            startMonth = selectMonth;
                            startDay = selectDay;
                            startHour = selectHour;
                            startMinute = selectMinute;
                            tvConfirm.setText(nextSelectText);
                            startTime = selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute;
                            setTimeData();
                        }
                    } else {
                        long millisecondValue = getMillisecondValue(selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute + ":00");
                        String currentTime = getCurrentTime(millisecondValue);
                        if (minTimeShow) {//是否设置了最小的时间
                            long minTimeValue = getMillisecondValue(minTime);
                            if (millisecondValue < minTimeValue) {
                                Toast.makeText(mContext, "选择的时间不能小于最小时间", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (maxTimeShow) {//是否设置了最大的时间
                            long maxTimeValue = getMillisecondValue(maxTime);
                            if (millisecondValue > maxTimeValue) {
                                Toast.makeText(mContext, "选择的时间不能大于最大时间", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        onSelectTimeListener.onTimeSelect(currentTime);
                        dateSelectByWindow.dismiss();
                    }
                    break;
            }
        }


        /**
         * 设置具体的年份
         */
        private void setYear(String yearByMillis) {
            mYearList.clear();
            mYearList.addAll(YEAR_LIST);
            if (scrollToCurrentDate) {//是否滚动到当前时间
                selectYear = currentYear;
                int index = mYearList.indexOf(yearByMillis);
                if (index != -1) {
                    wv_year.setCurrentItem(index);
                } else {
                    wv_year.setCurrentItem(0);
                }
            } else {
                selectYear = mYearList.get(0);
                wv_year.setCurrentItem(0);
            }
        }


        /**
         * 设置具体的每个月
         *
         * @param monthByMillis
         */
        private void setMonth(String monthByMillis) {
            mMonthList.clear();
            mMonthList.addAll(MONTH_LIST);
            if (currentYear.equals(selectYear)) {
                if (monthShow) {
                    if (scrollToCurrentDate) {//是否滚动到当前时间
                        int index = MONTH_LIST.indexOf(monthByMillis);
                        if (index != -1) {
                            selectMonth = monthByMillis;
                            wv_month.setCurrentItem(mMonthList.indexOf(monthByMillis));
                            selectMonth = mMonthList.get(index);
                        } else {
                            wv_month.setCurrentItem(0);
                            selectMonth = mMonthList.get(0);
                        }
                    } else {
                        wv_month.setCurrentItem(0);
                        selectMonth = mMonthList.get(0);
                    }
                } else {
                    wv_month.setCurrentItem(0);
                    selectMonth = mMonthList.get(0);
                }
            } else {
                wv_month.setCurrentItem(0);
                selectMonth = mMonthList.get(0);
            }
            wv_month.setAdapter(new DateSelectAdapter(mMonthList));
        }

        /**
         * 设置具体的每一天
         *
         * @param monthByMillis
         * @param dayByMillis
         */
        private void setDay(String monthByMillis, String dayByMillis) {
            mDayList.clear();
            int days = getDayByMonth(selectYear, selectMonth);
            for (int i = 1; i <= days; i++) {
                mDayList.add(String.valueOf(i));
            }
            if (currentYear.equals(selectYear) && monthByMillis.equals(selectMonth)) {
                if (dayShow) {
                    if (scrollToCurrentDate) {//是否滚动到当前时间
                        Integer day = Integer.valueOf(dayByMillis);
                        selectDay = mDayList.get(day - 1);
                        wv_day.setCurrentItem(day - 1);
                    } else {
                        selectDay = mDayList.get(0);
                        wv_day.setCurrentItem(0);
                    }
                } else {
                    selectDay = mDayList.get(0);
                    wv_day.setCurrentItem(0);
                }
            } else {
                selectDay = mDayList.get(0);
                wv_day.setCurrentItem(0);
            }
            wv_day.setAdapter(new DateSelectAdapter(mDayList));
        }

        /**
         * 设置具体的每一秒
         *
         * @param monthByMillis
         * @param dayByMillis
         * @param hourByMillis
         */
        private void setHour(String monthByMillis, String dayByMillis, int hourByMillis) {
            mHourList.clear();
            mHourList.addAll(HOURS_LIST);
            if (currentYear.equals(selectYear) && monthByMillis.equals(selectMonth) && dayByMillis.equals(selectDay)) {
                if (hourShow) {
                    if (scrollToCurrentDate) {//是否滚动到当前时间
                        int index = mHourList.indexOf(hourByMillis + "");
                        if (index != -1) {
                            selectHour = mHourList.get(index);
                            wv_hour.setCurrentItem(index);
                        } else {
                            selectHour = mHourList.get(0);
                            wv_hour.setCurrentItem(0);
                        }
                    } else {
                        selectHour = mHourList.get(0);
                        wv_hour.setCurrentItem(0);
                    }
                } else {
                    selectHour = mHourList.get(0);
                    wv_hour.setCurrentItem(0);
                }
            } else {
                selectHour = mHourList.get(0);
                wv_hour.setCurrentItem(0);
            }
            wv_hour.setAdapter(new DateSelectAdapter(mHourList));
        }


        /**
         * 设置具体的每一分钟
         *
         * @param monthByMillis
         * @param dayByMillis
         * @param hourByMillis
         * @param minuteByMillis
         */
        private void setMinute(String monthByMillis, String dayByMillis, int hourByMillis, String minuteByMillis) {
            mMinuteList.clear();
            mMinuteList.addAll(SECONDS_LIST);
            if (currentYear.equals(selectYear) && monthByMillis.equals(selectMonth) && dayByMillis.equals(selectDay) && String.valueOf(hourByMillis).equals(selectHour)) {
                if (minuteShow) {
                    if (scrollToCurrentDate) {//是否滚动到当前时间
                        int index = SECONDS_LIST.indexOf(minuteByMillis);
                        if (index != -1) {
                            selectMinute = mMinuteList.get(index);
                            wv_minute.setCurrentItem(index);
                        } else {
                            selectMinute = mMinuteList.get(0);
                            wv_minute.setCurrentItem(0);
                        }
                    } else {
                        selectMinute = mMinuteList.get(0);
                        wv_minute.setCurrentItem(0);
                    }
                } else {
                    selectMinute = mMinuteList.get(0);
                    wv_minute.setCurrentItem(0);
                }
            } else {
                selectMinute = mMinuteList.get(0);
                wv_minute.setCurrentItem(0);
            }
            wv_minute.setAdapter(new DateSelectAdapter(mMinuteList));
        }


        /**
         * 获取年
         *
         * @param millis
         * @return
         */
        private String getYearByMillis(long millis) {
            Date date = new Date(millis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.YEAR));
        }


        /**
         * 获取月
         *
         * @param millis
         * @return
         */
        private String getMonthByMillis(long millis) {
            Date date = new Date(millis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.MONTH) + 1);
        }


        /**
         * 获取日
         *
         * @param millis
         * @return
         */
        private String getDayByMillis(long millis) {
            Date date = new Date(millis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }


        /**
         * 获取小时
         *
         * @param millis
         * @return
         */
        private int getHourByMillis(long millis) {
            Date date = new Date(millis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.HOUR_OF_DAY);
        }


        /**
         * 获取分钟
         *
         * @param millis
         * @return
         */
        private String getMinuteByMillis(long millis) {
            Date date = new Date(millis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.MINUTE));
        }


        /**
         * 获取每个月的天数
         *
         * @param year
         * @param month
         * @return
         */
        private int getDayByMonth(String year, String month) {
            int days = 0;
            if (!"2".equals(month)) {
                switch (month) {
                    case "1":
                    case "3":
                    case "5":
                    case "7":
                    case "8":
                    case "10":
                    case "12":
                        days = 31;
                        break;
                    case "4":
                    case "6":
                    case "9":
                    case "11":
                        days = 30;
                }
            } else {// 闰年
                int yearInt = Integer.valueOf(year);
                if (yearInt % 4 == 0 && yearInt % 100 != 0 || yearInt % 400 == 0)
                    days = 29;
                else
                    days = 28;
            }
            return days;
        }


        /**
         * 时间格式转时间戳--秒级
         *
         * @param date
         * @return
         */
        private long getMillisecondValue(String date) {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                long time = dateformat.parse(date).getTime();
                return time;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }


        /**
         * 时间格式化输入完整的时间格式,精确到分
         *
         * @param value
         * @return
         */
        private String getCurrentTime(long value) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String time = format.format(new Date(value));
            //LogUtils.d("解析到的时间为："+time);
            return time;
        }

        public interface OnSelectTimeListener {
            void onTimeSelect(String time);
        }


        public interface OnSelectNextTimeListener {
            void onNextTimeSelect(String startTime, String endTime);
        }

        /**
         * ====================================对外的方法=====================================
         */


        /**
         * 设置最大的年份
         *
         * @param year
         * @return
         */
        public Builder setCanSelectYear(String year) {
            this.canSelectYear = year;
            return this;
        }


        /**
         * 设置最大的年份
         *
         * @param year
         * @return
         */
        public Builder setMaxYear(String year) {
            ArrayList<List<String>> list = new ArrayList<>();
            for (int i = 0; i < YEAR_LIST.size(); i++) {
                if (YEAR_LIST.get(i).equals(year)) {
                    list.add(YEAR_LIST.subList(0, i + 1));
                }
            }
            YEAR_LIST = list.get(0);
            return this;
        }


        /**
         * 设置最大的时间
         *
         * @param time 时间格式 2019-10-10
         * @return
         */
        public Builder setMaxTime(String time) {
            this.maxTime = time;
            maxTimeShow = true;
            return this;
        }


        /**
         * 设置最小的时间
         *
         * @param time 时间格式 2019-10-10
         * @return
         */
        public Builder setMinTime(String time) {
            this.minTime = time;
            minTimeShow = true;
            return this;
        }


        /**
         * 设置最小的年份
         *
         * @param year
         * @return
         */
        public Builder setMinYear(String year) {
            ArrayList<List<String>> list = new ArrayList<>();
            for (int i = 0; i < YEAR_LIST.size(); i++) {
                if (YEAR_LIST.get(i).equals(year)) {
                    list.add(YEAR_LIST.subList(i, YEAR_LIST.size()));
                }
            }
            YEAR_LIST = list.get(0);
            return this;
        }

        /**
         * 设置年份的范围
         *
         * @param minYear
         * @return
         */
        public Builder setYearRange(String minYear, String maxYear) {
            ArrayList<List<String>> minList = new ArrayList<>();
            for (int i = 0; i < YEAR_LIST.size(); i++) {
                if (YEAR_LIST.get(i).equals(minYear)) {
                    minList.add(YEAR_LIST.subList(i, YEAR_LIST.size()));
                }
            }
            YEAR_LIST = minList.get(0);
            ArrayList<List<String>> maxList = new ArrayList<>();
            for (int i = 0; i < YEAR_LIST.size(); i++) {
                if (YEAR_LIST.get(i).equals(maxYear)) {
                    maxList.add(YEAR_LIST.subList(0, i + 1));
                }
            }
            YEAR_LIST = maxList.get(0);
            return this;
        }

        /**
         * 创建PopUpWindow
         *
         * @return
         */
        public DateSelectByWindow create() {
            setYear(yearByMillis);
            setMonth(monthByMillis);
            setDay(monthByMillis, dayByMillis);
            setHour(monthByMillis, dayByMillis, hourByMillis);
            setMinute(monthByMillis, dayByMillis, hourByMillis, minuteByMillis);
            return dateSelectByWindow;
        }

        /**
         * 设置选择下一步的监听
         *
         * @param onSelectNextTimeListener
         * @return
         */
        public Builder setOnSelectNextTimeListener(OnSelectNextTimeListener onSelectNextTimeListener) {
            this.onSelectNextTimeListener = onSelectNextTimeListener;
            return this;
        }

        /**
         * 设置时间选择的监听
         *
         * @param onSelectTimeListener
         * @return
         */
        public Builder setOnSelectTimeListener(OnSelectTimeListener onSelectTimeListener) {
            this.onSelectTimeListener = onSelectTimeListener;
            return this;
        }


        /**
         * 设置是否滚动到当前时间
         *
         * @param scrollToCurrentDate
         */
        public Builder setScrollToCurrentDate(boolean scrollToCurrentDate) {
            this.scrollToCurrentDate = scrollToCurrentDate;
            return this;
        }


        /**
         * 设置年份是否显示
         *
         * @param yearShow
         */
        public Builder setYearShow(boolean yearShow) {
            this.yearShow = yearShow;
            if (yearShow) {
                wv_year.setVisibility(View.VISIBLE);
                tvYear.setVisibility(View.VISIBLE);
            } else {
                selectYear = currentYear;
                wv_year.setVisibility(View.GONE);
                tvYear.setVisibility(View.GONE);
            }
            return this;
        }

        /**
         * 设置月份是否显示
         *
         * @param monthShow
         */
        public Builder setMonthShow(boolean monthShow) {
            this.monthShow = monthShow;
            if (monthShow) {
                wv_month.setVisibility(View.VISIBLE);
                tvMonth.setVisibility(View.VISIBLE);
            } else {
                selectMonth = mMonthList.get(0);
                wv_month.setVisibility(View.GONE);
                tvMonth.setVisibility(View.GONE);
            }
            return this;
        }


        /**
         * 设置天是否显示
         *
         * @param dayShow
         */
        public Builder setDayShow(boolean dayShow) {
            this.dayShow = dayShow;
            if (dayShow) {
                wv_day.setVisibility(View.VISIBLE);
                tvDay.setVisibility(View.VISIBLE);
            } else {
                selectDay = mDayList.get(0);
                wv_day.setVisibility(View.GONE);
                tvDay.setVisibility(View.GONE);
            }
            return this;
        }


        /**
         * 设置小时是否显示
         *
         * @param hourShow
         */
        public Builder setHourShow(boolean hourShow) {
            this.hourShow = hourShow;
            if (hourShow) {
                wv_hour.setVisibility(View.VISIBLE);
                tvHour.setVisibility(View.VISIBLE);
            } else {
                selectHour = mHourList.get(0);
                wv_hour.setVisibility(View.GONE);
                tvHour.setVisibility(View.GONE);
            }
            return this;
        }


        /**
         * 设置分钟是否显示
         *
         * @param minuteShow
         */
        public Builder setMinuteShow(boolean minuteShow) {
            this.minuteShow = minuteShow;
            if (minuteShow) {
                wv_minute.setVisibility(View.VISIBLE);
                tvMinute.setVisibility(View.VISIBLE);
            } else {
                selectMinute = mMinuteList.get(0);
                wv_minute.setVisibility(View.GONE);
                tvMinute.setVisibility(View.GONE);
            }
            return this;
        }


        /**
         * 设置取消按钮的字体大小
         *
         * @param dp
         */
        public Builder setCancelTextSize(int dp) {
            tvCancel.setTextSize(ScreenUtils.dp2px(dp));
            return this;
        }

        /**
         * 设置取消按钮的字体内容
         *
         * @param text
         */
        public Builder setCancelText(String text) {
            tvCancel.setText(text);
            return this;
        }

        /**
         * 设置取消按钮的字体颜色
         *
         * @param color
         */
        public Builder setCancelTextColor(String color) {
            tvCancel.setTextColor(Color.parseColor(color));
            return this;
        }

        /**
         * 设置取消按钮的字体颜色
         *
         * @param color
         */
        public Builder setCancelTextColor(int color) {
            tvCancel.setTextColor(color);
            return this;
        }


        /**
         * 设置确定按钮的字体大小
         *
         * @param dp
         */
        public Builder setConfirmTextSize(int dp) {
            tvConfirm.setTextSize(ScreenUtils.dp2px(dp));
            return this;
        }

        /**
         * 设置确定按钮的字体内容
         *
         * @param text
         */
        public Builder setConfirmText(String text) {
            confirmStr = text;
            tvConfirm.setText(confirmStr);
            return this;
        }

        /**
         * 设置确定按钮的字体颜色
         *
         * @param color
         */
        public Builder setConfirmTextColor(String color) {
            tvConfirm.setTextColor(Color.parseColor(color));
            return this;
        }


        /**
         * 设置确定按钮的字体颜色
         *
         * @param color
         */
        public Builder setConfirmTextColor(int color) {
            tvConfirm.setTextColor(color);
            return this;
        }

        /**
         * 设置标题按钮的字体颜色
         *
         * @param color
         */
        public Builder setTitleTextColor(int color) {
            tvTitle.setTextColor(color);
            return this;
        }


        /**
         * 设置标题的字体颜色
         *
         * @param text
         */
        public Builder setTitleText(String text) {
            if (!nextSelectShow) {
                tvTitle.setText(text);
            }
            return this;
        }


        /**
         * 设置开始标题的字体颜色
         *
         * @param text
         */
        public Builder setStartTitleText(String text) {
            startTitle = text;
            return this;
        }

        /**
         * 设置结束标题的字体颜色
         *
         * @param text
         */
        public Builder setEndTitleText(String text) {
            endTitle = text;
            return this;
        }


        /**
         * 设置是否可以分步选择
         *
         * @param nextSelectShow
         */
        public Builder setNextTimeSelectShow(boolean nextSelectShow, String nextSelectText) {
            this.nextSelectShow = nextSelectShow;
            if (nextSelectShow) {
                tvTitle.setText(startTitle);
                this.nextSelectText = nextSelectText;
                tvConfirm.setText(nextSelectText);
            }
            return this;
        }

        /**
         * 设置是否可以分步选择
         *
         * @param nextSelectShow
         */
        public Builder setNextTimeSelectShow(boolean nextSelectShow) {
            this.nextSelectShow = nextSelectShow;
            if (nextSelectShow) {
                tvTitle.setText(startTitle);
                this.nextSelectText = "下一步";
                tvConfirm.setText(nextSelectText);
            }
            return this;
        }
    }
}
