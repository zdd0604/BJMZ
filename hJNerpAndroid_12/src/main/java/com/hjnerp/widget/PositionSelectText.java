package com.hjnerp.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.PopupWindow;


public class PositionSelectText extends EditText
{
	private final PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener()
	{
		@Override
		public void onDismiss()
		{
			setSelected(false);
		}
	};
	private PositionSelectPopupWindow popup;
	private int popupTriggerLimit = 20;
	
	public PositionSelectText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		if(getDefaultEditable())
		{
			setFocusable(true);
		}else{
			setFocusable(false);
		}
	}
	
	@Override
	protected MovementMethod getDefaultMovementMethod()
	{
		if(getDefaultEditable())
		{
			return ArrowKeyMovementMethod.getInstance(); //依旧返回为null
		}
		else
		{
			return null;
		}
	}
	
	@Override
	protected boolean getDefaultEditable()
	{ //TODO 如果可编辑需设置为true
		return false;
	}

	public void setPopup(PositionSelectPopupWindow popup)
	{
		this.popup = popup;
		if(popup != null)
		{
			popup.setOnDismissListener(dismissListener);
		}
	}
	
	public PositionSelectPopupWindow getPopup()
	{
		return popup;
	}
	
	public void setPopupTriggerLimit(int popupTriggerLimit)
	{
		this.popupTriggerLimit = popupTriggerLimit;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
			case MotionEvent.ACTION_UP:
			{
				int currX = (int) event.getX();
				if(!getDefaultEditable() || (getDefaultEditable() && isPressedOnSelectTrigger(currX)))
				{
					setSelected(!isSelected());
				}
			}
				break;
			default:
				break;
		}
		return super.onTouchEvent(event);
	}

	private boolean isPressedOnSelectTrigger(int currX)
	{
		Drawable drawableRight = getCompoundDrawables()[2];
		int w = getWidth();
		int dw = drawableRight.getIntrinsicWidth();
		return currX > (w - dw - popupTriggerLimit) && currX < (w + popupTriggerLimit);
	}
	
	protected boolean canPopup()
	{
		return popup != null && popup.canShow();
	}
	
	@Override
	protected void dispatchSetSelected(boolean selected)
	{
		 if(canPopup())
		 {
			 if(selected)
			 {
				 if(!popup.isShowing())
				 {
					 popup.setWidth(getWidth());
					 popup.showAsDropDown(this);
					 popup.update();
				 }
			 }else{
				 if(popup.isShowing())
				 {
					 popup.dismiss();
				 }
			 }
		 }
	}
}
