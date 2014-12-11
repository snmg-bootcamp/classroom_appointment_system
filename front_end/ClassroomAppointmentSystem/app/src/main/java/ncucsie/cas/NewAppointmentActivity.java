package ncucsie.cas;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NewAppointmentActivity extends Activity
                implements InternetComm.ApiResponse{
    public NewAppointmentActivity() {
    }

    boolean notFinished = false;
    private InternetComm.ApiRequest mNewAppointmentRequest = null;


    public void postProcessing(JSONObject result){
        try {
            int status = result.getInt("status_code");
            switch(status){
                case 200:
                    this.onBackPressed();
                    break;
                default:
                    Toast.makeText(this, "Failed to create appointent", Toast.LENGTH_LONG);
                    break;

            }
        }
        catch (JSONException e){
            Log.i("JSON Exception", "Malformed response" + result.toString());
        }
        notFinished = false;
    }


    @Override
    public void onBackPressed() {
        if(!notFinished){
            super.onBackPressed();
        }
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
                notFinished = true;
                sendNewAppointment();
                break;
            case R.id.action_cancel:
                System.err.println("cancel");
                this.onBackPressed();
                break;
        }
        return true;
    }

    private void sendNewAppointment() {
        InternetComm comm = new InternetComm(this);
        Map<String, String> info = new HashMap<String, String>();
        info.put("client_ver", Constant.CLIENT_VER);
        info.put("sessionid", MainActivityDrawer.sessionid);
        info.put("name", ((EditText)findViewById(R.id.name)).getText().toString());
        info.put("phone", ((EditText)findViewById(R.id.phone)).getText().toString());
        info.put("teacher", ((EditText)findViewById(R.id.teacher)).getText().toString());
        info.put("classroom", ((EditText)findViewById(R.id.classroom_text)).getText().toString());
        info.put("month", ((EditText)findViewById(R.id.spinner_month)).getText().toString());
        info.put("day", ((EditText)findViewById(R.id.spinner_day)).getText().toString());
        info.put("year", ((EditText)findViewById(R.id.spinner_year)).getText().toString());
        info.put("start_period", ((EditText)findViewById(R.id.spinner_class_start)).getText().toString());
        info.put("end_period", ((EditText)findViewById(R.id.spinner_class_end)).getText().toString());
        info.put("note", ((EditText)findViewById(R.id.appointment_comment)).getText().toString());

        JSONObject data = new JSONObject(info);

        InternetComm.urlWithJSON result = comm.createURLRequest(Constant.ADD_APPOINTMENT, data);

        mNewAppointmentRequest = new InternetComm.ApiRequest();
        mNewAppointmentRequest.delegate = this;
        mNewAppointmentRequest.execute(result);
    }
}