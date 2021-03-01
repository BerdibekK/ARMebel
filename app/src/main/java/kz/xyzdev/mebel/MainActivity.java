package kz.xyzdev.mebel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import kz.xyzdev.mebel.PackageAR.ARActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name,price,features,color,material,country,catalog;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("GGG", getIntent().getStringExtra("name"));

        imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(getIntent().getStringExtra("imgurl")).into(imageView);

        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

        price = (TextView) findViewById(R.id.price);
        price.setText(getIntent().getStringExtra("price"));

        color = (TextView) findViewById(R.id.color);
        color.setText(getIntent().getStringExtra("color"));

        features = (TextView) findViewById(R.id.features);
        features.setText(getIntent().getStringExtra("features"));

        material = (TextView) findViewById(R.id.material);
        material.setText(getIntent().getStringExtra("material"));

        country = (TextView) findViewById(R.id.country);
        country.setText(getIntent().getStringExtra("country"));

        catalog = (TextView) findViewById(R.id.catalog);
        catalog.setText(getIntent().getStringExtra("catalog")+"room");

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ARActivity.class);
                intent.putExtra("modelName",getIntent().getStringExtra("model"));
                startActivity(intent);
            }
        });

    }
}
