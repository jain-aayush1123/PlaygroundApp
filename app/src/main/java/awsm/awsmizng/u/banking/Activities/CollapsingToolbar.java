package awsm.awsmizng.u.banking.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import awsm.awsmizng.u.banking.R;

public class CollapsingToolbar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toolbar);
        ActionBar bar = getSupportActionBar();
        bar.hide();
    }
}
