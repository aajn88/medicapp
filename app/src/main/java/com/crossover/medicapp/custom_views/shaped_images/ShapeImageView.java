package com.crossover.medicapp.custom_views.shaped_images;

import android.content.Context;
import android.util.AttributeSet;

import com.crossover.medicapp.custom_views.shaped_images.shader.ShaderHelper;
import com.crossover.medicapp.custom_views.shaped_images.shader.SvgShader;


public class ShapeImageView extends ShaderImageView {

    public ShapeImageView(Context context) {
        super(context);
    }

    public ShapeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShapeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader();
    }
}
