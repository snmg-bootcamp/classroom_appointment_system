package ncucsie.cas;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class ExistingAppointmentTab extends Fragment implements MainActivityDrawer.NotifyViewAppointment{

    public ExistingAppointmentTab() {
    }

    public void NotifyViewListener(JSONObject result){
        try {
            if (result.getInt("status_code") == 200) {
                JSONArray table = result.getJSONArray("response");
                SetExistingTable((TableLayout) getActivity().findViewById(R.id.existing_appointment_tab), table);

            }
        }
        catch(JSONException e){
            Log.i("Malformed response from server", result.toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivityDrawer) activity).onSectionAttached(
                getArguments().getInt("section_number"));
    }

    @Override
    public void onResume() {
        TextView classroom_text = (TextView) getActivity().findViewById(R.id.classroom_selection_value);
        TextView date_text = (TextView) getActivity().findViewById(R.id.date_selection_value);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        classroom_text.setText(sharedPref.getString("classroom", "A203"));
        date_text.setText(sharedPref.getString("date_year", "2013") + "-" + sharedPref.getString("date_month", "1") + "-" + sharedPref.getString("date_day", "1"));
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            System.out.println("Executing onCreateView in ExistingAppointmentTab.java");
        }
        final View rootView = inflater.inflate(R.layout.existing_appointment_tab, container, false);
        setRetainInstance(true);
        TextView classroom_text = (TextView) rootView.findViewById(R.id.classroom_selection_value);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        classroom_text.setText(sharedPref.getString("classroom", "A203"));

        MainActivityDrawer drawer = new MainActivityDrawer();
        drawer.actionRefreshAppointment();

        return rootView;
    }



    private void SetExistingTable(TableLayout tableLayout, JSONArray array) {
        tableLayout.removeAllViews();
        tableLayout.setStretchAllColumns(true);
        try {
            for (int i = 0; i < array.length(); i++) {
                TableRow row = new TableRow(getActivity());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);
                tableLayout.addView(row, i);
                for (int j = 0; j < array.getJSONArray(i).length(); j++) {
                    TextView text = new TextView(getActivity());
                    text.setText(array.getJSONArray(i).getString(j));
                    text.setBackgroundResource(R.drawable.cell_shape);
                    text.setPadding(16, 4, 12, 4);

                    row.addView(text);

                }

            }
        } catch (JSONException exception) {
            Log.i("JSON Exception", "Failed to parse JSON array");
        }

    }

    public static Fragment newInstance() {
        return new ExistingAppointmentTab();
    }


}
