package com.pp.tdl.tdl;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by micha on 20.04.2017.
 * Platformy programistyczne
 */

public class ItemAddForm extends AppCompatActivity {
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.item_add_form);
    }

    public void sendFeedback(View button) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("desc", "I DID IT");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
