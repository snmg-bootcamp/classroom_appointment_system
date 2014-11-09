package ncucsie.cas;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ExistingAppointmentTab extends Fragment {

    public ExistingAppointmentTab() {
    }

    public interface ActivityInterface {
        public View getViewById (int ResId);
        public Context getActivityContext();
    }

    ActivityInterface mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ActivityInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.existing_appointment_tab, container, false);
        setRetainInstance(true);

        set_existing_table((TableLayout) mCallback.getViewById(R.id.existing_appointment_tab));


        return rootView;
    }

    private void set_existing_table(TableLayout tableLayout) {
        for(int i=0;i<7;i++){
            TableRow row = new TableRow(mCallback.getActivityContext());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            tableLayout.addView(row,i);

        }
    }

    public static Fragment newInstance() {
        return new ExistingAppointmentTab();
    }
}
