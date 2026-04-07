package com.ggg.common.Interface;

import android.content.Context;

public interface IHostActivity {
    void navigateTo(String target);
    Context getHostActivity();
}
