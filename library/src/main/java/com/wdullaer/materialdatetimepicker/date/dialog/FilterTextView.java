package com.wdullaer.materialdatetimepicker.date.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.wdullaer.materialdatetimepicker.R;


/**
 * 筛选按钮
 */
public class FilterTextView extends AppCompatTextView {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;

    /**
     * 图标
     */
    Drawable iconDrawable, iconDrawableSelect;

    /**
     * 字体颜色
     */
    int textColor, textColorSelected;

    /**
     * 图标位置
     */
    int iconLocation = FilterTextView.RIGHT;

    /**
     * 图标状态
     */
    private boolean iconState = false;


    public FilterTextView(Context context) {
        super(context);
        initView();
    }

    public FilterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public FilterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {

        setTextColor(textColor);
        setGravity(Gravity.CENTER_VERTICAL);

        if (iconDrawable != null) {
            setCompoundDrawables(
                    iconLocation == LEFT ? iconDrawable : null,
                    iconLocation == TOP ? iconDrawable : null,
                    iconLocation == RIGHT ? iconDrawable : null,
                    iconLocation == BOTTOM ? iconDrawable : null);
        }
    }

    /**
     * 初始化属性
     */
    private void initAttrs(AttributeSet attrs) {
        TypedArray typed = getContext().obtainStyledAttributes(attrs, R.styleable.FilterTextView);
        if (typed.hasValue(R.styleable.FilterTextView_ftv_icon)) {
            iconDrawable = typed.getDrawable(R.styleable.FilterTextView_ftv_icon);
            iconDrawable.setBounds(0, 0, iconDrawable.getMinimumWidth(), iconDrawable.getMinimumHeight());
        }
        if (typed.hasValue(R.styleable.FilterTextView_ftv_iconSelected)) {
            iconDrawableSelect = typed.getDrawable(R.styleable.FilterTextView_ftv_iconSelected);
            iconDrawableSelect.setBounds(0, 0, iconDrawableSelect.getMinimumWidth(), iconDrawableSelect.getMinimumHeight());
        }
        iconLocation = typed.getInt(R.styleable.FilterTextView_ftv_iconLocation, RIGHT);
        textColor = typed.getColor(R.styleable.FilterTextView_ftv_textColor, Color.parseColor("#666666"));
        textColorSelected = typed.getColor(R.styleable.FilterTextView_ftv_textColorSelected,  Color.parseColor("#66D4C3"));
        typed.recycle();


    }

    /**
     * 变化状态
     */
    public void toggleIconState() {
        setIconState(!iconState);
    }

    public boolean getIconState() {
        return iconState;
    }

    /**
     * 设置按钮状态
     *
     * @param isOpen 是否打开
     */
    public void setIconState(boolean isOpen) {

        this.iconState = isOpen;

        if (iconState) {

            if (iconDrawableSelect != null) {
                setCompoundDrawables(
                        iconLocation == LEFT ? iconDrawableSelect : null,
                        iconLocation == TOP ? iconDrawableSelect : null,
                        iconLocation == RIGHT ? iconDrawableSelect : null,
                        iconLocation == BOTTOM ? iconDrawableSelect : null);
            }
        } else {

            if (iconDrawable != null) {
                setCompoundDrawables(
                        iconLocation == LEFT ? iconDrawable : null,
                        iconLocation == TOP ? iconDrawable : null,
                        iconLocation == RIGHT ? iconDrawable : null,
                        iconLocation == BOTTOM ? iconDrawable : null);
            }
        }
    }

    /**
     * 设置文字
     *
     * @param text
     * @param isSelected
     */
    public void setTextWithState(String text, boolean isSelected) {
        setText(text);
        setSelected(isSelected);
    }

    /**
     * 设置文字
     *
     * @param text
     * @param isSelected
     */
    public void setLimitTextWithState(String text, boolean isSelected, int limitLength) {
        if (!TextUtils.isEmpty(text) && text.length() > limitLength) {
            setText(text.substring(0, limitLength));
        } else {
            setText(text);
        }
        setSelected(isSelected);
    }

    /**
     * 选中
     *
     * @param selected
     */
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setTextColor(selected ? textColorSelected : textColor);
    }
}
