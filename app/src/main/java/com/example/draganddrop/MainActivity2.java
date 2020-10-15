package com.example.draganddrop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static com.example.draganddrop.MainActivity.convertDpToPixel;

public class MainActivity2 extends AppCompatActivity {
    private ImageView imgView;
    float actualScreenWidth, actualScreenHeight;
    int heightEnd, widthEnd;
    RelativeLayout rLayout;
    float positionX, positionY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rLayout = findViewById(R.id.parent_layout);
        imgView = findViewById(R.id.image_view);
        if (getSupportActionBar() != null) this.getSupportActionBar().hide();

        actualScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
        actualScreenHeight = getWindowManager().getDefaultDisplay().getHeight();

        heightEnd = convertDpToPixel(this, 150);
        widthEnd = convertDpToPixel(this, 120);

        imgView.setOnTouchListener(new onDragTouchListener(imgView, rLayout));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Rational ratio = new Rational(rLayout.getWidth(), rLayout.getHeight());
            PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
            builder.setAspectRatio(ratio).build();
            enterPictureInPictureMode(builder.build());
        }
    }


    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if(isInPictureInPictureMode){
            int newWidth = newConfig.screenWidthDp;
            int newHeight = newConfig.screenHeightDp;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(newWidth,newHeight);
            rLayout.setLayoutParams(params);

            RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(newWidth,newHeight);
            lp.height = (heightEnd / 2) - convertDpToPixel(this, actualScreenHeight / newHeight);
            lp.width = (widthEnd / 2) - convertDpToPixel(this, actualScreenWidth / newWidth);

            imgView.setLayoutParams(lp);
        }else{
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int)actualScreenWidth,(int)actualScreenHeight);
            rLayout.setLayoutParams(params);

            RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams((int)actualScreenWidth,(int)actualScreenHeight);
            lp.height = heightEnd;
            lp.width = widthEnd;
        }
    }
}