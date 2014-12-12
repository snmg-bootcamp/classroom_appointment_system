package ncucsie.cas;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

public class MyAppointmentTab extends Fragment implements InternetComm.ApiResponse {
    public MyAppointmentTab() {
    }
    private InternetComm.ApiRequest mNewAppointmentRequest = null;
    public void postProcessing(JSONObject result){

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
        return rootView;
    }


    public static Fragment newInstance() {
        return new MyAppointmentTab();
    }
}
