package com.example.synergyjavamod3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView coordinatesOut;
    private float x;
    private float y;
    private String sDown;
    private String sMove;
    private String sUp;

    private final float x_CAT = 750;
    private final float y_CAT = 700;
    private final float delta_CAt = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatesOut = findViewById(R.id.coordinatesOut);
        coordinatesOut.setOnTouchListener(listener);
    }

    private View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            x = motionEvent.getX();
            y = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sDown = "Нажатие: координата X = " + x + ", координата Y = " + y;
                    sMove = "";
                    sUp = "";
                    break;
                case MotionEvent.ACTION_MOVE:
                    sMove = "Движение: координата X = " + x + ", координата Y = " + y;
                    if (x < (x_CAT + delta_CAt) && x > (x_CAT - delta_CAt) && y < (y_CAT + delta_CAt) && y > (y_CAT - delta_CAt)) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.successful_search, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.LEFT, (int) x_CAT, (int) y_CAT);

                        LinearLayout toastConteiner = (LinearLayout) toast.getView();

                        // добавление картинки
                        ImageView cat = new ImageView(getApplicationContext());
                        cat.setImageResource(R.drawable.cat);
                        toastConteiner.addView(cat, 1);
                        toast.show();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    sMove = "";
                    sUp = "Отпускание: координата X = " + x + ", координата Y = " + y;
                    break;
            }

            coordinatesOut.setText(sDown + "\n" + sMove + "\n" + sUp);

            return false;
        }
    };
}