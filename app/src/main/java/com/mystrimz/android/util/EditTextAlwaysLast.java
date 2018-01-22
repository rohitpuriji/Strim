package com.mystrimz.android.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by manishjoshi on 11/1/18.
 */

public class EditTextAlwaysLast extends EditText {

    public EditTextAlwaysLast(Context context) {
        super(context);
    }

    public EditTextAlwaysLast(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextAlwaysLast(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        //if just tap - cursor to the end of row, if long press - selection menu
        if (selStart==selEnd)
            setSelection(getText().length());
        super.onSelectionChanged(selStart, selEnd);
    }


}