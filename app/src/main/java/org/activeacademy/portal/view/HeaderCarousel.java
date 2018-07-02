package org.activeacademy.portal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.activeacademy.portal.R;

/**
 * @author saifkhichi96
 * @version 1.0.0
 * @since 1.0.0 02/07/2018 5:10 PM
 */

public class HeaderCarousel extends CarouselView {

    private int[] images = {
            R.drawable.carousel_0,
            R.drawable.carousel_1,
            R.drawable.carousel_2
    };

    public HeaderCarousel(Context context) {
        super(context);
        init();
    }

    public HeaderCarousel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderCarousel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HeaderCarousel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setPageCount(images.length);
        setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(images[position]);
            }
        });
    }

}
