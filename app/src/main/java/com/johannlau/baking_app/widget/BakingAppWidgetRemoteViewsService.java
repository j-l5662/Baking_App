package com.johannlau.baking_app.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class BakingAppWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingWidgetRemoteViewsFactory(this.getApplicationContext(),intent);
    }
}
