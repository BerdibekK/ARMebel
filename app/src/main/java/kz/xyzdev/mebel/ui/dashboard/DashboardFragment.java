package kz.xyzdev.mebel.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.ar.sceneform.ux.ArFragment;

import kz.xyzdev.mebel.CatalogActivity;
import kz.xyzdev.mebel.PackageAR.ARActivity;
import kz.xyzdev.mebel.R;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private DashboardViewModel dashboardViewModel;
    private CardView bathBtn,bedBtn,kitchenBtn,livingBtn,studyBtnl;

    public static final String EXTRA_DBName = "null";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        bathBtn = (CardView) root.findViewById(R.id.bathtoomBtn);
        bedBtn = (CardView) root.findViewById(R.id.bedroomBtn);
        kitchenBtn = (CardView) root.findViewById(R.id.kitchenBtn);
        livingBtn = (CardView) root.findViewById(R.id.livingBtn);
        studyBtnl = (CardView) root.findViewById(R.id.studuBtn);

        bathBtn.setOnClickListener(this);
        bedBtn.setOnClickListener(this);
        kitchenBtn.setOnClickListener(this);
        livingBtn.setOnClickListener(this);
        studyBtnl.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        // TODO Hello World!)
        //Share database name and start child activity
        Intent intent;
        String string;
        switch (v.getId()){
            case R.id.bathtoomBtn:
                string = "Bath";
                intent = new Intent(getContext(), CatalogActivity.class);
                intent.putExtra(EXTRA_DBName, string);
                startActivity(intent);break;
            case R.id.bedroomBtn:
                string = "Bed";
                intent = new Intent(getContext(), CatalogActivity.class);
                intent.putExtra(EXTRA_DBName, string);
                startActivity(intent);  break;
            case R.id.kitchenBtn:
                string = "Kitchen";
                intent = new Intent(getContext(), CatalogActivity.class);
                intent.putExtra(EXTRA_DBName, string);
                startActivity(intent); break;
            case R.id.livingBtn:
                string = "Living";
                intent = new Intent(getContext(), CatalogActivity.class);
                intent.putExtra(EXTRA_DBName, string);
                startActivity(intent);break;
            case R.id.studuBtn:
                string = "Study";
                intent = new Intent(getContext(), CatalogActivity.class);
                intent.putExtra(EXTRA_DBName, string);
                startActivity(intent);break;
            default: break;
        }
    }
}