package ncucsie.cas;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


public class NewAppointmentActivity extends Activity {
    public NewAppointmentActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_appointment_tab);
        View actionBarButtons = getLayoutInflater().inflate(R.layout.custom_form_custom_actionbar,
                new LinearLayout(this), false);
        View cancelActionView = actionBarButtons.findViewById(R.id.action_cancel);
        //cancelActionView.setOnClickListener(mActionBarListener);
        View doneActionView = actionBarButtons.findViewById(R.id.action_done);
        //doneActionView.setOnClickListener(mActionBarListener);
        getActionBar().setCustomView(actionBarButtons);
    }


}