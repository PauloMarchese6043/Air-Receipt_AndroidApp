package airreceipt.com.air_reeipt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating the menu and Search--->NOT DONE

//        @Override
//        public boolean onCreateOptionMenu(Menu menu) {
//            getMenuInflater().inflate(R.menu.menu, menu);
//
//            MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
//            SearchView searchView = (SearchView)myActionMenuItem.getActionView();
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//                @Override
//                public boolean OnQueryTextSubmit(String s) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String s) {
//                    if (TextUtils.isEmpty(s)) {
//                        adapter.filter(charText: "");
//                        listView.clearTextFilter();
//                    }
//                    else {
//                        adapter.filter(s);
//                    }
//                    return true;
//                }
//            });
//            return true;
//        }
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id = item.getItemId();
//
//            if (id==R.id.action_settings) {
//
//
//                return true;
//            }
//                return super.onOptionsItemSelected(item);
//
//        }
//

    }
}
