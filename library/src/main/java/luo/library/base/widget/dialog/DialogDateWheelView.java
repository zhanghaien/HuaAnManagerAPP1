package luo.library.base.widget.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.wheelview.OnWheelChangedListener;
import com.wheelview.WheelView;
import com.wheelview.adapters.ArrayWheelAdapter;
import com.wheelview.bean.NameTypeBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import luo.library.R;


/**
 * 带有WheelView 的Dialog 选择年份、月份、日
 * Created by john lee on 2017/5/20.
 */
public class DialogDateWheelView extends AlertDialog {

    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;

    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_MONTH = 1;
    private static final int DEFAULT_END_MONTH = 12;
    private static final int DEFAULT_START_DAY = 1;
    private static final int DEFAULT_END_DAY = 31;

    private int startYear = DEFAULT_START_YEAR;
    private int endYear = DEFAULT_END_YEAR;
    private int startMonth = DEFAULT_START_MONTH;
    private int endMonth = DEFAULT_END_MONTH;
    private int startDay = DEFAULT_START_DAY;
    private int endDay = DEFAULT_END_DAY; //表示31天的
    private int currentYear;

    private Calendar date;//当前选中时间
    private Calendar startDate;//开始时间
    private Calendar endDate;//终止时间

    private Context context;
    private OnConfirmListener onConfirmListener;

    public interface OnConfirmListener {
        void onConfirm(String year);
    }

    public DialogDateWheelView(Context context, OnConfirmListener onConfirmListener) {
        super(context, R.style.AlertDialogTheme);
        this.context = context;
        this.onConfirmListener = onConfirmListener;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_date_wheel_view, null);
        setView(view);
        setCanceledOnTouchOutside(false);

        wv_year = (WheelView) view.findViewById(R.id.wheel_year);
        wv_month = (WheelView) view.findViewById(R.id.wheel_month);
        wv_day = (WheelView) view.findViewById(R.id.wheel_day);

        wv_year.setVisibleItems(8);
        wv_month.setVisibleItems(8);
        wv_day.setVisibleItems(8);

        wv_year.setCyclic(false);
        wv_month.setCyclic(false);
        wv_day.setCyclic(false);

        date = Calendar.getInstance();

        startDate = Calendar.getInstance();
        startDate.set(DEFAULT_START_YEAR, DEFAULT_START_MONTH, DEFAULT_START_DAY);

        endDate = Calendar.getInstance();

        if (startDate != null && endDate != null) {
            if (startDate.getTimeInMillis() <= endDate.getTimeInMillis()) {
                setRangDate();
            }
        } else if (startDate != null && endDate == null) {
            setRangDate();
        } else if (startDate == null && endDate != null) {
            setRangDate();
        }

        setTime();

        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 获取日期
     *
     * @return
     */
    public String getTime() {
        StringBuffer sb = new StringBuffer();
        if (currentYear == startYear) {
            if ((wv_month.getCurrentItem() + startMonth) == startMonth) {
                sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                        .append((format(wv_month.getCurrentItem() + startMonth))).append("-")
                        .append((format(wv_day.getCurrentItem() + startDay)));
            } else {
                sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                        .append((format(wv_month.getCurrentItem() + startMonth))).append("-")
                        .append((format(wv_day.getCurrentItem() + 1)));
            }
        } else {
            sb.append((wv_year.getCurrentItem() + startYear)).append("-")
                    .append((format(wv_month.getCurrentItem() + 1))).append("-")
                    .append((format(wv_day.getCurrentItem() + 1)));
        }
        return sb.toString();
    }

    private String format(int time) {
        StringBuffer sb = new StringBuffer();
        if (time < 10)
            sb.append("0");
        sb.append(time);
        return sb.toString();
    }

    /**
     * 设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRangDate() {
        setRangDate(startDate, endDate);
        //如果设置了时间范围
        if (startDate != null && endDate != null) {
            //判断一下默认时间是否设置了，或者是否在起始终止时间范围内
            if (date == null || date.getTimeInMillis() < startDate.getTimeInMillis()
                    || date.getTimeInMillis() > endDate.getTimeInMillis()) {
                date = startDate;
            }
        } else if (startDate != null) {
            //没有设置默认选中时间,那就拿开始时间当默认时间
            date = startDate;
        } else if (endDate != null) {
            date = endDate;
        }
    }

    public void setRangDate(Calendar startDate, Calendar endDate) {

        if (startDate == null && endDate != null) {
            int year = endDate.get(Calendar.YEAR);
            int month = endDate.get(Calendar.MONTH) + 1;
            int day = endDate.get(Calendar.DAY_OF_MONTH);
            if (year > startYear) {
                this.endYear = year;
                this.endMonth = month;
                this.endDay = day;
            } else if (year == startYear) {
                if (month > startMonth) {
                    this.endYear = year;
                    this.endMonth = month;
                    this.endDay = day;
                } else if (month == startMonth) {
                    if (month > startDay) {
                        this.endYear = year;
                        this.endMonth = month;
                        this.endDay = day;
                    }
                }
            }

        } else if (startDate != null && endDate == null) {
            int year = startDate.get(Calendar.YEAR);
            int month = startDate.get(Calendar.MONTH) + 1;
            int day = startDate.get(Calendar.DAY_OF_MONTH);
            if (year < endYear) {
                this.startMonth = month;
                this.startDay = day;
                this.startYear = year;
            } else if (year == endYear) {
                if (month < endMonth) {
                    this.startMonth = month;
                    this.startDay = day;
                    this.startYear = year;
                } else if (month == endMonth) {
                    if (day < endDay) {
                        this.startMonth = month;
                        this.startDay = day;
                        this.startYear = year;
                    }
                }
            }

        } else if (startDate != null && endDate != null) {
            this.startYear = startDate.get(Calendar.YEAR);
            this.endYear = endDate.get(Calendar.YEAR);
            this.startMonth = startDate.get(Calendar.MONTH) + 1;
            this.endMonth = endDate.get(Calendar.MONTH) + 1;
            this.startDay = startDate.get(Calendar.DAY_OF_MONTH);
            this.endDay = endDate.get(Calendar.DAY_OF_MONTH);

        }

    }

    /**
     * 设置选中时间,默认选中当前时间
     */
    private void setTime() {
        int year, month, day;

        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            year = date.get(Calendar.YEAR);
            month = date.get(Calendar.MONTH);
            day = date.get(Calendar.DAY_OF_MONTH);
        }

        setPicker(year, month, day);
    }


    public void setPicker(int year, final int month, int day) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

      /*  final Context context = view.getContext();*/
        currentYear = year;
        // 年
        wv_year.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startYear, endYear)));// 设置"年"的显示数据

        /*wv_year.setLabel(context.getString(R.string.pickerview_year));// 添加文字*/
        wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据
        // 月
        if (startYear == endYear) {//开始年等于终止年
            wv_month.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startMonth, endMonth)));
            wv_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == startYear) {
            //起始日期的月份控制
            wv_month.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startMonth, 12)));
            wv_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == endYear) {
            //终止日期的月份控制
            wv_month.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, endMonth)));
            wv_month.setCurrentItem(month);
        } else {
            wv_month.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, 12)));
            wv_month.setCurrentItem(month);
        }
     /*   wv_month.setLabel(context.getString(R.string.pickerview_month));*/

        // 日
        if (startYear == endYear && startMonth == endMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startDay, endDay)));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startDay, endDay)));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startDay, endDay)));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startDay, endDay)));
                }
            }
            wv_day.setCurrentItem(day - startDay);
        } else if (year == startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startDay, 31)));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startDay, 30)));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startDay, 29)));
                } else {

                    wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startDay, 28)));
                }
            }
            wv_day.setCurrentItem(day - startDay);
        } else if (year == endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, endDay)));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, endDay)));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, endDay)));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, endDay)));
                }
            }
            wv_day.setCurrentItem(day - 1);
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {

                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, 31)));
            } else if (list_little.contains(String.valueOf(month + 1))) {

                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, 30)));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {

                    wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, 29)));
                } else {

                    wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, 28)));
                }
            }
            wv_day.setCurrentItem(day - 1);
        }

       /* wv_day.setLabel(context.getString(R.string.pickerview_day));*/

        // 添加"年"监听
        wv_year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + startYear;
                currentYear = year_num;
                int currentMonthItem = wv_month.getCurrentItem();//记录上一次的item位置
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (startYear == endYear) {
                    //重新设置月份
                    wv_month.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startMonth, endMonth)));

                    if (currentMonthItem > wv_month.getViewAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_month.getViewAdapter().getItemsCount() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }

                    int monthNum = currentMonthItem + startMonth;

                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, endDay, list_big, list_little);
                    } else if (monthNum == startMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }
                } else if (year_num == startYear) {//等于开始的年
                    //重新设置月份
                    wv_month.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startMonth, 12)));

                    if (currentMonthItem > wv_month.getViewAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_month.getViewAdapter().getItemsCount() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }

                    int month = currentMonthItem + startMonth;
                    if (month == startMonth) {

                        //重新设置日
                        setReDay(year_num, month, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日

                        setReDay(year_num, month, 1, 31, list_big, list_little);
                    }

                } else if (year_num == endYear) {
                    //重新设置月份
                    wv_month.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, endMonth)));
                    if (currentMonthItem > wv_month.getVisibleItems() - 1) {
                        currentMonthItem = wv_month.getVisibleItems() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }
                    int monthNum = currentMonthItem + 1;

                    if (monthNum == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }

                } else {
                    //重新设置月份
                    wv_month.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(1, 12)));
                    //重新设置日
                    setReDay(year_num, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);

                }
            }
        });

        // 添加"月"监听
        wv_month.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;

                if (startYear == endYear) {
                    month_num = month_num + startMonth - 1;
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, endDay, list_big, list_little);
                    } else if (startMonth == month_num) {

                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else if (endMonth == month_num) {
                        setReDay(currentYear, month_num, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }
                } else if (currentYear == startYear) {
                    month_num = month_num + startMonth - 1;
                    if (month_num == startMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }

                } else if (currentYear == endYear) {
                    if (month_num == endMonth) {
                        //重新设置日
                        setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
                    }

                } else {
                    //重新设置日
                    setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                }

            }
        });
    }


    private void setReDay(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
        int currentItem = wv_day.getCurrentItem();

        int maxItem;
        if (list_big
                .contains(String.valueOf(monthNum))) {
            if (endD > 31) {
                endD = 31;
            }
            wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startD, endD)));
            maxItem = endD;
        } else if (list_little.contains(String.valueOf(monthNum))) {
            if (endD > 30) {
                endD = 30;
            }
            wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startD, endD)));
            maxItem = endD;
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                if (endD > 29) {
                    endD = 29;
                }
                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startD, endD)));
                maxItem = endD;
            } else {
                if (endD > 28) {
                    endD = 28;
                }
                wv_day.setViewAdapter(new ArrayWheelAdapter(context, getNameTypes(startD, endD)));
                maxItem = endD;
            }
        }

        if (currentItem > wv_day.getViewAdapter().getItemsCount() - 1) {
            currentItem = wv_day.getViewAdapter().getItemsCount() - 1;
            wv_day.setCurrentItem(currentItem);
        }
    }

    private List<NameTypeBean> getNameTypes(int start, int end) {
        List<NameTypeBean> nameTypes = new ArrayList<>();
        for (int i = 0; i < end - start + 1; i++) {
            NameTypeBean nameType = new NameTypeBean(start + i + "", start + i + "");
            nameTypes.add(nameType);
        }
        return nameTypes;
    }

    private void confirm() {
        if (onConfirmListener != null) {
            onConfirmListener.onConfirm(getTime());
        }
        dismiss();
    }

}
