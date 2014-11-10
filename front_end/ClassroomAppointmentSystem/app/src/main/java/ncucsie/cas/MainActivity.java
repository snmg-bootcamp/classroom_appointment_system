package ncucsie.cas;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //    if (savedInstanceState == null) {
    //        getFragmentManager().beginTransaction()
    //                .add(R.id.realtabcontent, new PlaceholderFragment())
    //                .commit();
    //    }
        Intent intent = new Intent(this, MainActivityDrawer.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);
    }
    @Override
    protected void onResume(){
        super.onResume();
        this.finish();
    }
}
