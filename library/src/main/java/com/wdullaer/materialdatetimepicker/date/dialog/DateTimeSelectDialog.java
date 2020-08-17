package com.wdullaer.materialdatetimepicker.date.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.R;
import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.AccessibleDateAnimator;
import com.wdullaer.materialdatetimepicker.date.CustomDateRangeLimiter;
import com.wdullaer.materialdatetimepicker.date.DatePickerController;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DateRangeLimiter;
import com.wdullaer.materialdatetimepicker.date.DayPickerGroup;
import com.wdullaer.materialdatetimepicker.date.DayPickerView;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;
import com.wdullaer.materialdatetimepicker.date.MonthView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @author yandeqing
 * 备注:时间选择
 * @date 2020/8/4
 */
public class DateTimeSelectDialog extends BottomDialog implements DatePickerController, DayPickerView.OnPageListener {

    private ListView yearListView;
    private ListView monthListView;

    private FilterTextView filterYear;
    private FilterTextView filterMonth;
    private DialogConfirmTitleBar titleBar;

    @Override
    public void onPageChanged(int position) {
        MonthView mostVisibleMonth = mDayPickerView.getDayPickerView().getMostVisibleMonth();
        if (mostVisibleMonth != null) {
            int year = mostVisibleMonth.getYear();
            int month = mostVisibleMonth.getMonth();
            filterYear.setText(year + "年");
            filterMonth.setText((month + 1) + "月");
        }
    }


    /**
     * OnTimeSelectDialogListener
     */
    private OnTimeSelectDialogListener mOnTimeSelectDialogListener;


    private AccessibleDateAnimator mAnimator;
    private DayPickerGroup mDayPickerView;
    private Calendar mCalendar = Utils.trimToMidnight(Calendar.getInstance(getTimeZone()));
    private int mWeekStart = mCalendar.getFirstDayOfWeek();
    private HashSet<DatePickerDialog.OnDateChangedListener> mListeners = new HashSet<>();
    private CustomDateRangeLimiter mDefaultLimiter = new CustomDateRangeLimiter(DateTime.now().getYear() - 2, DateTime.now().getYear() + 5);
    private DateRangeLimiter mDateRangeLimiter = mDefaultLimiter;

    /**
     * 租期计算
     */
    private LinearLayout leaseTermLayout;

    /**
     * 时间选择
     */
    private TextView rentBtn1;
    private TextView rentBtn2;
    private TextView rentBtn3;

    private String titleName = "";

    /**
     * 起始时间
     */
    private DateTime startTime = null;

    /**
     * 3，6，一年的参考起始时间
     * 参考时间
     */
    private DateTime refferenceTime = DateTime.now();

    /**
     * 最小时间
     */
    private DateTime minDateTime;
    /**
     * 最大时间
     */
    private DateTime maxDateTime;

    /**
     * 是否显示租期计算
     */
    private boolean isLeaseTerm = false;

    /**
     * 是否自动加一年
     */
    private boolean isAutoAddYear = false;

    private boolean needInitSelected = false;

    ItemSelectcAdapter yearAdapter;

    ItemSelectcAdapter monthAdapter;

    public DateTimeSelectDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_datetime_select);
        yearListView = findViewById(R.id.year_list);
        monthListView = findViewById(R.id.month_list);
        titleBar = findViewById(R.id.dialog_confirm_title_bar);
        titleBar.setTitleName(titleName);
        titleBar.setOnConfirmClickListener(new DialogConfirmTitleBar.OnConfirmClickListener() {
            @Override
            public void onEnter() {

                String tag = (String) titleBar.getTitleNameView().getTag();
                if (TextUtils.isEmpty(tag)) {
                    Toast.makeText(getContext(), "请选择日期",Toast.LENGTH_LONG).show();
                    return;
                }
                DateTime selectDateTime = DateTime.create(mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

                if (mOnTimeSelectDialogListener != null) {
                    mOnTimeSelectDialogListener.onTimeSelect(selectDateTime);
                }
                dismiss();
            }

            @Override
            public void onCancel() {
                dismiss();
            }
        });
        filterYear = findViewById(R.id.dialog_datetime_filter_year);
        filterMonth = findViewById(R.id.dialog_datetime_filter_month);
        findViewById(R.id.fragment_rent_out_btn_filter_year).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yearListView.getVisibility() == View.GONE) {
                    yearListView.setVisibility(View.VISIBLE);
                    filterYear.setIconState(true);
                    monthListView.setVisibility(View.GONE);
                    filterMonth.setIconState(false);
                    //初始选中
                    yearAdapter.setSelectedByContent(filterYear.getText().toString());
                    yearListView.smoothScrollToPosition(yearAdapter.getSelectedPosition());
                } else {
                    yearListView.setVisibility(View.GONE);
                    filterYear.setIconState(false);
                }
            }
        });
        findViewById(R.id.fragment_rent_out_btn_filter_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monthListView.getVisibility() == View.GONE) {
                    monthListView.setVisibility(View.VISIBLE);
                    filterMonth.setIconState(true);
                    yearListView.setVisibility(View.GONE);
                    filterYear.setIconState(false);
                    monthAdapter.setSelectedByContent(filterMonth.getText().toString());
                    monthListView.smoothScrollToPosition(monthAdapter.getSelectedPosition());
                } else {
                    monthListView.setVisibility(View.GONE);
                    filterMonth.setIconState(false);
                }
            }
        });

        yearAdapter = new ItemSelectcAdapter(getContext());
        monthAdapter = new ItemSelectcAdapter(getContext());
        yearListView.setAdapter(yearAdapter);
        monthListView.setAdapter(monthAdapter);


        List<String> years = new ArrayList<>();
        int year = DateTime.now().getYear();
        for (int i = year - 2; i <= year + 5; i++) {
            years.add(i + "年");
        }
        yearAdapter.addItems(years);

        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(i + "月");
        }
        monthAdapter.addItems(months);


        yearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearAdapter.setSelectedPosition(position);
                yearAdapter.notifyDataSetChanged();
                int year = Integer.parseInt(years.get(position).replace("年", ""));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(mCalendar.getTimeInMillis());
                calendar1.set(Calendar.YEAR, year);
                Calendar calendar = adjustDayInMonthIfNeeded(calendar1);
                mAnimator.setDateMillis(calendar.getTimeInMillis());
                yearListView.setVisibility(View.GONE);
                filterYear.setIconState(false);
                filterYear.setText(years.get(position));
                gotoPage(createCalendar(calendar), false, false);
            }
        });
        monthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monthAdapter.setSelectedPosition(position);
                monthAdapter.notifyDataSetChanged();
                int month = Integer.parseInt(months.get(position).replace("月", ""));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(mCalendar.getTimeInMillis());
                calendar1.set(Calendar.MONTH, month - 1);
                Calendar calendar = adjustDayInMonthIfNeeded(calendar1);
                mAnimator.setDateMillis(calendar.getTimeInMillis());
                monthListView.setVisibility(View.GONE);
                filterMonth.setIconState(false);
                filterMonth.setText(months.get(position));
                gotoPage(createCalendar(calendar), false, false);
            }
        });

        leaseTermLayout = findViewById(R.id.dialog_time_select_rent_cal);
        rentBtn1 = findViewById(R.id.dialog_datetime_select_1);
        rentBtn2 = findViewById(R.id.dialog_datetime_select_2);
        rentBtn3 = findViewById(R.id.dialog_datetime_select_3);

        mAnimator = findViewById(com.wdullaer.materialdatetimepicker.R.id.mdtp_animator);
        final Context context = getContext();
        mDayPickerView = new DayPickerGroup(context, this);

        mAnimator.addView(mDayPickerView);

        rentBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateTimeAfterNMonths = DateTime.getDateTimeAfterNMonths(DateTime.create(refferenceTime.getTimeInMillis()).formatDateTime(), 3);
                monthListView.setVisibility(View.GONE);
                yearListView.setVisibility(View.GONE);
                MonthAdapter.CalendarDay calendar = createCalendar(dateTimeAfterNMonths.getCalendar());
                gotoPage(calendar, false, true);
                setCalendar(dateTimeAfterNMonths.getCalendar());
//                mDayPickerView.onDateChanged();
                DateTime dateTime = DateTime.create(dateTimeAfterNMonths.getTimeInMillis());
                titleBar.setTitleName(String.format("已选“%s”", dateTime.formatDateTime()));
                titleBar.getTitleNameView().setTag(dateTime.formatDateTime());
                filterYear.setText(dateTime.getYear() + "年");
                filterMonth.setText((dateTime.getMonth() + 1) + "月");
            }
        });

        rentBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateTimeAfterNMonths = DateTime.getDateTimeAfterNMonths(DateTime.create(refferenceTime.getTimeInMillis()).formatDateTime(), 6);
                monthListView.setVisibility(View.GONE);
                yearListView.setVisibility(View.GONE);
                MonthAdapter.CalendarDay calendar = createCalendar(dateTimeAfterNMonths.getCalendar());
                gotoPage(calendar, false, true);
                setCalendar(dateTimeAfterNMonths.getCalendar());
//                mDayPickerView.onDateChanged();
                DateTime dateTime = DateTime.create(dateTimeAfterNMonths.getTimeInMillis());
                titleBar.setTitleName(String.format("已选“%s”", dateTime.formatDateTime()));
                titleBar.getTitleNameView().setTag(dateTime.formatDateTime());
                filterYear.setText(dateTime.getYear() + "年");
                filterMonth.setText((dateTime.getMonth() + 1) + "月");
            }
        });

        rentBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateTimeAfterNMonths = DateTime.getDateTimeAfterNMonths(DateTime.create(refferenceTime.getTimeInMillis()).formatDateTime(), 12);
                monthListView.setVisibility(View.GONE);
                yearListView.setVisibility(View.GONE);
                MonthAdapter.CalendarDay calendar = createCalendar(dateTimeAfterNMonths.getCalendar());
                gotoPage(calendar, false, true);
                setCalendar(dateTimeAfterNMonths.getCalendar());
//                mDayPickerView.onDateChanged();
                DateTime dateTime = DateTime.create(dateTimeAfterNMonths.getTimeInMillis());
                titleBar.setTitleName(String.format("已选“%s”", dateTime.formatDateTime()));
                titleBar.getTitleNameView().setTag(dateTime.formatDateTime());
                filterYear.setText(dateTime.getYear() + "年");
                filterMonth.setText((dateTime.getMonth() + 1) + "月");
            }
        });
        leaseTermLayout.setVisibility(isLeaseTerm ? View.VISIBLE : View.GONE);
        ViewGroup.LayoutParams layoutParams = monthListView.getLayoutParams();
        layoutParams.height = DisplayUtils.dp2px(getContext(), isLeaseTerm ? 369 : 324);
        monthListView.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams layoutParams1 = yearListView.getLayoutParams();
        layoutParams1.height = DisplayUtils.dp2px(getContext(), isLeaseTerm ? 369 : 324);
        yearListView.setLayoutParams(layoutParams1);
        if (isLeaseTerm) {
            if (isAutoAddYear) {
                rentBtn3.performClick();
            }
        }
        if (startTime == null) {
            startTime = DateTime.now();
        }
        initialize(startTime.getCalendar());

        if (needInitSelected) {
            titleBar.setTitleName(String.format("已选“%s”", DateTime.create(mCalendar.getTimeInMillis()).formatDateTime()));
            titleBar.getTitleNameView().setTag(DateTime.create(mCalendar.getTimeInMillis()).formatDateTime());
        }

        mAnimator.setDateMillis(mCalendar.getTimeInMillis());
        filterYear.setText(startTime.getYear() + "年");
        filterMonth.setText((startTime.getMonth() + 1) + "月");
        mDayPickerView.getDayPickerView().goTo(createCalendar(mCalendar), false, needInitSelected, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDayPickerView.getDayPickerView().setOnPageListener(DateTimeSelectDialog.this);
            }
        }, 300);
    }


    private void gotoPage(MonthAdapter.CalendarDay calendar, boolean animate, boolean selected) {
        mDayPickerView.getDayPickerView().setOnPageListener(null);
        mDayPickerView.getDayPickerView().goTo(calendar, animate, selected, true);
        new Handler().postDelayed(() -> mDayPickerView.getDayPickerView().setOnPageListener(DateTimeSelectDialog.this), 300);
    }

    public DateTimeSelectDialog setStartTime(DateTime dateTime) {
        this.startTime = dateTime;
        return this;
    }

    public DateTimeSelectDialog initialize(Calendar initialSelection) {
        mCalendar = Utils.trimToMidnight((Calendar) initialSelection.clone());
        return this;
    }

    private void setCalendar(Calendar calendar) {
        mCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        mCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        mCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public DateTimeSelectDialog setRefferenceTime(DateTime refferenceTime) {
        this.refferenceTime = refferenceTime;
        return this;
    }

    public DateTimeSelectDialog setTitleName(String titleName) {
        this.titleName = titleName;
        return this;
    }

    public DateTimeSelectDialog setIsAutoAddYear(boolean isAutoAddYear) {
        this.isAutoAddYear = isAutoAddYear;
        return this;
    }

    public DateTimeSelectDialog setNeedInitSelected(boolean needInitSelected) {
        this.needInitSelected = needInitSelected;
        return this;
    }

    public DateTimeSelectDialog setMinDateTime(DateTime minDateTime) {
        this.minDateTime = minDateTime;
        this.mDefaultLimiter.setMinDate(minDateTime.getCalendar());
        return this;
    }

    public DateTimeSelectDialog setMaxDateTime(DateTime maxDateTime) {
        this.maxDateTime = maxDateTime;
        this.mDefaultLimiter.setMaxDate(maxDateTime.getCalendar());
        return this;
    }


    public DateTimeSelectDialog setLeaseTerm(boolean leaseTerm) {
        isLeaseTerm = leaseTerm;
        return this;
    }

    public DateTimeSelectDialog setOnTimeSelectDialogListener(OnTimeSelectDialogListener l) {
        this.mOnTimeSelectDialogListener = l;
        return this;
    }

    private Calendar adjustDayInMonthIfNeeded(Calendar calendar) {
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        if (day > daysInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//        }
        return calendar;
    }

    @Override
    public void onYearSelected(int year) {
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        updatePickers();
        titleBar.setTitleName(String.format("已选“%s”", DateTime.create(mCalendar.getTimeInMillis()).formatDateTime()));
        titleBar.getTitleNameView().setTag(DateTime.create(mCalendar.getTimeInMillis()).formatDateTime());
    }


    private void updatePickers() {
        for (DatePickerDialog.OnDateChangedListener listener : mListeners) {
            listener.onDateChanged();
        }
    }


    @Override
    public MonthAdapter.CalendarDay getSelectedDay() {
        return new MonthAdapter.CalendarDay(mCalendar, getTimeZone());
    }

    /**
     * @param calendar
     * @return
     */
    public MonthAdapter.CalendarDay createCalendar(Calendar calendar) {
        return new MonthAdapter.CalendarDay(calendar, getTimeZone());
    }

    @Override
    public boolean isThemeDark() {
        return false;
    }

    @Override
    public int getAccentColor() {
//        return Color.parseColor("#FC992B");
        return Utils.getAccentColorFromThemeIfAvailable(getContext());
    }

    @Override
    public boolean isHighlighted(int year, int month, int day) {
        return false;
    }

    @Override
    public Calendar getStartDate() {
        return mDateRangeLimiter.getStartDate();
    }

    @Override
    public Calendar getEndDate() {
        return mDateRangeLimiter.getEndDate();
    }

    @Override
    public int getMinYear() {
        return mDateRangeLimiter.getMinYear();
    }

    @Override
    public int getMaxYear() {
        return mDateRangeLimiter.getMaxYear();
    }


    @Override
    public boolean isOutOfRange(int year, int month, int day) {
        return mDateRangeLimiter.isOutOfRange(year, month, day);
    }

    @Override
    public int getFirstDayOfWeek() {
        return mWeekStart;
    }

    @Override
    public void registerOnDateChangedListener(DatePickerDialog.OnDateChangedListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void unregisterOnDateChangedListener(DatePickerDialog.OnDateChangedListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void tryVibrate() {
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    @Override
    public DatePickerDialog.Version getVersion() {
        return DatePickerDialog.Version.VERSION_2;
    }

    @Override
    public DatePickerDialog.ScrollOrientation getScrollOrientation() {
        return DatePickerDialog.ScrollOrientation.VERTICAL;
    }


    public interface OnTimeSelectDialogListener {

        void onTimeSelect(DateTime dateTime);
    }

}
