package ncucsie.cas;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;


public class NewAppointmentActivity extends Activity {
    public NewAppointmentActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_appointment_activity);

        View actionBarButtons = getLayoutInflater().inflate(R.layout.custom_form_custom_actionbar,
                new LinearLayout(this), false);
        View cancelActionView = actionBarButtons.findViewById(R.id.action_cancel);
        cancelActionView.setOnClickListener(mActionBarListener);
        View doneActionView = actionBarButtons.findViewById(R.id.action_done);
        doneActionView.setOnClickListener(mActionBarListener);
        if(getActionBar() != null) {
            getActionBar().setDisplayShowCustomEnabled(true);
            getActionBar().setCustomView(actionBarButtons);
            getActionBar().setIcon(
                    new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }

        Spinner spinner_classroom = (Spinner) findViewById(R.id.spinner_classroom);
        ArrayAdapter<CharSequence> adapter_classroom = ArrayAdapter.createFromResource(this,
                R.array.preferenceListArray, android.R.layout.simple_spinner_item);
        spinner_classroom.setAdapter(adapter_classroom);

        Spinner spinner_year = (Spinner) findViewById(R.id.spinner_year);
        ArrayAdapter<CharSequence> adapter_year = ArrayAdapter.createFromResource(this,
                R.array.preferenceYearListArray, android.R.layout.simple_spinner_item);
        spinner_year.setAdapter(adapter_year);

        Spinner spinner_month = (Spinner) findViewById(R.id.spinner_month);
        ArrayAdapter<CharSequence> adapter_month = ArrayAdapter.createFromResource(this,
                R.array.preferenceMonthListArray, android.R.layout.simple_spinner_item);
        spinner_month.setAdapter(adapter_month);

        Spinner spinner_day = (Spinner) findViewById(R.id.spinner_day);
        ArrayAdapter<CharSequence> adapter_day = ArrayAdapter.createFromResource(this,
                R.array.preferenceDayListArray, android.R.layout.simple_spinner_item);
        spinner_day.setAdapter(adapter_day);

        Spinner class_start = (Spinner) findViewById(R.id.spinner_class_start);
        ArrayAdapter<CharSequence> adapter_start = ArrayAdapter.createFromResource(this,
                R.array.timevalue, android.R.layout.simple_spinner_item);
        class_start.setAdapter(adapter_start);

        Spinner class_end = (Spinner) findViewById(R.id.spinner_class_end);
        ArrayAdapter<CharSequence> adapter_end = ArrayAdapter.createFromResource(this,
                R.array.timevalue, android.R.layout.simple_spinner_item);
        class_end.setAdapter(adapter_end);
    }

    private final View.OnClickListener mActionBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onActionBarItemSelected(v.getId());
        }
    };
    private boolean onActionBarItemSelected(int itemId) {
        switch (itemId) {
            case R.id.action_done:
                //save();
                //TODO: implement save() function
                break;
            case R.id.action_cancel:
                System.err.println("cancel");
                this.onBackPressed();
                break;
        }
        return true;
    }
}