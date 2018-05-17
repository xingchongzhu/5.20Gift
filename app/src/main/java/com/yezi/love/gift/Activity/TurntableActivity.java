package com.yezi.love.gift.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yezi.love.gift.Activity.view.LuckPanLayout;
import com.yezi.love.gift.Activity.view.RotatePan;
import com.yezi.love520.gift.R;


public class TurntableActivity extends AppCompatActivity implements LuckPanLayout.AnimationEndListener{

    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private ImageView goBtn;
    private ImageView yunIv;
    private String[] strs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_turntable);
        strs = getResources().getStringArray(R.array.names);
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.setAnimationEndListener(this);
        goBtn = (ImageView)findViewById(R.id.go);
        yunIv = (ImageView)findViewById(R.id.yun);
    }

    public void rotation(View view){
        luckPanLayout.rotate(-1,100);
    }

    @Override
    public void endAnimation(int position) {
        Toast.makeText(this,"Position = "+position+","+strs[position], Toast.LENGTH_SHORT).show();
    }


}
