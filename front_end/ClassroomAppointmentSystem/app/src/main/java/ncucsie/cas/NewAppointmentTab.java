package ncucsie.cas;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class NewAppointmentTab extends Fragment {
    public NewAppointmentTab() {
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
        View rootView = inflater.inflate(R.layout.new_appointment_tab, container, false);
        View actionBarButtons = inflater.inflate(R.layout.custom_form_custom_actionbar,
                new LinearLayout(getActivity()), false);
        View cancelActionView = actionBarButtons.findViewById(R.id.action_cancel);
        //cancelActionView.setOnClickListener(mActionBarListener);
        View doneActionView = actionBarButtons.findViewById(R.id.action_done);
        //doneActionView.setOnClickListener(mActionBarListener);
        getActivity().getActionBar().setCustomView(actionBarButtons);
        setRetainInstance(true);
        return rootView;
    }


    public static Fragment newInstance() {
        return new NewAppointmentTab();
    }
}
