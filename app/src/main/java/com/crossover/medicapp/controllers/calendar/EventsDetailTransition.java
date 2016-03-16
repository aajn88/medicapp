package com.crossover.medicapp.controllers.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.AttributeSet;

/**
 * Esta clase contiene las transiciones para la transicion del calendario al detalle del evento
 *
 * @author <a href="mailto:antonio-jimenez@accionplus.com">Antonio A. Jimenez N.</a>
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class EventsDetailTransition extends TransitionSet {

    /**
     * Constructs an empty transition set. Add child transitions to the
     * set by calling {@link #addTransition(Transition)}. By default,
     * child transitions will play {@link #ORDERING_TOGETHER together}.
     */
    public EventsDetailTransition() {
        init();
    }

    public EventsDetailTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Init transitions
     */
    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
    }

}
