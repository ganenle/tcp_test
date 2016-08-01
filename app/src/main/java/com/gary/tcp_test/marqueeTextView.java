package com.gary.tcp_test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**

 * Created by Gary on 2016/8/1.
 */
public class marqueeTextView extends TextView{
    public marqueeTextView(Context context) {
        super(context);
    }

    public marqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public marqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
