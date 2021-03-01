package kz.xyzdev.mebel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import kz.xyzdev.mebel.Models.Adapter;
import kz.xyzdev.mebel.Models.ProductModels;
import kz.xyzdev.mebel.ui.dashboard.DashboardFragment;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    Adapter myadapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Intent intent = getIntent();
        String DBName = intent.getStringExtra(DashboardFragment.EXTRA_DBName);


        recyclerView = (RecyclerView) findViewById(R.id.recItems);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        Query query = FirebaseDatabase.getInstance().getReference()
                .child("HomeDB")
                .orderByChild("catalog")
                .startAt(DBName)
                .endAt(DBName);

        FirebaseRecyclerOptions<ProductModels> options =
                new FirebaseRecyclerOptions.Builder<ProductModels>()
                        .setQuery(query, ProductModels.class)
                        .build();

        myadapter = new Adapter(options,this);


        recyclerView.setAdapter(myadapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        myadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myadapter.stopListening();
    }
}