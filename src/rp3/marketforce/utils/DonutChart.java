package rp3.marketforce.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.R;

/**
 * Created by magno_000 on 30/05/2015.
 */
public class DonutChart extends View {


    private float radius;
    private List<Integer> values;
    private List<Integer> colors;

    Paint paint;
    Paint shadowPaint;

    Path myPath;
    Path shadowPath;

    RectF outterCircle;
    RectF innerCircle;
    RectF shadowRectF;


    public DonutChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DonutChart,
                0, 0
        );

        try {
            radius = a.getDimension(R.styleable.DonutChart_radius, 20.0f);
        } finally {
            a.recycle();
        }

        paint = new Paint();


        shadowPaint = new Paint();
        //shadowPaint.setColor(0xf0000000);
        //shadowPaint.setStyle(Paint.Style.STROKE);
        //shadowPaint.setAntiAlias(true);
        //shadowPaint.setStrokeWidth(6.0f);
        //shadowPaint.setMaskFilter(new BlurMaskFilter(4, BlurMaskFilter.Blur.SOLID));


        myPath = new Path();
        shadowPath = new Path();


        outterCircle = new RectF();
        innerCircle = new RectF();
        shadowRectF = new RectF();

        float adjust = (.019f*radius);
        //shadowRectF.set(adjust, adjust, radius*2-adjust, radius*2-adjust);

        adjust = .038f * radius;
        outterCircle.set(adjust, adjust, radius*2-adjust, radius*2-adjust);

        adjust = .276f * radius;
        innerCircle.set(adjust, adjust, radius*2-adjust, radius*2-adjust);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw shadow
        paint.setShader(null);
        float adjust = (.0095f*radius);
        //paint.setShadowLayer(8, adjust, -adjust, 0xaa000000);
        drawDonut(canvas,paint, 0,359.9f);

        if(values == null)
            return;
        if(colors == null)
            return;


        int firstValue = 0;
        for(int i = 0; i < values.size(); i++) {
            if(values.get(i) != 360) {
                setGradient(colors.get(i), colors.get(i));
                drawDonut(canvas, paint, firstValue, values.get(i));
                firstValue = firstValue + values.get(i);
            }
            else
            {
                setGradient(colors.get(i), colors.get(i));
                drawDonut(canvas, paint, 0, 359);
                drawDonut(canvas, paint, 359, 1);
                //firstValue = firstValue + values.get(i);
            }
        }



    }

    public void drawDonut(Canvas canvas, Paint paint, float start,float sweep){

        myPath.reset();
        myPath.arcTo(outterCircle, start, sweep, false);
        myPath.arcTo(innerCircle, start+sweep, -sweep, false);
        myPath.close();
        canvas.drawPath(myPath, paint);
    }

    public void setGradient(int sColor, int eColor){
        paint.setShader(new RadialGradient(radius, radius, radius-5,
                new int[]{sColor,eColor},
                new float[]{.6f,.95f}, Shader.TileMode.CLAMP) );
    }

    public void setValues(List<Integer> values)
    {
        List<Integer> newValues = new ArrayList<Integer>();
        for(int i = 0; i < values.size(); i ++)
        {
            newValues.add((int) (values.get(i) * 3.6));
        }
        this.values = newValues;
    }

    public void setColors(List<Integer> colors)
    {
        this.colors = colors;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = (int) radius*2;
        int desiredHeight = (int) radius*2;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //70dp exact
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }else if (widthMode == MeasureSpec.AT_MOST) {
            //wrap content
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

}