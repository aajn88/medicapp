package com.crossover.medicapp.custom_views.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MaterialDesignIconsTextView extends TextView {
	
	private static Typeface sMaterialDesignIcons;

	public MaterialDesignIconsTextView(Context context) {
		this(context, null);
	}

	public MaterialDesignIconsTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaterialDesignIconsTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setTypeface();
	}
	
	private void setTypeface() {
		if (sMaterialDesignIcons == null) {
			sMaterialDesignIcons = Typeface
					.createFromAsset(getContext().getAssets(), "fonts/MaterialDesignIcons.ttf");
		}
		setTypeface(sMaterialDesignIcons);
	}
}
