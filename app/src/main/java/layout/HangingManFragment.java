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

import com.gfaiers.hangman.HangmanActivity;
import com.gfaiers.hangman.R;

public class HangingManFragment extends Fragment {

    static Paint paint = new Paint();
    static int intLivesRemaining, intLives;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hanging_man, container, false);

        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);

        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutFragment);
        relativeLayout.addView(new MyView(getActivity()));

        return rootView;
    }

    public class MyView extends View {

        int intMove = 1;
        long currentTime = 0, dieTime = 0;

        public MyView (Context context) {
            super(context);

        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float x = getWidth();
            float y = getHeight();
            float radius = y / 12;
            intLives = HangmanActivity.intLives;
            intLivesRemaining = HangmanActivity.intLivesRemaining;
            paint.setStrokeWidth(y / 50);
            canvas.drawRect(0 + paint.getStrokeWidth(), 0 + paint.getStrokeWidth(), x - paint.getStrokeWidth(), y - paint.getStrokeWidth(), paint);
            // Check how many lives the user set at the start
            switch (intLives) {
                // Depending on how many lives they've selected, draw the required amount of parts for the lives they've got remaining
                // Then when they are dead add the animation
                case 5:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            }
                        case 3:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                        case 4:
                            if (!(intLivesRemaining == 0)) {
                                // Reset the variables to make the man die
                                intMove = 1;
                                dieTime = 0;
                            }
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 10), (y - (y / 10)), (x - (x / 10)), (y - (y / 10)), paint); // Base line 1
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                            break;
                        default:
                            break;
                    }
                    break;
                case 6:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            }
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 4:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                        case 5:
                            if (!(intLivesRemaining == 0)) {
                                // Reset the variables to make the man die
                                intMove = 1;
                                dieTime = 0;
                            }
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 10), (y - (y / 10)), (x - (x / 10)), (y - (y / 10)), paint); // Base line 1
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                            break;
                        default:
                            break;
                    }
                    break;
                case 7:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            }
                        case 4:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 5:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                        case 6:
                            if (!(intLivesRemaining == 0)) {
                                // Reset the variables to make the man die
                                intMove = 1;
                                dieTime = 0;
                            }
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 10), (y - (y / 10)), (x - (x / 10)), (y - (y / 10)), paint); // Base line 1
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                            break;
                        default:
                            break;
                    }
                    break;
                case 8:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 1
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 91
                            }
                        case 4:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                            }
                        case 5:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 6:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                        case 7:
                            if (!(intLivesRemaining == 0)) {
                                // Reset the variables to make the man die
                                intMove = 1;
                                dieTime = 0;
                            }
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 10), (y - (y / 10)), (x - (x / 10)), (y - (y / 10)), paint); // Base line 1
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                            break;
                        default:
                            break;
                    }
                    break;
                case 9:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 91
                            }
                        case 4:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                            }
                        case 5:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 6:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                        case 7:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                        case 8:
                            if (!(intLivesRemaining == 0)) {
                                // Reset the variables to make the man die
                                intMove = 1;
                                dieTime = 0;
                            }
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 10), (y - (y / 10)), (x - (x / 10)), (y - (y / 10)), paint); // Base line 1
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                            break;
                        default:
                            break;
                    }
                    break;
                case 10:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 91
                            }
                        case 4:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                            }
                        case 5:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 6:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                        case 7:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                        case 8:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                        case 9:
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
                    break;
                case 11:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 91
                            }
                        case 4:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                            }
                        case 5:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 6:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                        case 7:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                        case 8:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                        case 9:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                        case 10:
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
                    break;
                case 12:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 91
                            }
                        case 4:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                            }
                        case 5:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 6:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                        case 7:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                        case 8:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                        case 9:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                        case 10:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                        case 11:
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
                    break;
                case 13:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                        case 4:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 91
                            }
                        case 5:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                            }
                        case 6:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 7:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                        case 8:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                        case 9:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                        case 10:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                        case 11:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                        case 12:
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
                    break;
                case 14:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (radius / 5) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            } else {
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left eye 14
                                canvas.drawLine((x / 2) + ((x / 40)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right eye 15
                                canvas.drawLine((x / 2) - ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + ((x / 40)), (y / 4) + (radius / 2) + (paint.getStrokeWidth() / 2) + intMove, paint); // Mouth 16
                            }
                        case 1:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) + (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Arm 13
                        case 2:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 4) + radius + (y / 25) + (paint.getStrokeWidth() / 2) + intMove, (x / 2) - (x / 7), ((y / 4) + radius + (y / 38)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Arm 12
                        case 3:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) - (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Right Leg 11
                        case 4:
                            paint.setStrokeWidth(y / 85);
                            canvas.drawLine((x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, ((x / 2) + (x / 13)), (y - (y / 5)) + (paint.getStrokeWidth() / 2) + intMove, paint); // Left Leg 10
                        case 5:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 9
                            } else {
                                canvas.drawLine((x / 2), ((y / 4) + (radius)) + (paint.getStrokeWidth() / 2) + intMove, (x / 2), (y / 2) + radius + (paint.getStrokeWidth() / 2) + intMove, paint); // Body 91
                            }
                        case 6:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove + (radius / 10), radius, paint); // Head 8
                            } else {
                                canvas.drawCircle((x / 2), (y / 4) + (paint.getStrokeWidth() / 2) + intMove, radius, paint); // Head 8
                            }
                        case 7:
                            paint.setStrokeWidth(y / 85);
                            if (currentTime - dieTime >= 1000) {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + (radius / 10) + intMove, paint); // Rope 7
                            } else {
                                canvas.drawLine((x / 2), (y / 11), (x / 2), ((y / 4) - (radius)) + intMove, paint); // Rope 7
                            }
                        case 8:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y / 11), (x / 4), ((y / 11) + (y / 15)), paint); // Right Top 6
                        case 9:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 4), (y / 11), (x / 2), (y / 11), paint); // Top line 3
                        case 10:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) - (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Right Base 5
                        case 11:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine(((x / 4) + (x / 15)), (y - (y / 10)), (x / 4), (y - (y / 6)), paint); // Left Base 4
                        case 12:
                            paint.setStrokeWidth(y / 50);
                            canvas.drawLine((x / 4), (y / 11), (x / 4), (y - (y / 10)), paint); //Vertical line 2
                        case 13:
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
                    break;
                case 15:
                    switch (intLivesRemaining) {
                        case 0:
                            if (dieTime == 0) dieTime = System.currentTimeMillis();
                            currentTime = System.currentTimeMillis();
                            if (intMove <= (y / 30)) {
                                intMove = intMove *2;
                            }
                            paint.setStrokeWidth(y / 150);
                            // 1 second has passed
                            if (currentTime - dieTime >= 1000) {
                                // Draw with X'd eyes and face lower
                                canvas.drawLine((x / 2) - ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye \
                                canvas.drawLine((x / 2) - ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) - ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Left eye /
                                canvas.drawLine((x / 2) + ((x / 30)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 50)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye \
                                canvas.drawLine((x / 2) + ((x / 50)), (y / 4) - (y / 30) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , (x / 2) + ((x / 30)), (y / 4) - (y / 60) + (paint.getStrokeWidth() / 2) + intMove + (radius / 5) , paint); // Right eye /
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
                    break;
                default:
            }
            invalidate();
        }
    }
}
