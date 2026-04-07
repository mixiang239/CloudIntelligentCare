package com.ggg.common.Utils;

import android.content.Context;
import android.util.TypedValue;

public class UIUtils {
    public static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }
}
