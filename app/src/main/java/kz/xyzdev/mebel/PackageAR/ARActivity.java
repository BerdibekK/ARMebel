package kz.xyzdev.mebel.PackageAR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.Anchor;
import com.google.ar.core.CameraConfig;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hluhovskyi.camerabutton.CameraButton;

import kz.xyzdev.mebel.R;
import kz.xyzdev.mebel.VideoRecorder;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;



public class ARActivity extends AppCompatActivity {
    private String prefix, suffix;
    private String modelName;
    private ModelRenderable modelRenderable;
    private ArFragment arFragment;
    private CameraButton cameraButton;
    private VideoRecorder videoRecorder;
    private File videoDirectory;
    private MediaRecorder mediaRecorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
        FirebaseApp.initializeApp(this);
        modelName = getIntent().getStringExtra("modelName");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReference().child(modelName);

        arFragment = (ArFragment) getSupportFragmentManager()
                .findFragmentById(R.id.arFragment);
        CameraConfig cameraConfig;
        





        cameraButton = (CameraButton) findViewById(R.id.camera_button);
        cameraButton.setOnPhotoEventListener(new CameraButton.OnPhotoEventListener() {
            @Override
            public void onClick() {
                takePhoto();
            }
        });

        /*cameraButton.setOnVideoEventListener(new CameraButton.OnVideoEventListener() {
            @Override
            public void onStart() {

                boolean isRecording = videoRecorder.onToggleRecord();
                if (videoRecorder == null) {
                    videoRecorder = new VideoRecorder();
                    videoRecorder.setSceneView(arFragment.getArSceneView());
                    int orientation = getResources().getConfiguration().orientation;
                    videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation);
                }


                if (isRecording) {
                    Toast.makeText(ARActivity.this, "Started Recording", Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onFinish() {
                boolean isRecording = videoRecorder.onToggleRecord();
                Toast.makeText(ARActivity.this, "Recording stoped", Toast.LENGTH_SHORT).show();
                String videoPath = videoRecorder.getVideoPath().getAbsolutePath();
                // Send  notification of updated content.
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.TITLE, "Mebel+ Video");
                values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                values.put(MediaStore.Video.Media.DATA, videoPath);
                getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

            }

            @Override
            public void onCancel() {

            }
        });*/


        setUpModel();
        setUpPlane();

    }


    private void setUpPlane() {
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        }));
    }

    private void handleOnTauch(HitTestResult hitTestResult, MotionEvent motionEvent){

    }

    private void createModel(AnchorNode anchorNode){
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.getScaleController().setMinScale(1f);
        transformableNode.getScaleController().setMaxScale(1.0001f);
        transformableNode.setRenderable(modelRenderable);
        transformableNode.select();
    }

    private void setUpModel() {
        ModelRenderable.builder()
                .setSource(this,RenderableSource.builder().setSource(this,
                        Uri.parse(modelName),RenderableSource.SourceType.GLB)
                        .setScale(1f)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build())
        .setRegistryId(modelName)
                .build()
        .thenAccept(renderable -> modelRenderable = renderable)
        .exceptionally(throwable -> {
            Toast.makeText(this, "Can't load the model", Toast.LENGTH_SHORT).show();
            return null;
        });


    }

    private void takePhoto() {
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap);
                } catch (IOException e) {
                    Toast toast = Toast.makeText(this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }





            } else {



            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }


    public void saveBitmapToDisk(Bitmap bitmap) throws IOException {

        //  String path = Environment.getExternalStorageDirectory().toString() +  "/Pictures/Screenshots/";

        if (videoDirectory == null) {
            videoDirectory =
                    new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                                    + "/Camera");
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        String formattedDate = df.format(c.getTime());

        File mediaFile = new File(videoDirectory, "FieldVisualizer"+formattedDate+".jpeg");

        FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        arFragment.onDestroyView();
    }
}
