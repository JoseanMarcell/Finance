package senac.finance;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import senac.finance.adapters.FinanceAdapter;
import senac.finance.models.Finance;
import senac.finance.models.FinanceDB;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Finance>> {

    public static FinanceDB financeDB;
    RecyclerView recyclerView;
    ProgressBar loading;
    LoaderManager loaderManager;
    static List<Finance> financeList = new ArrayList<>();

    public static final int OPERATION_SEARCH_LOADER = 15;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: Step 4 of 4: Finally call getTag() on the view.
            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            // viewHolder.getItemId();
            // viewHolder.getItemViewType();
            // viewHolder.itemView;
            Finance thisItem = financeList.get(position);
            Toast.makeText(MainActivity.this, "You Clicked: " + thisItem.getId(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (financeDB == null) {
            financeDB = new FinanceDB(this);
        }

        loaderManager = getSupportLoaderManager();

        loading = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.listFinances);

        /*
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(layout);
        */

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerView.setOnClickListener(onItemClickListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent novaFinance = new Intent(getBaseContext(), FinanceActivity.class);
                startActivity(novaFinance);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Loader<List<Finance>> loader = loaderManager.getLoader(OPERATION_SEARCH_LOADER);

        if (loader == null) {
            loaderManager.initLoader(OPERATION_SEARCH_LOADER, null, MainActivity.this);
        } else {
            loaderManager.restartLoader(OPERATION_SEARCH_LOADER, null, MainActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Finance>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Finance>>(this) {
            @Nullable
            @Override
            public List<Finance> loadInBackground() {

                if (financeList.size() > 0){
                    return financeList;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return financeDB.select();
            }

            @Override
            protected void onStartLoading() {
                loading.setVisibility(View.VISIBLE);
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Finance>> loader, List<Finance> data) {
        loading.setVisibility(View.GONE);

        financeList = new ArrayList<>(data);

        recyclerView.setAdapter(new FinanceAdapter(financeList, this));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Finance>> loader) {

    }
}
