package com.crossover.medicapp.custom_views.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontelloTextView extends TextView {
	
	private static Typeface sFontello;

	public FontelloTextView(Context context) {
		super(context);
		setTypeface();
	}

	public FontelloTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setTypeface();
	}

	public FontelloTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setTypeface();
	}
	
	private void setTypeface() {
		if (sFontello == null) {
			sFontello = Typeface.createFromAsset(getContext().getAssets(), "fonts/Fontello.ttf");
		}
		setTypeface(sFontello);
	}
}
