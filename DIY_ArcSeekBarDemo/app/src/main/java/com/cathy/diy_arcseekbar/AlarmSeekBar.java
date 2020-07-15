package com.cathy.diy_arcseekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cathy.diy_arcseekbar.tools.FontTool;


/**
 * @author admin
 */
public class AlarmSeekBar extends FrameLayout {

    private Paint mPaint;
    private int thumbWidth = dpToPx(30);
    private Bitmap bitmapGetUp;
    private Bitmap bitmapSleep;
    private Paint textPaint;
    private float fontHeight;
    private SweepGradient sweepGradient;
    private Paint circlePaint;
    private RectF circleRectF;

    private Path path = new Path();
    /**
     * 外層錶盤的寬度
     */

    private int borderWidth = thumbWidth;
    private int innerPaintDistance = thumbWidth / 2;

    /**
     * 外环半径
     */
    private float outerCircleRadius;
    /**
     * 刻度每一个格的角度
     */
    private float gapAngle;
    /**
     * 绘制的文字的位置离圆心的距离
     */
    private float circleTextRadius;
    private TimeListener mListener;
    private TextView centerText;
    private int realSleepTime;
    private int realGetUpTime;
    private Context context;


    public AlarmSeekBar(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public AlarmSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public AlarmSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }


    private int outCircleBorderWidth = thumbWidth;
    /**
     * 圆环的其实角度
     */
    private float startAngle = 0;
    /**
     * 圆环的终止角度
     */
    private float endAngle = 0;


    private void initView() {
        bitmapSleep = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_sleeping), thumbWidth, thumbWidth, true);
        bitmapGetUp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_alarm), thumbWidth, thumbWidth, true);
        centerText = new TextView(getContext());
        centerText.setTextSize(18);
        centerText.setTypeface(FontTool.getNumberLightTypeface());
        centerText.setTextColor(Color.WHITE);
        addView(centerText, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) centerText.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outCircleBorderWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(dpToPx(7.2f));
        textPaint.setTypeface(FontTool.getNumberLightTypeface());
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        fontHeight = -fontMetrics.ascent;

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.parseColor("#523587"));
        circlePaint.setStrokeWidth(outCircleBorderWidth);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        gapAngle = 360 / (12f * 5);
    }

    private void setCenterText() {
        int duration;
        if (getUpTimeMoreThan12) {
            if (sleepTimeMoreThan12) {
                if (realSleepTime <= realGetUpTime) {
                    duration = realGetUpTime - realSleepTime;
                } else {
                    duration = 24 * 60 - (realSleepTime - realGetUpTime);
                }
            } else {
                duration = 12 * 60 + (realGetUpTime - realSleepTime);
            }
        } else {
            if (sleepTimeMoreThan12) {
                duration = 12 * 60 + (realGetUpTime - realSleepTime);
            } else {
                if (realGetUpTime < realSleepTime) {
                    duration = 24 * 60 - (realSleepTime - realGetUpTime);
                } else {
                    duration = realGetUpTime - realSleepTime;
                }
            }
        }
        int hours = duration / 60;
        int minute = duration % 60;
        String currentDisplay;
        if (minute != 0 && hours != 0) {
            currentDisplay = String.format(getContext().getString(R.string.alarm_display_format), hours, minute);
        } else {
            if (minute == 0 && hours != 0) {
                currentDisplay = hours + "HR";
            } else if (hours == 0 && minute != 0) {
                currentDisplay = minute + "MIN";
            } else {
                currentDisplay = "0MIN";
            }
        }
        centerText.setText(currentDisplay);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        outerCircleRadius = w / 2f - innerPaintDistance;
        circleTextRadius = outerCircleRadius - innerPaintDistance - dpToPx(17);
        //设置进度渐变
        int[] colors = {
                Color.parseColor("#FF9F0A"), Color.parseColor("#FFD30A"),
                Color.parseColor("#FF9F0A")
        };
        //设置需要的颜色，首位颜色必须一致。
        sweepGradient = new SweepGradient(0F, 0, colors, null);
        circlePaint.setShader(sweepGradient);
        circlePaint.setStrokeWidth(borderWidth);
        circleRectF = new RectF(-outerCircleRadius, -outerCircleRadius, outerCircleRadius, outerCircleRadius);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.translate(getWidth() / 2f, getHeight() / 2f);
        //设置外环底色
        mPaint.setColor(ContextCompat.getColor(context, R.color.color_circle_path_1C1C1E));
        mPaint.setStrokeWidth(borderWidth);
        canvas.drawCircle(0, 0, outerCircleRadius, mPaint);
        canvas.save();
        //设置刻度颜色
        mPaint.setColor(ContextCompat.getColor(context, R.color.color_scale_464649));
        for (int i = 0; i < 12 * 5; i++) {
            if (i % 15 == 0) {
                //12,3,6,9
                mPaint.setColor(ContextCompat.getColor(context, R.color.color_scale_8C8C91_big));
                mPaint.setStrokeWidth(dpToPx(3));
                canvas.drawLine(0, outerCircleRadius - innerPaintDistance - dpToPx(5), 0, outerCircleRadius - innerPaintDistance - dpToPx(10), mPaint);
            } else if (i % 5 == 0) {
                mPaint.setColor(ContextCompat.getColor(context, R.color.color_scale_464649));
                mPaint.setStrokeWidth(dpToPx(2));
                canvas.drawLine(0, outerCircleRadius - innerPaintDistance - dpToPx(6), 0, outerCircleRadius - innerPaintDistance - dpToPx(9), mPaint);

            } else {
                //设置刻度渐变
                if (i > 0 && i < 15) {
                    mPaint.setColor(Color.parseColor("#FF9F0A"));
                } else if (i > 15 && i < 22 || i > 53 && i < 60) {
                    mPaint.setColor(Color.parseColor("#FFB80A"));

                } else {
                    mPaint.setColor(Color.parseColor("#FFD30A"));

                }
                //绘制其他时间刻度。
                mPaint.setStrokeWidth(dpToPx(1.5f));
                canvas.drawLine(0, outerCircleRadius - innerPaintDistance - dpToPx(7), 0, outerCircleRadius - innerPaintDistance - dpToPx(8), mPaint);
            }
            canvas.rotate(gapAngle);
        }
        canvas.restore();
        for (int i = 0; i < 12; i++) {
            float angle = 360 / 12f;
            canvas.drawText((i == 0 ? 12 : i) + "", (float) (circleTextRadius * Math.cos(Math.toRadians(angle * i - 90))), (float) (circleTextRadius * Math.sin(Math.toRadians(angle * i - 90))) + fontHeight / 2f + 1, textPaint);
        }

        path.reset();
        path.addArc(circleRectF, startAngle, endAngle - startAngle);
        canvas.drawPath(path, circlePaint);
        drawMorningIcon(canvas);
        drawEveningIcon(canvas);
    }


    private void drawMorningIcon(Canvas canvas) {
        float y = (float) (Math.sin(Math.toRadians(endAngle)) * outerCircleRadius - thumbWidth / 2f);
        float x = (float) (Math.cos(Math.toRadians(endAngle)) * outerCircleRadius - thumbWidth / 2f);
        canvas.drawBitmap(bitmapGetUp, x, y, mPaint);
    }

    private void drawEveningIcon(Canvas canvas) {
        float y = (float) (Math.sin(Math.toRadians(startAngle)) * outerCircleRadius - thumbWidth / 2f);
        float x = (float) (Math.cos(Math.toRadians(startAngle)) * outerCircleRadius - thumbWidth / 2f);
        canvas.drawBitmap(bitmapSleep, x, y, mPaint);
    }


    private boolean isTouchMorningIcon;
    private boolean isTouchEveningIcon;

    boolean getUpTimeMoreThan12 = false;
    boolean sleepTimeMoreThan12 = false;


    float lastMorningX = 0;
    float lastEveningX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x;
        float y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                if (isTouchMorningIcon(x, y) && nearMorningIcon(x, y)) {
                    isTouchMorningIcon = true;
                    return true;
                } else if (isTouchEveningIcon(x, y)) {
                    isTouchEveningIcon = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                if (isTouchMorningIcon) {
                    moveMorningIcon(x, y);
                    if ((lastMorningX - getWidth() / 2.0f < 0) ^ (x - getWidth() / 2.0f < 0) && y - getHeight() / 2.0f < 0) {
                        getUpTimeMoreThan12 = !getUpTimeMoreThan12;
                    }
                    if (mListener != null) {
                        float tempAngle = endAngle + 90;
                        if (tempAngle >= 360) {
                            tempAngle -= 360;
                        } else if (tempAngle < -360) {
                            tempAngle += 360;
                        }
                        realGetUpTime = (int) (12 * 60 * (tempAngle) / 360);
                        if (getUpTimeMoreThan12) {
                            mListener.onMorningTime(realGetUpTime + 12 * 60);
                        } else {
                            mListener.onMorningTime(realGetUpTime);
                        }
                    }
                    lastMorningX = x;
                } else if (isTouchEveningIcon) {
                    moveEveningIcon(x, y);
                    if ((lastEveningX - getWidth() / 2.0f < 0) ^ (x - getWidth() / 2.0f < 0) && y - getHeight() / 2.0f < 0) {
                        sleepTimeMoreThan12 = !sleepTimeMoreThan12;
                    }
                    if (mListener != null) {
                        float tempAngle = startAngle + 90;
                        if (tempAngle >= 360) {
                            tempAngle -= 360;
                        } else if (tempAngle < -360) {
                            tempAngle += 360;
                        }
                        realSleepTime = (int) (12 * 60 * (tempAngle) / 360);
                        if (sleepTimeMoreThan12) {
                            mListener.onSleepTime(realSleepTime + 12 * 60);
                        } else {
                            mListener.onSleepTime(realSleepTime);
                        }
                    }
                    lastEveningX = x;
                }
                setCenterText();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mListener != null) {
                    if (isTouchMorningIcon) {
                        mListener.onMorningEnd();
                    }
                    if (isTouchEveningIcon) {
                        mListener.onSleepEnd();
                    }
                }
                isTouchEveningIcon = false;
                isTouchMorningIcon = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void moveMorningIcon(float x, float y) {
        x = x - getWidth() / 2f;
        y = y - getHeight() / 2f;
        double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double aSin = 0;
        float angle = 0;
        if (x > 0 && y <= 0) {
            aSin = Math.asin(-y / radius);
            angle = 90 - (float) Math.toDegrees(aSin);
        } else if (x > 0 && y > 0) {
            aSin = Math.asin(y / radius);
            angle = (float) Math.toDegrees(aSin) + 90;
        } else if (x < 0 & y > 0) {
            aSin = Math.asin(y / radius);
            angle = 90 - (float) Math.toDegrees(aSin) + 180;
        } else {
            aSin = Math.asin(-y / radius);
            angle = (float) Math.toDegrees(aSin) + 270;
        }
        endAngle = angle - 90;
        if (endAngle < startAngle) {
            endAngle += 360;
        } else if (endAngle - startAngle > 360) {
            endAngle -= 360;
        }
        invalidate();
    }

    private void moveEveningIcon(float x, float y) {
        x = x - getWidth() / 2f;
        y = y - getHeight() / 2f;
        double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double aSin = 0;
        float angle = 0;
        if (x > 0 && y <= 0) {
            aSin = Math.asin(-y / radius);
            angle = 90 - (float) Math.toDegrees(aSin);
        } else if (x > 0 && y > 0) {
            aSin = Math.asin(y / radius);
            angle = (float) Math.toDegrees(aSin) + 90;
        } else if (x < 0 & y > 0) {
            aSin = Math.asin(y / radius);
            angle = 90 - (float) Math.toDegrees(aSin) + 180;
        } else {
            aSin = Math.asin(-y / radius);
            angle = (float) Math.toDegrees(aSin) + 270;
        }
        startAngle = angle - 90;
        if (endAngle < startAngle) {
            endAngle += 360;
        } else if (endAngle - startAngle > 360) {
            endAngle -= 360;
        }
        invalidate();
    }

    private boolean isTouchMorningIcon(float x, float y) {
        x = x - getWidth() / 2f;
        y = y - getHeight() / 2f;
        float iconX = (float) (Math.cos(Math.toRadians(endAngle)) * outerCircleRadius);
        float iconY = (float) (Math.sin(Math.toRadians(endAngle)) * outerCircleRadius);
        int touchThumbRadius = thumbWidth * 2;
        return calculateEventDistance(x, y, iconX, iconY) < touchThumbRadius;
//        return x < iconX + touchThumbRadius && x > iconX - touchThumbRadius && y < iconY + touchThumbRadius && y > iconY - touchThumbRadius;
    }

    private boolean isTouchEveningIcon(float x, float y) {
        x = x - getWidth() / 2f;
        y = y - getHeight() / 2f;
        float iconX = (float) (Math.cos(Math.toRadians(startAngle)) * outerCircleRadius);
        float iconY = (float) (Math.sin(Math.toRadians(startAngle)) * outerCircleRadius);
        int touchThumbRadius = thumbWidth * 2;
        return calculateEventDistance(x, y, iconX, iconY) < touchThumbRadius;
//        return x < iconX + touchThumbRadius && x > iconX - touchThumbRadius && y < iconY + touchThumbRadius && y > iconY - touchThumbRadius;
    }

    /**
     * 是否离morning更近
     *
     * @param x
     * @param y
     * @return
     */
    private boolean nearMorningIcon(float x, float y) {
        x = x - getWidth() / 2f;
        y = y - getHeight() / 2f;
        float morningX = (float) (Math.cos(Math.toRadians(endAngle)) * outerCircleRadius);
        float morningY = (float) (Math.sin(Math.toRadians(endAngle)) * outerCircleRadius);
        float eveningX = (float) (Math.cos(Math.toRadians(startAngle)) * outerCircleRadius);
        float eveningY = (float) (Math.sin(Math.toRadians(startAngle)) * outerCircleRadius);
        return calculateEventDistance(x, y, morningX, morningY) < calculateEventDistance(x, y, eveningX, eveningY);
    }

    /**
     * 计算两个Event之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private float calculateEventDistance(float x1, float y1, float x2, float y2) {
        float x = Math.abs(x1 - x2);
        float y = Math.abs(y1 - y2);
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public int dpToPx(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    /**
     * 设置闹钟的进度
     *
     * @param getUpTime
     * @param sleepTime
     */
    public void setAlarmProgressData(int getUpTime, int sleepTime) {
        if (getUpTime > 12 * 60) {
            this.getUpTimeMoreThan12 = true;
            realGetUpTime = getUpTime - 12 * 60;
        } else {
            this.getUpTimeMoreThan12 = false;
            realGetUpTime = getUpTime;
        }
        post(() -> lastMorningX = getWidth() / 2.0f - getWidth() / 2.0f * (realGetUpTime - 6 * 60) / (6 * 60));
        if (sleepTime > 12 * 60) {
            this.sleepTimeMoreThan12 = true;
            realSleepTime = sleepTime - 12 * 60;
        } else {
            this.sleepTimeMoreThan12 = false;
            realSleepTime = sleepTime;
        }
        post(() -> lastEveningX = getWidth() / 2.0f - getWidth() / 2.0f * (realSleepTime - 6 * 60) / (6 * 60));
        if (mListener != null) {
            mListener.onSleepTime(sleepTime);
            mListener.onMorningTime(getUpTime);
        }
        startAngle = realSleepTime * 360 / (12 * 60) - 90;
        endAngle = realGetUpTime * 360 / (12 * 60) - 90;
        if (endAngle < startAngle) {
            this.endAngle += 360;
        } else if (endAngle - startAngle > 360) {
            this.endAngle -= 360;
        }
        invalidate();
        setCenterText();
    }


    public interface TimeListener {
        void onMorningTime(int time);

        void onSleepTime(int time);

        void onMorningEnd();

        void onSleepEnd();
    }

    public void setTimeListener(TimeListener listener) {
        this.mListener = listener;
    }
}
