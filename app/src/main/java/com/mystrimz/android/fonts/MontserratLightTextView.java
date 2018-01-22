package com.mystrimz.android.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by manishjoshi on 5/1/18.
 */

public class MontserratLightTextView extends TextView {
    Context context;
    public MontserratLightTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
        init();
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "Montserrat-Light.otf");
        setTypeface(font);
    }
}
