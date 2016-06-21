package org.itsk.slideimagedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SlideImageView slideImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slideImageView= (SlideImageView) findViewById(R.id.slider);

        slideImageView.setOnMoveDoneListener(new SlideImageView.OnMoveDoneListener() {
            @Override
            public void moveDone() {
                slideImageView.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "验证完成", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
