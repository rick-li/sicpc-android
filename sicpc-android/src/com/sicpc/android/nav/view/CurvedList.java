package com.sicpc.android.nav.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class CurvedList extends ListView {

	private static final String TAG = "CurvedList";

	private final Camera mCamera = new Camera();
	private final Matrix mMatrix = new Matrix();
	/** Paint object to draw with */
	private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

	private float maxTranslateXZ = 0f;
	private int maxAlpha = 255;
	private int minAlpha = 100;

	public CurvedList(Context context) {
		super(context);
	}

	public CurvedList(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		// (top,left) is the pixel position of the child inside the list
		final int marginTop = 0;
		final int top = child.getTop();
		int left = child.getLeft();
		// center point of child
		final int childCenterY = child.getHeight() / 2 + marginTop;
		int childCenterX = child.getWidth() / 2;
		// center of list
		final int parentCenterY = getHeight() / 2;
		// center point of child relative to list
		final int absChildCenterY = child.getTop() + childCenterY;
		// distance of child center to the list center
		final int distanceY = Math.abs(parentCenterY - absChildCenterY);
		// radius of imaginary circle
		final int r = getHeight();

		float scaleRate = prepareMatrix(mMatrix, distanceY, r);

		Bitmap bitmap = getChildDrawingCache(child, scaleRate);
		mMatrix.preTranslate(-childCenterX, -childCenterY);
		mMatrix.postTranslate(childCenterX, childCenterY);
		mMatrix.postTranslate(left, top);
		int scaledWidth = (int)(bitmap.getWidth() * scaleRate);
		if(scaledWidth < 1){
			scaledWidth = 1;
		}
		int scaledHeight = (int)(bitmap.getHeight() * scaleRate);
		if(scaledHeight <1){
			scaledHeight = 1;
		}
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
				scaledWidth, scaledHeight,
				true);
		canvas.drawBitmap(scaledBitmap, mMatrix, mPaint);
//		canvas.drawBitmap(bitmap, mMatrix, mPaint);
		return false;
	}

	private float prepareMatrix(final Matrix outMatrix, int distanceY, int r) {
		// clip the distance
		final int d = Math.min(r, Math.abs(distanceY));
		// use circle formula
		final float translateZ = (float) ((r - Math.sqrt((r * r) - (d * d))));
		
		
		
		if (maxTranslateXZ == 0) {
			maxTranslateXZ = translateZ + 1;
		}
		// (maxTranslateXZ - translateZ)/maxTranslateXZ = x/255;
		int alpha = (int) ((int) (maxTranslateXZ - translateZ - 0.3) * maxAlpha / maxTranslateXZ);
		if (alpha < minAlpha) {
			alpha = minAlpha;
		}
		Log.i(TAG, "Alpha is " + alpha);
		Log.i(TAG, "translateZ is " + translateZ);
		Log.i(TAG, "maxTranslateXZ is " + maxTranslateXZ);
		mCamera.save();
		mCamera.translate(maxTranslateXZ - translateZ, 0,
				-(maxTranslateXZ - translateZ));
//		mCamera.translate(maxTranslateXZ - translateZ, 0, 0);

		mCamera.getMatrix(outMatrix);
		mCamera.restore();

		float fontSize = 12;
		fontSize = fontSize
				* (1 - ((maxTranslateXZ - translateZ) / maxTranslateXZ));
		Log.i(TAG, "font size is " + fontSize);
		float scaleRate = (1 - ((maxTranslateXZ - translateZ) / maxTranslateXZ))/2;
		mPaint.setColor(Color.argb(alpha, 0, 0, 0));
		return (1-scaleRate);
	}
	private int listitemHeight = 0;
	private Bitmap getChildDrawingCache(final View child, float scaleRate) {
//		Bitmap bitmap = null;
		Bitmap bitmap = child.getDrawingCache();
		if (bitmap == null) {
			int paddingTopButtom = (int)(10*scaleRate);
//			Log.d(TAG, "paddingTopButtom: "+paddingTopButtom);
//		if(listitemHeight == 0){
//			listitemHeight = child.getHeight();
//		}
//			Log.d(TAG, "list item height: "+listitemHeight);
			
//			child.setPadding(0, paddingTopButtom, 0, 0);
//			int newHeight = (int)(scaleRate*listitemHeight) + 2 * paddingTopButtom;
//			int newWidth = child.getWidth();
			
//			Log.d(TAG, "list item size: "+newHeight + " : "+newWidth);
//			child.setLayoutParams(new LayoutParams(newWidth, newHeight));
			child.setDrawingCacheEnabled(true);
			child.buildDrawingCache();
			bitmap = child.getDrawingCache();
		}
		return bitmap;
	}

}
