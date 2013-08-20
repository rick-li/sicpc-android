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

	public CurvedList(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		Bitmap bitmap = getChildDrawingCache(child);
		// (top,left) is the pixel position of the child inside the list
		final int marginTop = 100;
		final int top = child.getTop();
		int left = child.getLeft();
		// center point of child
		final int childCenterY = child.getHeight() / 2 + marginTop;
		int childCenterX = child.getWidth() / 2;
		// center of list
		final int parentCenterY = getHeight() / 2 ;
		// center point of child relative to list
		final int absChildCenterY = child.getTop() + childCenterY;
		// distance of child center to the list center
		final int distanceY = parentCenterY - absChildCenterY;
		// radius of imaginary cirlce
		final int r = getHeight();

		prepareMatrix(mMatrix, distanceY, r);

		mMatrix.preTranslate(-childCenterX, -childCenterY);
		mMatrix.postTranslate(childCenterX, childCenterY);
		mMatrix.postTranslate(left, top);

		canvas.drawBitmap(bitmap, mMatrix, mPaint);
		return false;
	}

	private void prepareMatrix(final Matrix outMatrix, int distanceY, int r) {
		// clip the distance
		final int d = Math.min(r, Math.abs(distanceY));
		// use circle formula
		final float translateZ = (float) (r - Math.sqrt((r * r) - (d * d)));
		if (maxTranslateXZ == 0) {
			maxTranslateXZ = translateZ + 1;
		}
		// (maxTranslateXZ - translateZ)/maxTranslateXZ = x/255;
		int alpha = (int) ((int) (maxTranslateXZ - translateZ) * maxAlpha / maxTranslateXZ);
		if (alpha < minAlpha) {
			alpha = minAlpha;
		}
		Log.i(TAG, "Alpha is " + alpha);
		Log.i(TAG, "translateZ is " + translateZ);
		mCamera.save();
		mCamera.translate(maxTranslateXZ - translateZ, 0,
				-(maxTranslateXZ - translateZ));

		// mCamera.rotateY((float) degree);
		mCamera.getMatrix(outMatrix);
		mCamera.restore();

		mPaint.setColor(Color.argb(alpha, 0, 0, 0));
	}

	private Bitmap getChildDrawingCache(final View child) {
		Bitmap bitmap = child.getDrawingCache();
		if (bitmap == null) {
			child.setDrawingCacheEnabled(true);
			child.buildDrawingCache();
			bitmap = child.getDrawingCache();
		}
		return bitmap;
	}

}
