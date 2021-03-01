package kz.xyzdev.mebel.ui.notifications;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.ar.core.Anchor;
import com.google.ar.core.CameraConfig;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hluhovskyi.camerabutton.CameraButton;

import kz.xyzdev.mebel.Models.AdapterForAR;
import kz.xyzdev.mebel.Models.ProductModels;
import kz.xyzdev.mebel.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private Button up,down;
    private LinearLayout linearLayout;

    private String modelName="https://firebasestorage.googleapis.com/v0/b/mebel-10363.appspot.com/o/commode.glb?alt=media&token=be093edc-8a9f-4bfe-b0b7-7c3105e4ca3b";
    private ModelRenderable modelRenderable;
    private ArFragment arFragment;
    private CameraButton cameraButton;

    private RecyclerView recbottom;
    AdapterForAR myadapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReference().child(modelName);

        arFragment = (ArFragment) getChildFragmentManager()
                .findFragmentById(R.id.arFragment);
        CameraConfig cameraConfig;


        linearLayout = (LinearLayout) root.findViewById(R.id.bottomsheet);
        // Bottom sheet close\open
        up = (Button) root.findViewById(R.id.up);
        down = (Button) root.findViewById(R.id.down);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                up.setVisibility(View.INVISIBLE);
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.INVISIBLE);
                up.setVisibility(View.VISIBLE);
            }
        });

        //recycler helper
        recbottom = (RecyclerView) root.findViewById(R.id.recbottom);
        recbottom.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("HomeDB");
        FirebaseRecyclerOptions<ProductModels> options =
                new FirebaseRecyclerOptions.Builder<ProductModels>()
                        .setQuery(query, ProductModels.class)
                        .build();
        myadapter = new AdapterForAR(options);
        recbottom.setAdapter(myadapter);

        myadapter.setOnItemClickListener(new AdapterForAR.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot documentSnapshot, int position) {
                String string =documentSnapshot.child("model").getValue().toString();
                modelName = string;
                ModelRenderable.builder()
                        .setSource(getContext(), RenderableSource.builder().setSource(getContext(),
                                Uri.parse(modelName),RenderableSource.SourceType.GLB)
                                .setScale(1f)
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                .build())
                        .setRegistryId(modelName)
                        .build()
                        .thenAccept(renderable -> modelRenderable = renderable)
                        .exceptionally(throwable -> {
                            Toast.makeText(getContext(), "Can't load the model", Toast.LENGTH_SHORT).show();
                            return null;
                        });
            }
        });

        //ar helper
        //setUpModel();
        setUpPlane();





        return root;

    }

    private void setUpPlane() {
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        }));
    }

    private void createModel(AnchorNode anchorNode){
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.getScaleController().setMaxScale(1f);
        transformableNode.setRenderable(modelRenderable);
        transformableNode.select();
    }

    //private void setUpModel() {}

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