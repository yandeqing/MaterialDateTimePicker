/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wdullaer.materialdatetimepicker.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;

public class SimpleMonthView extends MonthView {

    public SimpleMonthView(Context context, AttributeSet attr, DatePickerController controller) {
        super(context, attr, controller);
    }

    @Override
    public void drawMonthDay(Canvas canvas, int year, int month, int day,
                             int x, int y, int startX, int stopX, int startY, int stopY) {
        if (!mController.isOutOfRange(year, month, day) && mSelectedDay == day) {
            RectF rectf = new RectF(startX + 10, startY, stopX - 10, stopY + 15);
            int rx = MINI_DAY_NUMBER_TEXT_SIZE / 3;
            canvas.drawRoundRect(rectf, rx, rx, mSelectedCirclePaint);
        }

        if (isHighlighted(year, month, day) && mSelectedDay != day) {
            canvas.drawCircle(x, y + MINI_DAY_NUMBER_TEXT_SIZE - DAY_HIGHLIGHT_CIRCLE_MARGIN,
                    DAY_HIGHLIGHT_CIRCLE_SIZE, mSelectedCirclePaint);
//            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        } else {
//            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        }

        // gray out the day number if it's outside the range.
        if (mController.isOutOfRange(year, month, day)) {
            mMonthNumPaint.setColor(mDisabledDayTextColor);
        } else if (mSelectedDay == day) {
//            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mMonthNumPaint.setColor(mSelectedDayTextColor);
        } else if (mHasToday && mToday == day) {
            mMonthNumPaint.setColor(mDayTextColor);
        } else {
            mMonthNumPaint.setColor(isHighlighted(year, month, day) ? mHighlightedDayTextColor : mDayTextColor);
        }
        mMonthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        canvas.drawText(String.format(mController.getLocale(), "%d", day), x, y, mMonthNumPaint);
        if (mToday == day) {
            mMonthNumPaint.setTextSize(MINI_TODAY_TEXT_SIZE);
            if (mSelectedDay == day&&!mController.isOutOfRange(year, month, day) ) {
                mMonthNumPaint.setColor(mSelectedDayTextColor);
            } else {
                mMonthNumPaint.setColor(Color.parseColor("#FC992B"));
            }
            canvas.drawText("今天", x, y - 45, mMonthNumPaint);
        }
    }
}
