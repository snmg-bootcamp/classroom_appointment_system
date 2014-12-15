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
    View cancelActionView;


    public void postProcessing(boolean has_data, JSONObject result){
        if(!has_data){
            Toast.makeText(this, "Received no response from server, try again later", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            if(result != null) {
                int status = result.getInt("status_code");
                switch (status) {
                    case 200:
                        this.onBackPressed();
                        break;
                    default:
                        Toast.makeText(this, "Failed to create appointment, " + result.getString("response"), Toast.LENGTH_LONG).show();
                        break;

                }
            }
            else{
                Toast.makeText(this, "Connection to server failed, no response from server", Toast.LENGTH_LONG).show();
            }
        }
        catch (JSONException e){
            Log.d("JSON Exception", "Malformed response" + result.toString());
        }
        notFinished = false;
        cancelActionView.setEnabled(true);
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
        cancelActionView = actionBarButtons.findViewById(R.id.action_cancel);
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
                cancelActionView.setEnabled(false);
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
        info.put("classroom", ((Spinner) findViewById(R.id.spinner_classroom)).getSelectedItem().toString());
        info.put("month", ((Spinner)findViewById(R.id.spinner_month)).getSelectedItem().toString());
        info.put("day", ((Spinner)findViewById(R.id.spinner_day)).getSelectedItem().toString());
        info.put("year", ((Spinner)findViewById(R.id.spinner_year)).getSelectedItem().toString());
        info.put("start_period", ((Spinner)findViewById(R.id.spinner_class_start)).getSelectedItem().toString());
        info.put("end_period", ((Spinner)findViewById(R.id.spinner_class_end)).getSelectedItem().toString());
        info.put("note", ((EditText)findViewById(R.id.appointment_comment)).getText().toString());

        JSONObject data = new JSONObject(info);

        InternetComm.urlWithJSON result = comm.createURLRequest(Constant.ADD_APPOINTMENT, data);

        mNewAppointmentRequest = new InternetComm.ApiRequest(Constant.NEW_APPOINTMENT_REQUEST);
        mNewAppointmentRequest.delegate = this;
        mNewAppointmentRequest.execute(result);
    }
}