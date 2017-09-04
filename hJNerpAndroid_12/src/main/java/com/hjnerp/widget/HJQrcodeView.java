package com.hjnerp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.hjnerpandroid.R;


public class HJQrcodeView extends View {
	  private static final long ANIMATION_DELAY = 20;
	  private Paint finderMaskPaint;
	  private int measureedWidth;
	  private int measureedHeight;

	  
	  private Rect topRect = new Rect();
	  private Rect bottomRect = new Rect();
	  private Rect rightRect = new Rect();
	  private Rect leftRect = new Rect();
	  private Rect middleRect = new Rect();

	  private Rect lineRect = new Rect();
	  private Drawable zx_code_kuang;
	  private Drawable zx_code_line; 
	  private int lineHeight;

	  
	public HJQrcodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		  init(context);

	}
	
	public HJQrcodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		  init(context);

	}

	public HJQrcodeView(Context context ) {
		super(context);
		// TODO Auto-generated constructor stub
		  init(context);

	}
	
	  private void init(Context context) {
		    int finder_mask = context.getResources().getColor(R.color.finder_mask);
		    finderMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		    finderMaskPaint.setColor(finder_mask);
		    zx_code_kuang = context.getResources().getDrawable(R.drawable.zx_code_kuang);
		    zx_code_line = context.getResources().getDrawable(R.drawable.qrcode_scan_line);
		   
		    lineHeight = 20;
		  }

	  @Override
	  protected void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    canvas.drawRect(leftRect, finderMaskPaint);
	    canvas.drawRect(topRect, finderMaskPaint);
	    canvas.drawRect(rightRect, finderMaskPaint);
	    canvas.drawRect(bottomRect, finderMaskPaint);
	    //画框
	    zx_code_kuang.setBounds(middleRect);
	    zx_code_kuang.draw(canvas);
	    if (lineRect.bottom < middleRect.bottom) {
	      zx_code_line.setBounds(lineRect);
	      lineRect.top = lineRect.top + lineHeight / 2;
	      lineRect.bottom = lineRect.bottom + lineHeight / 2;
	    } else {
	      lineRect.set(middleRect);
	      lineRect.bottom = lineRect.top + lineHeight;
	      zx_code_line.setBounds(lineRect);
	    }
	    zx_code_line.draw(canvas);
	     
	    postInvalidateDelayed(ANIMATION_DELAY, middleRect.left, middleRect.top, middleRect.right, middleRect.bottom);
	  }

	  @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    measureedWidth = MeasureSpec.getSize(widthMeasureSpec);
	    measureedHeight = MeasureSpec.getSize(heightMeasureSpec);
	    int borderWidth = measureedWidth / 2 + 100;
	    middleRect.set((measureedWidth - borderWidth) / 2, (measureedHeight - borderWidth) / 2 -100, (measureedWidth - borderWidth) / 2 + borderWidth, (measureedHeight - borderWidth) / 2 + borderWidth -100);
	    lineRect.set(middleRect);
	    lineRect.bottom = lineRect.top + lineHeight;
	    leftRect.set(0, middleRect.top, middleRect.left, middleRect.bottom);
	    topRect.set(0, 0, measureedWidth, middleRect.top);
	    rightRect.set(middleRect.right, middleRect.top, measureedWidth, middleRect.bottom);
	    bottomRect.set(0, middleRect.bottom, measureedWidth, measureedHeight);
	  }

	  
}
