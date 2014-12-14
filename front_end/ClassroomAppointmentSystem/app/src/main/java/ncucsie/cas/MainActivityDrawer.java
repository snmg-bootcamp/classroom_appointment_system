package ncucsie.cas;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivityDrawer extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, InternetComm.ApiResponse {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public static String sessionid;
    private InternetComm.ApiRequest mLogoutTask = null;
    private InternetComm.ApiRequest mRefreshTask = null;
    private InternetComm.ApiRequest mRefreshTask2 = null;

    public MainActivityDrawer drawerActivity = this;

    public class ExistingAppointmentTabRequestClass{
        public void refresh(){
            actionRefreshAppointment();
        }
    }
    static public class NotifyClass {
        static public NotifyViewAppointment mNotifyView = null;
        public void doNotify(JSONObject result){
            if(mNotifyView != null){
                mNotifyView.NotifyViewListener(result);
            }
        }
    }

    static public class NotifyClass2 {
        static public NotifyMyAppointment mNotifyView2 = null;
        public void doNotify(JSONObject result){
            if(mNotifyView2 != null){
                mNotifyView2.NotifyViewListener(result);
            }
        }
    }


    public void postProcessing(JSONObject result){
        if(mLogoutTask != null) {
            try {
                finish();
                LoginActivity.mNotifyView.setText(result.getString("response"));
            } catch (JSONException e) {
                Log.i("JSON Exception", "Failed to parse malformed response" + result.toString());
            }
            mLogoutTask = null;
        }
        if(mRefreshTask != null){
            Log.i("result: ", result.toString());
            NotifyClass mNotify = new NotifyClass();
            mNotify.doNotify(result);
            mRefreshTask = null;

        }
        if(mRefreshTask2 != null){
            Log.i("result: ", result.toString());
            NotifyClass2 mNotify = new NotifyClass2();
            mNotify.doNotify(result);
            mRefreshTask2 = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_drawer);
        Intent intent = getIntent();
        sessionid = intent.getStringExtra(Constant.USER_EXTRA);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        ExistingAppointmentTab.RefreshClass.mRequest = this;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment;
        Bundle args = new Bundle();

        switch (position + 1) {
            case 1:
                fragment = ExistingAppointmentTab.newInstance();
                break;
            case 2:
                fragment = MyAppointmentTab.newInstance();
                break;
            case 3:
                fragment = AppPreferenceTab.newInstance();
                break;
            default:
                return;
        }
        args.putInt("section_number", position + 1);
        fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_activity_drawer, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }



    private void actionAddAppointment(){
        Intent intent = new Intent(this, NewAppointmentActivity.class);
        startActivity(intent);
    }

    public void actionRefreshAppointment(){
        InternetComm comm = new InternetComm(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, String> info = new HashMap<String, String>();
        info.put("client_ver", Constant.CLIENT_VER);
        info.put("sessionid", MainActivityDrawer.sessionid);
        info.put("classroom", sharedPref.getString("classroom", "A203"));
        String date = "";
        String temp_month = sharedPref.getString("date_month", "1");
        String temp_day = sharedPref.getString("date_day", "1");
        date += sharedPref.getString("date_year", "2013");
        if(Integer.parseInt(temp_month) < 10){
            date += "0" + temp_month;
        }
        else {
            date += temp_month;
        }
        if(Integer.parseInt(temp_day) < 10){
            date += "0" + temp_day;
        }
        else {
            date += temp_day;
        }
        Log.i("Date: ", date);
        info.put("appointment-date", date);
        info.put("last-modified", "0");
        JSONObject data = new JSONObject(info);

        InternetComm.urlWithJSON result = comm.createURLRequest(Constant.VIEW_APPOINTMENT, data);

        mRefreshTask = new InternetComm.ApiRequest();
        mRefreshTask.delegate = this;
        mRefreshTask.execute(result);



        info = new HashMap<String, String>();
        info.put("client_ver", Constant.CLIENT_VER);
        info.put("sessionid", MainActivityDrawer.sessionid);
        info.put("last-modified", "");

        data = new JSONObject(info);
        result = comm.createURLRequest(Constant.MY_APPOINTMENT, data);

        mRefreshTask2 = new InternetComm.ApiRequest();
        mRefreshTask2.delegate = this;
        mRefreshTask2.execute(result);



    }

    private void actionLogout(){
        Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        InternetComm comm = new InternetComm(this);
        Map info = new HashMap<String, String>();
        info.put("client_ver", Constant.CLIENT_VER);
        info.put("sessionid", sessionid);
        JSONObject data = new JSONObject(info);

        InternetComm.urlWithJSON result = comm.createURLRequest(Constant.LOGOUT, data);
        mLogoutTask = new InternetComm.ApiRequest();
        mLogoutTask.delegate = this;
        mLogoutTask.execute(result);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_appointment) {
            actionAddAppointment();
            return true;
        }

        if(id == R.id.refresh_appointment){
            Toast.makeText(getApplicationContext(), "Refreshing data", Toast.LENGTH_SHORT).show();
            actionRefreshAppointment();
            return true;
        }

        if(id == R.id.action_logout){
            actionLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_activity_drawer, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivityDrawer) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
