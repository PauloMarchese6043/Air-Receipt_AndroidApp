package airreceipt.com.air_reeipt;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

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
    static int clearCacheFolder(final File dir, final int numDays) {

        int deletedFiles = 0;
        if (dir!= null && dir.isDirectory()) {
            try {
                for (File child:dir.listFiles()) {

                    //first delete subdirectories recursively
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }

                    //then delete the files and subdirectories in this dir
                    //only empty directories can be deleted, so subdirs have been done first
                    if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            }
            catch(Exception e) {
                Log.e("ATTENTION!", String.format("Failed to clean the cache, error %s", e.getMessage()));
            }
        }
        return deletedFiles;
    }

    public static void clearCache(final Context context, final int numDays) {
        Log.i("ADVL", String.format("Starting cache prune, deleting files older than %d days", numDays));
        int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);
        Log.i("ADVL", String.format("Cache pruning completed, %d files deleted", numDeletedFiles));
    }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id==R.id.action_settings) {
//                FirebaseAuth.getInstance().signOut();
                GoogleSignInClient mGoogleSignInClient ;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(MainActivity.this,
                        new OnCompleteListener<Void>() {  //signout Google
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseAuth.getInstance().signOut(); //signout firebase
                                Intent setupIntent = new Intent(getBaseContext(), GoogleLogin.class);
                                Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                                setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(setupIntent);
                                finish();
                            }
                        });
                return true;
            }
                return super.onOptionsItemSelected(item);

        }


    }


