package ncucsie.cas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NewAppointmentTab extends Fragment {
    public NewAppointmentTab(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,//get bundle data here from placeholderfragment, the default of int is zero
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_appointment_tab, container, false);

        setRetainInstance(true);
        return rootView;
    }


    public static Fragment newInstance() {
        return new NewAppointmentTab();
    }
}
