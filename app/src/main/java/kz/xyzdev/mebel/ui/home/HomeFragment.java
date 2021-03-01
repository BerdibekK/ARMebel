package kz.xyzdev.mebel.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Priority;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import kotlin.Deprecated;
import kz.xyzdev.mebel.R;
import kz.xyzdev.mebel.Models.Adapter;
import kz.xyzdev.mebel.Models.ProductModels;
import kz.xyzdev.mebel.ui.home.SliderAdapter.SliderAdapter;
import kz.xyzdev.mebel.ui.home.SliderAdapter.SliderItem;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //reccycler views
    private RecyclerView recyclerViewHome;

    private  Adapter myadapter;


    private SliderView sliderView;

    private Query query;

    private EditText search;
    private ImageButton searchBtn;
    private String searchText;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //SliderView
        sliderView = (SliderView) root.findViewById(R.id.imageSlider);
        SliderAdapter adapter = new SliderAdapter(container.getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.rgb(255, 165, 0));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }


        });

        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i % 2 == 0) {
                sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
            } else {
                sliderItem.setImageUrl("https://firebasestorage.googleapis.com/v0/b/mebel-10363.appspot.com/o/scrnli_10_6_2020_6-50-52%20PM.png?alt=media&token=ac4db614-e80f-4191-9c32-a2609f7d7311");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);

        recyclerViewHome = (RecyclerView) root.findViewById(R.id.recviewItemsHome);
        recyclerViewHome.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));


        query = FirebaseDatabase.getInstance().getReference().child("HomeDB");

        /*search = (EditText) root.findViewById(R.id.search);
        searchBtn = (ImageButton) root.findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = search.getText().toString();
                searchFirebase();
            }
        });*/


        FirebaseRecyclerOptions<ProductModels>  options =
                new FirebaseRecyclerOptions.Builder<ProductModels>()
                        .setQuery(query,ProductModels.class)
                        .build();

        myadapter = new Adapter(options,getContext());


        recyclerViewHome.setAdapter(myadapter);

        return root;

    }


    /*private void searchFirebase() {
        Query nquery = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("HomeDB")
                .orderByChild("name")
                .startAt(searchText)
                .endAt(searchText+"\uf8ff");

        FirebaseRecyclerOptions<ProductModels>  options =
                new FirebaseRecyclerOptions.Builder<ProductModels>()
                        .setQuery(nquery,ProductModels.class)
                        .build();

        myadapter.updateOptions(options);
    }*/

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

