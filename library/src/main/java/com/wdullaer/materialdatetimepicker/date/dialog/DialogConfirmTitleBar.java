package com.wdullaer.materialdatetimepicker.date.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;

import com.wdullaer.materialdatetimepicker.R;


/**
 * DialogConfirmTitleBar
 * 确定取消TitleBar
 * <p>
 * Created by suntongwei on 2017/12/13.
 */
public class DialogConfirmTitleBar extends RelativeLayout {

    /**
     * RootLayout
     */
    RelativeLayout rootLayout;

    /**
     * 标题
     */
    TextView titleNameView;

    /**
     * 确定
     */
    TextView btnEnter;

    /**
     * 取消
     */
    TextView btnCancel;

    /**
     * 事件监听器
     */
    private DialogConfirmTitleBar.OnConfirmClickListener mOnConfirmClickListener;

    public DialogConfirmTitleBar(Context context) {
        super(context);
        initView();
    }

    public DialogConfirmTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttr(attrs);
    }

    public DialogConfirmTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DialogConfirmTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
        initAttr(attrs);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_title_bar, this, true);
        rootLayout = findViewById(R.id.dialog_confirm_title_bar_root);
        titleNameView = findViewById(R.id.dialog_confirm_title_bar_title_name);
        btnEnter = findViewById(R.id.btn_enter);
        btnCancel = findViewById(R.id.btn_cancel);

        btnEnter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onEnter();
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmClickListener != null) {
                    mOnConfirmClickListener.onCancel();
                }
            }
        });
    }

    /**
     * 初始化参数
     *
     * @param attrs
     */
    private void initAttr(AttributeSet attrs) {
        TypedArray typed = getContext().obtainStyledAttributes(attrs, R.styleable.DialogConfirmTitleBar);
        // 设置标题
        if (typed.hasValue(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_titleName)) {
            setTitleName(typed.getString(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_titleName));
        }
        // 设置标题颜色
        if (typed.hasValue(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_titleNameColor)) {
            setTitleNameColor(typed.getColor(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_titleNameColor, Color.WHITE));
        }
        // 是否需要显示按钮
        if (typed.hasValue(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_hasButton)) {
            boolean isShowButton = typed.getBoolean(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_hasButton, true);
            btnEnter.setVisibility(isShowButton ? View.VISIBLE : View.GONE);
            btnCancel.setVisibility(isShowButton ? View.VISIBLE : View.GONE);
        }
        // 设置背景色
        if (typed.hasValue(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_background)) {
            setTitleBarBackgroud(typed.getColor(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_background, Color.WHITE));
        }
        // 设置左边按钮颜色
        if (typed.hasValue(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_leftButtonColor)) {
            btnCancel.setTextColor(typed.getColor(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_leftButtonColor, Color.WHITE));
        }
        // 设置右边按钮颜色
        if (typed.hasValue(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_rightButtonColor)) {
            btnEnter.setTextColor(typed.getColor(R.styleable.DialogConfirmTitleBar_dialogConfirmTitleBar_rightButtonColor, Color.WHITE));
        }
        typed.recycle();
    }

    /**
     * 设置标题
     *
     * @param titleName
     * @return
     */
    public DialogConfirmTitleBar setTitleName(String titleName) {
        titleNameView.setText(titleName);
        return this;
    }

    public DialogConfirmTitleBar setTitleName(@StringRes int resId) {
        titleNameView.setText(resId);
        return this;
    }

    /**
     * 设置标题字体颜色
     *
     * @param color
     * @return this
     */
    public DialogConfirmTitleBar setTitleNameColor(@ColorInt int color) {
        titleNameView.setTextColor(color);
        return this;
    }

    /**
     * 设置右边按钮文字
     */
    public DialogConfirmTitleBar setRightText(String titleName) {
        btnEnter.setText(titleName);
        return this;
    }

    public TextView getBtnEnter() {
        return btnEnter;
    }

    /**
     * @return 返回标题视图
     */
    public TextView getTitleNameView() {
        return titleNameView;
    }

    /**
     * 设置背景色
     *
     * @param color
     * @return
     */
    public DialogConfirmTitleBar setTitleBarBackgroud(@ColorInt int color) {
        rootLayout.setBackgroundColor(color);
        return this;
    }

    public DialogConfirmTitleBar.OnConfirmClickListener getOnConfirmClickListener() {
        return mOnConfirmClickListener;
    }

    public void setOnConfirmClickListener(DialogConfirmTitleBar.OnConfirmClickListener mOnConfirmClickListener) {
        this.mOnConfirmClickListener = mOnConfirmClickListener;
    }

    /**
     * OnConfirmClickListener
     */
    public interface OnConfirmClickListener {

        /**
         * 点击确认
         */
        void onEnter();

        /**
         * 点击取消
         */
        void onCancel();

    }
}