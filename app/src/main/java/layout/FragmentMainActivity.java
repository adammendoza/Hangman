package layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gfaiers.hangman.MainActivity;
import com.gfaiers.hangman.R;

public class FragmentMainActivity extends Fragment {

    Paint paint = new Paint();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hanging_man, container, false);

        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        RelativeLayout relativeLayoutFragment = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutFragment);
        relativeLayoutFragment.addView(new MyView(getActivity()));

        return rootView;
    }

    public class MyView extends View {

        int intMove = 1;
        long currentTime = 0, dieTime = 0;

        public MyView (Context context) {
            super(context);
            this.postInvalidate();

        }
        @Override
        protected void onDraw(Canvas canvas) {
            /*In this section I want to generate a random letter every second
            * This letter will not be the same as a previously selected letter
            * If the letter is correct for "HANGMAN"
            * Then place it in the section above on MainActivity
            * If it's not correct then draw the next part of the man
            * Assume 15 lives
            */
            super.onDraw(canvas);
            float x = getWidth();
            float y = getHeight();
            float radius = y / 12;

            int intLivesRemaining = MainActivity.intLivesRem;
            paint.setStrokeWidth(y / 50);
            canvas.drawRect(0 + paint.getStrokeWidth(), 0 + paint.getStrokeWidth(), x - paint.getStrokeWidth(), y - paint.getStrokeWidth(), paint);
            switch (intLivesRemaining) {
                case 0:
                    if (dieTime == 0) dieTime = System.currentTimeMillis();
                    currentTime = System.currentTimeMillis();
                    if (intMove <= (y / 30)) {
                        intMove = intMove * 2;
                    }
                    paint.setStrokeWidth(y / 150);
                    // 1 second has passed
                    if (currentTime - dieTime >= 1000) {
                        // Draw with X'd eyes and face lower
                        canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5), (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5), paint); // Left eye \
                        canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5), (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5), paint); // Left eye /
                        canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5), (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5), paint); // Right eye \
                        canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5), (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5), paint); // Right eye /
                        canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                    } else {
                        canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                        canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                        canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                    }
                case 1:
                    paint.setStrokeWidth(y / 150);
                    // 1 second has passed
                    if (currentTime - dieTime >= 1000) {
                        canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                    } else {
                        canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                    }
                case 2:
                    paint.setStrokeWidth(y / 85);
                    canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                case 3:
                    paint.setStrokeWidth(y / 85);
                    canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                case 4:
                    paint.setStrokeWidth(y / 85);
                    canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                case 5:
                    paint.setStrokeWidth(y / 85);
                    canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                case 6:
                    paint.setStrokeWidth(y / 85);
                    if (currentTime - dieTime >= 1000) {
                        canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                    } else {
                        canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 91
                    }
                case 7:
                    paint.setStrokeWidth(y / 85);
                    if (currentTime - dieTime >= 1000) {
                        canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                    } else {
                        canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                    }
                case 8:
                    paint.setStrokeWidth(y / 85);
                    if (currentTime - dieTime >= 1000) {
                        canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                    } else {
                        canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                    }
                case 9:
                    paint.setStrokeWidth(y / 50);
                    canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                case 10:
                    paint.setStrokeWidth(y / 50);
                    canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                case 11:
                    paint.setStrokeWidth(y / 50);
                    canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                case 12:
                    paint.setStrokeWidth(y / 50);
                    canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                case 13:
                    paint.setStrokeWidth(y / 50);
                    canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                case 14:
                    if (!(intLivesRemaining == 0)) {
                        // Reset the variables to make the man die
                        intMove = 1;
                        dieTime = 0;
                    }
                    paint.setStrokeWidth(y / 50);
                    canvas.drawLine((x / 10), (y - (y / 10)), (x - (x / 10)), (y - (y / 10)), paint); // Base line 1
                    break;
                default:
                    break;
            }
            invalidate();
        }
    }
}
