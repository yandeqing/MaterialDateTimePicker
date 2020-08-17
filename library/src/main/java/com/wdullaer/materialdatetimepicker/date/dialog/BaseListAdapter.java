package com.wdullaer.materialdatetimepicker.date.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import androidx.annotation.StringRes;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseListAdapter
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    // Activity
    protected WeakReference<Context> mContext;

    /**
     * 适配器数据
     */
    protected List<T> items;

    // 选择Position
    protected int selectedPosition = -1;

    /**
     * 构造方法
     *
     * @param ctx Context
     */
    public BaseListAdapter(Context ctx) {
        mContext = new WeakReference<>(ctx);
    }

    /**
     * 返回视图的总数
     */
    @Override
    public int getCount() {
        return items != null && !items.isEmpty() ? items.size() : 0;
    }

    /**
     * 返回单个item
     *
     * @param position 返回item的position
     */
    @Override
    public T getItem(int position) {
        return items != null && items.size() > position ? items.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 设置选择项位置
     *
     * @param position
     * @param isNotifyDataSetChanged
     */
    public void setSelectedPosition(int position, boolean isNotifyDataSetChanged) {
        selectedPosition = position;
        if (isNotifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    public void setSelectedPosition(int position) {
        setSelectedPosition(position, true);
    }

    /**
     * 获取选择位置
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }


    /**
     * 返回所有Items
     */
    public List<T> getItems() {
        return items;
    }


    /**
     * 增加单个Item
     *
     * @param newItem
     */
    public void addItem(T newItem) {
        if (null == items) {
            items = new ArrayList<>();
        }
        items.add(newItem);
        notifyDataSetChanged();
    }

    public void addItem(int position, T newItem) {
        if (null == items) {
            items = new ArrayList<>();
        }
        items.add(position, newItem);
        notifyDataSetChanged();
    }

    /**
     * 增加多个Item
     *
     * @param newItems
     */
    public void addItems(List<T> newItems) {
        if (null == items) {
            items = new ArrayList<>();
        }
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    /**
     * 设置Items
     * 将原有集合内的所有元素删除
     *
     * @param newItems
     */
    public void setItems(List<T> newItems) {
        setItems(newItems, true);
    }

    public void setItems(List<T> newItems, boolean isNotifyDataSetChanged) {
        if (null == items) {
            items = new ArrayList<>();
        } else {
            items.clear();
        }
        items.addAll(newItems);
        if (isNotifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除所有Items
     */
    public void removeItems() {
        removeItems(true);
    }

    /**
     * 删除所有Items
     *
     * @param isNotifyDataSetChanged 是否立刻刷新数据
     */
    public void removeItems(boolean isNotifyDataSetChanged) {
        if (null == items) {
            return;
        }
        items.clear();
        if (isNotifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除单个Item
     */
    public void removeItem(T item) {
        if (null == items || items.isEmpty()) {
            return;
        }
        items.remove(item);
        notifyDataSetChanged();
    }

    /**
     * 根据position删除
     */
    public void removeItem(int position) {
        if (null == items || items.isEmpty()) {
            return;
        }
        items.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 返回LayoutInflater
     *
     * @return
     */
    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext.get());
    }

    /**
     * 获取字符
     *
     * @param resId      字符ID
     * @param formatArgs 格式化参数
     */
    protected String getString(@StringRes int resId, Object... formatArgs) {
        return mContext.get().getString(resId, formatArgs);
    }

}