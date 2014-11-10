package ncucsie.cas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
