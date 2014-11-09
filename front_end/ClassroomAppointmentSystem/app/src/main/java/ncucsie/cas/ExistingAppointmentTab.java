package ncucsie.cas;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ExistingAppointmentTab extends Fragment {

    public ExistingAppointmentTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.existing_appointment_tab, container, false);

        setRetainInstance(true);
        return rootView;
    }

    public static Fragment newInstance() {
        return new ExistingAppointmentTab();
    }
}
