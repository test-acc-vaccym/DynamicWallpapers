package amirz.dynamicwallpapers;

import android.content.Context;

/**
 * Keeps track of timing after locking/unlocking, and gets the variables for effects
 */
public class StateTransitions {
    /**
     * Constants in milliseconds
     * 1000 / FAST_UPDATES is the framerate
     */
    private final static int FAST_UPDATES = 2;
    private final static int SCHEDULED_UPDATES = 60 * 1000;
    private final static int UNLOCK_BLUR = 500;
    private final static int MAX_BLUR = 25;

    private final Context mContext;
    private final Runnable mUpdate;

    private boolean mUnlocked;
    private long mLastChange;

    StateTransitions(Context context, Runnable update) {
        mContext = context;
        mUpdate = update;
    }

    float getSaturation() {
        return 1f;
    }

    float getContrast() {
        return 1f;
    }

    int delayToNext() {
         return mUnlocked && System.currentTimeMillis() - mLastChange <= UNLOCK_BLUR ?
                 FAST_UPDATES :
                 SCHEDULED_UPDATES;
    }

    int getBlur() {
        if (mUnlocked) {
            long absoluteBlur = (System.currentTimeMillis() - mLastChange) * MAX_BLUR;
            return Math.max(0, MAX_BLUR - (int) (absoluteBlur / UNLOCK_BLUR));
        }
        return MAX_BLUR;
    }

    void setUnlocked(boolean unlocked) {
        if (mUnlocked != unlocked) {
            mLastChange = System.currentTimeMillis();
        }
        mUnlocked = unlocked;
        mUpdate.run();
    }
}
