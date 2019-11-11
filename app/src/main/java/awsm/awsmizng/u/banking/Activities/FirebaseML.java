package awsm.awsmizng.u.banking.Activities;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.squareup.picasso.Picasso;

import java.util.List;

import awsm.awsmizng.u.banking.R;

public class FirebaseML extends AppCompatActivity {
    EditText etImageUrl;
    ImageView imageView;
    Button btText, btFaces, btLables, btDone, btClear;
    LinearLayout llButtons;
    FirebaseVisionImage image;
    final String AppTag = "wheuqewedhwe";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ml);
        etImageUrl = findViewById(R.id.image_url_field);
        imageView = findViewById(R.id.image_holder);
        btLables = findViewById(R.id.btLables);
        btFaces = findViewById(R.id.btFaces);
        btText = findViewById(R.id.btText);
        btDone = findViewById(R.id.btDone);
        btClear = findViewById(R.id.btClear);
        llButtons = findViewById(R.id.llButtons);
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.with(getApplicationContext())
                        .load(etImageUrl.getText().toString().trim())
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imageView)
                ;
                llButtons.setVisibility(View.VISIBLE);
            }
        });
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etImageUrl.setText(null);
                llButtons.setVisibility(View.GONE);
            }
        });
        btText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextRecognition();
            }
        });
        btFaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FaceRecognition();
            }
        });
        btLables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageLabels();
            }
        });
    }
    
    public void TextRecognition() {
        image = FirebaseVisionImage.fromBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap());
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> result = detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        String t = "";
                        String tLineWise = "";
                        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                            String blockText = block.getText();
                            Float blockConfidence = block.getConfidence();
                            Point[] blockCornerPoints = block.getCornerPoints();
                            Rect blockFrame = block.getBoundingBox();
                            for (FirebaseVisionText.Line line : block.getLines()) {
                                String lineText = line.getText();
                                Float lineConfidence = line.getConfidence();
                                Point[] lineCornerPoints = line.getCornerPoints();
                                Rect lineFrame = line.getBoundingBox();
                                tLineWise += lineText + "\n";
                                for (FirebaseVisionText.Element element : line.getElements()) {
                                    String elementText = element.getText();
                                    Float elementConfidence = element.getConfidence();
                                    Point[] elementCornerPoints = element.getCornerPoints();
                                    Rect elementFrame = element.getBoundingBox();
                                    t += elementText + " ";
                                }
                            }
                        }
                        displayMsg(tLineWise);
                        Log.d(AppTag, "onSuccess: " + t);
                        Log.d(AppTag, "onSuccess: " + tLineWise);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayMsg("Fail");
                    }
                });
    }
    
    public void FaceRecognition() {
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector();
        detector.detectInImage(FirebaseVisionImage.fromBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap()))
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                        displayMsg("Succes Face");
                        //TODO complete on success code with drawing rectangles around faces
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayMsg("Failure Face");
                    }
                });
    }
    
    public void ImageLabels() {
        image = FirebaseVisionImage.fromBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap());
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
        labeler.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                        String labelText = "";
                        for (FirebaseVisionImageLabel label : firebaseVisionImageLabels) {
                            labelText += label.getText() + " ";
                        }
                        displayMsg(labelText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayMsg("Failure Labels");
                    }
                })
        ;
    }
    
    private void displayMsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
