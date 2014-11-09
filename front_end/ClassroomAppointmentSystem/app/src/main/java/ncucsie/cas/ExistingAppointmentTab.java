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

import org.json.JSONArray;
import org.json.JSONException;


public class ExistingAppointmentTab extends Fragment {

    public ExistingAppointmentTab() {
    }

    public interface ActivityInterface {
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
        set_existing_table((TableLayout) rootView.findViewById(R.id.existing_appointment_tab));//why getviewbyid is null?


        return rootView;
    }

    private void set_existing_table(TableLayout tableLayout) {
        try {
            JSONArray array = new JSONArray("[[\"\",\"Sun日(11.2)\",\"Mon一(11.3)\",\"Tue二(11.4)\",\"Wed三(11.5)\",\"Thu四(11.6)\",\"Fri五(11.7)\",\"Sat六(11.8)\"],\n" +
                    "[\"108:00-08:50\",\"\",\"\",\"\",\"\",\"\",\"陳日憲\",\"\"],\n" +
                    "[\"209:00-09:50\",\"\",\"\",\"\",\"\",\"\",\"陳日憲\",\"\"],\n" +
                    "[\"310:00-10:50\",\"\",\"\",\"3A演算法實習課\",\"線性代數-2A曾定章\",\"影像處理曾定章\",\"陳日憲\",\"\"],\n" +
                    "[\"411:00-11:50\",\"\",\"\",\"3A演算法實習課\",\"線性代數-2A曾定章\",\"影像處理曾定章\",\"陳日憲\",\"\"],\n" +
                    "[\"Z12:00-12:50\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],\n" +
                    "[\"513:00-13:50\",\"\",\"影像處理曾定章\",\"\",\"演算法-3A何錦文\",\"演算法-3A何錦文\",\"林鼎國\",\"\"],\n" +
                    "[\"614:00-14:50\",\"\",\"線性代數-2A曾定章\",\"機器學習栗永徽\",\"\",\"演算法-3A何錦文\",\"林鼎國\",\"\"],\n" +
                    "[\"715:00-15:50\",\"\",\"計算機網路-3B曾黎明\",\"機器學習栗永徽\",\"\",\"離散數學-2B孫敏德\",\"林鼎國\",\"\"],\n" +
                    "[\"816:00-16:50\",\"\",\"計算機網路-3B曾黎明\",\"機器學習栗永徽\",\"\",\"離散數學-2B孫敏德\",\"林鼎國\",\"\"],\n" +
                    "[\"917:00-17:50\",\"\",\"計算機網路-3B曾黎明\",\"\",\"劉于碩\",\"離散數學-2B孫敏德\",\"\",\"\"],\n" +
                    "[\"A18:00-18:50\",\"\",\"\",\"\",\"劉于碩\",\"\",\"\",\"\"],\n" +
                    "[\"B19:00-19:50\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],\n" +
                    "[\"C20:00-20:50\",\"\",\"\",\"蘇俊儒\",\"陳姿妤\",\"\",\"\",\"\"],\n" +
                    "[\"D21:00-21:50\",\"\",\"\",\"蘇俊儒\",\"陳姿妤\",\"\",\"\",\"\"]]");
        }
        catch (JSONException exception){
            
        }
        for(int i=0;i<7;i++){
            TableRow row = new TableRow(getActivity());
            TextView text = new TextView(getActivity());
            text.setText("hello");
            System.out.println(row);
            System.out.println(tableLayout);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            row.addView(text);
            tableLayout.addView(row,i);

        }
    }

    public static Fragment newInstance() {
        return new ExistingAppointmentTab();
    }
}
