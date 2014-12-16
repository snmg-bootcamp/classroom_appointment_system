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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ExistingAppointmentTab extends Fragment implements NotifyViewAppointment {

    public ExistingAppointmentTab() {
    }


    public void NotifyViewListener(JSONObject result) {
        try {
            if (result.getInt("status_code") == 200) {
                JSONArray table = result.getJSONArray("response");
                if (getActivity() != null && getActivity().findViewById(R.id.existing_appointment_tab) != null) {
                    SetExistingTable((TableLayout) getActivity().findViewById(R.id.existing_appointment_tab), table);
                }
            } else {
                Toast.makeText(getActivity(), "Failed to refresh appointment" + result.getString("response"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.d("Malformed response from server", result.toString());
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
        RefreshClass request = new RefreshClass();
        request.refresh();
        super.onResume();
    }


    static public class RefreshClass {
        static public MainActivityDrawer mRequest = null;

        RefreshClass() {
        }

        public void refresh() {
            if (mRequest != null) {
                mRequest.actionRefreshAppointment();
            }
        }

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
        MainActivityDrawer.NotifyRefreshExistingAppointmentClass.mNotifyView = this;


        return rootView;
    }


    private void SetExistingTable(TableLayout tableLayout, JSONArray array) {
        tableLayout.removeAllViews();
        tableLayout.setStretchAllColumns(true);
        String[] timeArray = getResources().getStringArray(R.array.timevalue);
        System.out.println(Arrays.toString(timeArray));
        try {
            for (int i = 0; i < array.length(); i++) {
                TableRow row = new TableRow(getActivity());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);
                tableLayout.addView(row, i);
                TextView text = new TextView(getActivity());
                try {
                    if (i == 0) {
                        text.setText("");
                        text.setBackgroundResource(R.drawable.cell_shape);
                        text.setPadding(16, 4, 12, 4);
                    } else {
                        text.setText(timeArray[i - 1]);
                        text.setBackgroundResource(R.drawable.cell_shape);
                        text.setPadding(16, 4, 12, 4);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.d("ArrayIndexOutOfBoundsException", e.toString());
                }

                TextView text2 = new TextView(getActivity());
                text2.setText(array.get(i).toString());
                text2.setBackgroundResource(R.drawable.cell_shape);
                text2.setPadding(16, 4, 12, 4);

                row.addView(text);
                row.addView(text2);
            }
        } catch (JSONException exception) {
            Log.d("JSON Exception", "Failed to parse JSON array: " + array.toString());
        }

    }

    public static Fragment newInstance() {
        return new ExistingAppointmentTab();
    }


}
