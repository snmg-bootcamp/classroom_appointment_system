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
                    ArrayList lists = getListData(table);
                    final ListView list = (ListView) getActivity().findViewById(R.id.my_appointment_list);
                    list.setAdapter(new CustomListAdapter(this.getActivity(), lists));
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                            Object o = list.getItemAtPosition(position);
                            myAppointmentClass item = (myAppointmentClass) o;
                        }

                    });
                }

                Log.d("NotifyMyAppointment Response: ", table.toString());
            }
        }
        catch(JSONException e){
            Log.d("Malformed response from server", result.toString());
        }
    }
    static public class RefreshClass{
        static public MainActivityDrawer mRequest = null;
        RefreshClass (){
        }
        public void refresh(){
            if(mRequest != null){
                mRequest.actionRefreshAppointment();
            }
        }

    }

    @Override
    public void onResume() {
        RefreshClass request = new RefreshClass();
        request.refresh();
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


    private ArrayList getListData(JSONArray array) {
        ArrayList<myAppointmentClass> results = new ArrayList<myAppointmentClass>();
        try {
            for (int i = 0; i < array.length(); i++) {
                myAppointmentClass appointment = new myAppointmentClass();
                appointment.setNum(Integer.parseInt(array.getJSONArray(i).get(0).toString()));
                appointment.setDate(array.getJSONArray(i).get(1).toString());
                appointment.setClassroom(array.getJSONArray(i).get(2).toString());
                appointment.setTime(array.getJSONArray(i).get(3).toString());
                appointment.setName(array.getJSONArray(i).get(4).toString());
                appointment.setTeacher(array.getJSONArray(i).get(5).toString());
                appointment.setHiddenNum(array.getJSONArray(i).get(6).toString());
                appointment.setData(array);
                results.add(appointment);
            }
        }
        catch (JSONException e){
            Log.d("JSONException: ", e.toString());
        }


        return results;
    }


    public static Fragment newInstance() {
        return new MyAppointmentTab();
    }
}
