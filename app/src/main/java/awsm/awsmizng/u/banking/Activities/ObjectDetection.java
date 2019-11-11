package awsm.awsmizng.u.banking.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.objects.FirebaseVisionObject;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.util.List;

import awsm.awsmizng.u.banking.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ObjectDetection extends AppCompatActivity {
    @BindView(R.id.tvDetectedItem)
    TextView tvDetectedItem;
    String LogTag = "ipsita";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_detection);
        ButterKnife.bind(this);
        CameraView cameraView = findViewById(R.id.cameraView);
        cameraView.setLifecycleOwner(this);
        final FirebaseVisionObjectDetectorOptions options = new FirebaseVisionObjectDetectorOptions.Builder()
                .setDetectorMode(FirebaseVisionObjectDetectorOptions.STREAM_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build();
        Log.d(LogTag, "onCreate: " + "entereing detector ");
        final FirebaseVisionObjectDetector detector = FirebaseVision.getInstance().getOnDeviceObjectDetector(options);
        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                Log.d(LogTag, "process: " + "processing frame");
                detector.processImage(getVisionImageFromFrame(frame))
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionObject>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionObject> objects) {
                                for (FirebaseVisionObject object : objects) {
                                    displayMsg(object.getTrackingId().toString());
                                    Log.d(LogTag, "onSuccess: "+ object.getBoundingBox() + " " + object.getTrackingId() + " " + object.getClassificationCategory());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(LogTag, "onFailure: ");
                                displayMsg("Object Detecton Failed");
                            }
                        });
            }
        });
    }
    
    private FirebaseVisionImage getVisionImageFromFrame(Frame frame) {
        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setHeight(frame.getSize().getHeight())
                .setWidth(frame.getSize().getWidth())
                .setRotation(frame.getRotation())
                .build();
        FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(frame.getData(), metadata);
        return image;
    }
    
    private void displayMsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
