package org.activeacademy.portal.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


public class CustomButton extends AppCompatButton {

    public CustomButton(Context context) {
        super(context);
        setupTypeface(context);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupTypeface(context);
    }

    public CustomButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupTypeface(context);
    }

    private void setupTypeface(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/opensans_regular.ttf"));
    }
}
