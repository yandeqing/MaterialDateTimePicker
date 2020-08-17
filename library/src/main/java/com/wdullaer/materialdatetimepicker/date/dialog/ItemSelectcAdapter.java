package com.wdullaer.materialdatetimepicker.date.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.R;
import com.wdullaer.materialdatetimepicker.date.dialog.BaseListAdapter;

import java.util.List;


/**
 * @author yandeqing
 * 备注:
 * @date 2020/8/4
 */
public class ItemSelectcAdapter extends BaseListAdapter<String> {

    /**
     * @param context
     */
    public ItemSelectcAdapter(Context context) {
        super(context);
    }


    public void setSelectedByContent(String text) {
        List<String> items = this.items;
        int size = items.size();
        for (int i = 0; i < size; i++) {
            String s = items.get(i);
            if (s.equals(text)) {
                super.setSelectedPosition(i);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (null == convertView) {
            convertView = getLayoutInflater().inflate(R.layout.item_date_select, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        holder.itemDateSelectContent.setText(item);
        holder.itemDateSelectContent.setSelected(selectedPosition == position);
        holder.itemDateSelectImg.setVisibility(selectedPosition == position ? View.VISIBLE : View.GONE);
        return convertView;
    }


    static class ViewHolder {
        TextView itemDateSelectContent;
        View itemDateSelectImg;

        ViewHolder(View itemView) {
            itemDateSelectContent = itemView.findViewById(R.id.item_date_select_content);
            itemDateSelectImg = itemView.findViewById(R.id.item_date_select_img);
        }
    }
}
