package com.example.draganddrop;

import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;
    float actualScreenWidth, actualScreenHeight;
    int heightEnd, widthEnd;
    RelativeLayout rLayout;
    float positionX, positionY;

    @SuppressLint("ClickableViewAccessibility")
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
            Rational ratio = new Rational((int)actualScreenWidth, (int)actualScreenHeight);
            PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
            builder.setAspectRatio(ratio).build();
            enterPictureInPictureMode(builder.build());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint({"ClickableViewAccessibility", "NewApi"})
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        if (isInPictureInPictureMode) {
            int newWidth = newConfig.screenWidthDp;
            int newHeight = newConfig.screenHeightDp;

            //set the imageview to the respective params in pip mode
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imgView.getLayoutParams();
            params.height = (heightEnd / 2) - convertDpToPixel(this, actualScreenHeight / newHeight);
            params.width = (widthEnd / 3) - convertDpToPixel(this, actualScreenWidth / newWidth);
            imgView.setLayoutParams(params);
            if(positionX==0 && positionY==0){
                imgView.setX(0);
                imgView.setY(0);
            }
//            imgView.setX((newWidth/2)-(positionX/newWidth));

            //set the imageview on the position after in p i p mode.


            Log.i("NewDimen",
                    "actual : " + actualScreenWidth + " x " + actualScreenHeight + "\n" +

                            "end : " + widthEnd + "x" + heightEnd + "\n" +

                            "new : " + newWidth + " x " + newHeight + "\n" +

                            "newPosition : " + imgView.getX() + " x " + imgView.getY() + "\n" +

                            "params : " + params.width + " x " + params.height);

        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imgView.getLayoutParams();
            params.height = heightEnd;
            params.width = widthEnd;

            positionX=imgView.getX();
            positionY=imgView.getY();

            imgView.setLayoutParams(params);

            Log.i("NewDimen", "Position : " + positionX + " x " + positionY);
        }
    }

    public static int convertDpToPixel(Context context, float dp) {
        return (int) (dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}