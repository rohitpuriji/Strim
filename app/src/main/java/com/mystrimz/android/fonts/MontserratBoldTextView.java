package com.mystrimz.android.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by manishjoshi on 4/1/18.
 */

public class MontserratBoldTextView extends TextView{
    Context context;

    public MontserratBoldTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Montserrat-Bold.otf");
        setTypeface(font);
    }
}
