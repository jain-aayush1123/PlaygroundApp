package awsm.awsmizng.u.banking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import awsm.awsmizng.u.banking.Activities.CollapsingToolbar;
import awsm.awsmizng.u.banking.Activities.FirebaseML;
import awsm.awsmizng.u.banking.Activities.GraphSimple;
import awsm.awsmizng.u.banking.Activities.ObjectDetection;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Index extends AppCompatActivity {
    @BindView(R.id.btGraph)
    Button btGraph;
    @BindView(R.id.btML)
    Button btML;
    @BindView(R.id.btObjectTracking)
    Button btObjectTracking;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
    }
    
    @OnClick({R.id.btGraph, R.id.btML, R.id.btObjectTracking, R.id.btCollapsingToolbar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btGraph:
                funStartActivity(GraphSimple.class);
                break;
            case R.id.btML:
                funStartActivity(FirebaseML.class);
                break;
            case R.id.btObjectTracking:
                funStartActivity(ObjectDetection.class);
            case R.id.btCollapsingToolbar:
                funStartActivity(CollapsingToolbar.class);
        }
    }
    
    private void funStartActivity(Class myClass) {
        startActivity(new Intent(getApplicationContext(), myClass));
    }
}
