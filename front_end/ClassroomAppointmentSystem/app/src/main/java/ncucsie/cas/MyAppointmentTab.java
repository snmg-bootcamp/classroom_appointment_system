package ncucsie.cas;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyAppointmentTab extends Fragment implements NotifyMyAppointment {
    public MyAppointmentTab() {
    }

    public void NotifyViewListener(JSONObject result){
        try {
            if (result.getInt("status_code") == 200) {
                JSONArray table = result.getJSONArray("response");
                /*if(getActivity() != null && getActivity().findViewById(R.id.my_appointment_tab) != null) {

                }
                */
                Log.i("NotifyMyAppointment Response: ", table.toString());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_appointment_tab, container, false);

        setRetainInstance(true);
        MainActivityDrawer.NotifyClass2.mNotifyView2 = this;


        return rootView;
    }


    public static Fragment newInstance() {
        return new MyAppointmentTab();
    }
}
