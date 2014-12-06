package ncucsie.cas;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyAppointmentTab extends Fragment {
    public MyAppointmentTab() {
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
