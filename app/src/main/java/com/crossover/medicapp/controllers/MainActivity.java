package com.crossover.medicapp.controllers;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.crossover.medicapp.R;

import roboguice.RoboGuice;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * This is the Medicapp Mai Activity where the splash will be displayed. Here is where the decision
 * to go to home or login is taken. This depends if there is an active user
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity {

    /** Constant for animations duration **/
    private static final int ANIM_DURATION = 1000;
    /** Delay for the splash view **/
    private static final int SPLASH_DELAY = 2500;

    static {
        RoboGuice.setUseAnnotationDatabases(false);
    }

    /** The Medicapp Logo ImageView **/
    @InjectView(R.id.medicapp_logo_iv)
    private ImageView mMedicappLogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fadeAnimation(mMedicappLogoIv);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goLogin();
            }
        }, SPLASH_DELAY);

    }

    /**
     * This method redirects to HomeActivity in the properly manner
     */
    private void goLogin() {
        Intent homeIntent = new Intent(this, LoginActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, mMedicappLogoIv,
                        getString(R.string.transition_body));
        startActivity(homeIntent, options.toBundle());
    }

    /**
     * This method creates a fade animation that suddenly appears "in front" of the screen to the
     * back of the screen
     */
    private void fadeAnimation(View view) {
        ObjectAnimator scaleXAnimation = createObjectAnimator(view, "scaleX", 5.0F, 1.0F);
        ObjectAnimator scaleYAnimation = createObjectAnimator(view, "scaleY", 5.0F, 1.0F);
        ObjectAnimator alphaAnimation = createObjectAnimator(view, "alpha", 0.0F, 1.0F);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.start();
    }

    /**
     * This method creates an Object Animator based on the targeted view, the property to be
     * animated and the initial value and final value
     *
     * @param view
     *         Target view
     * @param property
     *         Property to be animated
     * @param init
     *         Initial value
     * @param end
     *         Final value
     *
     * @return ObjectAnimator with the given animated property
     */
    @NonNull
    private ObjectAnimator createObjectAnimator(View view, String property, float init, float end) {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(view, property, init, end);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(ANIM_DURATION);
        return scaleXAnimation;
    }

}
