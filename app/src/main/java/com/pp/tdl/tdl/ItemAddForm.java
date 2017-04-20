package com.pp.tdl.tdl;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Created by micha on 20.04.2017.
 * Platformy programistyczne
 */

public class ItemAddForm extends AppCompatActivity {
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.item_add_form);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void sendFeedback(View button) {
        Intent resultIntent = new Intent();
        final EditText nameField = (EditText) findViewById(R.id.EditTextDesc);
        String desc = nameField.getText().toString();

        final EditText emailField = (EditText) findViewById(R.id.EditTextEmail);
        String email = emailField.getText().toString();

        resultIntent.putExtra("desc", desc);
        resultIntent.putExtra("email", email);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
