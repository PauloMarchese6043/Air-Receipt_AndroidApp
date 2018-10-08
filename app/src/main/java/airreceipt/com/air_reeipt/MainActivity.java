package airreceipt.com.air_reeipt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ListViewAdapter adapter;
    String [] store;
    String [] price;
    int [] icon;
    ArrayList<Model> arrayList = new ArrayList<Model>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        store = new String[]{"H&M", "Zara", "TopShop", "GlueStore", "TopMan"};
        price = new String[]{"$23.95", "$129.99", "$69.00", "$55.95", "$100.00"};
        icon = new int[]{R.drawable.receipticon, R.drawable.receipticon, R.drawable.receipticon,
                R.drawable.receipticon, R.drawable.receipticon};

        listView = findViewById(R.id.listView);

        for (int i = 0; i < store.length; i++) {
            Model model = new Model(store[i], price[i], icon[i]);
            //bind all strings in an array
            arrayList.add(model);
        }

        //pass results to listViewAdapter class
        adapter = new ListViewAdapter(this, arrayList);

        //bind adapter to the listview
        listView.setAdapter(adapter);

    }

        //Creating the menu and Search--->NOT DONE


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else {
                    adapter.filter(s);
                }
                return true;
            }
        });
        return true;
    }
    public void GoToNFCActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, NfcActivity.class);
        startActivity(intent);
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id==R.id.action_settings) {


                return true;
            }
                return super.onOptionsItemSelected(item);

        }


    }


