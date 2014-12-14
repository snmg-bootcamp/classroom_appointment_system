package ncucsie.cas;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAppointmentTab extends Fragment implements NotifyMyAppointment {
    public MyAppointmentTab() {
    }

    public void NotifyViewListener(JSONObject result){
        try {
            if (result.getInt("status_code") == 200) {
                JSONArray table = result.getJSONArray("response");
                if(getActivity() != null && getActivity().findViewById(R.id.my_appointment_list) != null) {

                }

                Log.d("NotifyMyAppointment Response: ", table.toString());
            }
        }
        catch(JSONException e){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_appointment_tab, container, false);

        setRetainInstance(true);
        MainActivityDrawer.NotifyClass2.mNotifyView2 = this;


        ArrayList image_details = getListData();
        final ListView list = (ListView) getActivity().findViewById(R.id.my_appointment_list);
        list.setAdapter(new CustomListAdapter(this.getActivity(), image_details));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = list.getItemAtPosition(position);
                myAppointmentClass item = (myAppointmentClass) o;
            }

        });



        return rootView;
    }


    private ArrayList getListData(JSONArray array) {
        ArrayList<myAppointmentClass> results = new ArrayList<myAppointmentClass>();
        for(int i=0;i<array.length();i++) {
            myAppointmentClass appointment = new myAppointmentClass();

            results.add(appointment);
        }


        return results;
    }


    public static Fragment newInstance() {
        return new MyAppointmentTab();
    }
}
