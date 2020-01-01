/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.victoria.vshow;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
