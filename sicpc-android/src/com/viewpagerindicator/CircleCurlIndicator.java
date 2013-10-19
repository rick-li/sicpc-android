package com.viewpagerindicator;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

import com.sicpc.android.R;

public class CircleCurlIndicator extends View {

	private static final int INVALID_POINTER = -1;

	private float mRadius;
	private final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
	private final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
	private final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
	private int mCurrentPage;
	private int mSnapPage;
	private float mPageOffset;
	private int mOrientation;
	private boolean mCentered;
	private boolean mSnap;
	private int itemCount = 0;

	public CircleCurlIndicator(Context context) {
		this(context, null);
	}

	public CircleCurlIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
	}

	public CircleCurlIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;

		// Load defaults from resources
		final Resources res = getResources();
		final int defaultPageColor = res.getColor(R.color.circle_unselect);
		final int defaultFillColor = res.getColor(R.color.circle_selected);
		final int defaultOrientation = res
				.getInteger(R.integer.default_circle_indicator_orientation);
		final int defaultStrokeColor = res.getColor(R.color.circle_selected);
		final float defaultStrokeWidth = res
				.getDimension(R.dimen.default_circle_indicator_stroke_width);
		final float defaultRadius = res.getDimension(R.dimen.circle_radius);
		final boolean defaultCentered = res
				.getBoolean(R.bool.default_circle_indicator_centered);
		final boolean defaultSnap = res
				.getBoolean(R.bool.default_circle_indicator_snap);

		// Retrieve styles attributes
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CirclePageIndicator, defStyle, 0);

		mCentered = a.getBoolean(R.styleable.CirclePageIndicator_centered,
				defaultCentered);
		mOrientation = a.getInt(
				R.styleable.CirclePageIndicator_android_orientation,
				defaultOrientation);
		mPaintPageFill.setStyle(Style.FILL);
		mPaintPageFill.setColor(a.getColor(
				R.styleable.CirclePageIndicator_pageColor, defaultPageColor));
		mPaintStroke.setStyle(Style.STROKE);
		mPaintStroke.setColor(a
				.getColor(R.styleable.CirclePageIndicator_strokeColor,
						defaultStrokeColor));
		mPaintStroke.setStrokeWidth(a
				.getDimension(R.styleable.CirclePageIndicator_strokeWidth,
						defaultStrokeWidth));
		mPaintFill.setStyle(Style.FILL);
		mPaintFill.setColor(a.getColor(
				R.styleable.CirclePageIndicator_fillColor, defaultFillColor));
		mRadius = a.getDimension(R.styleable.CirclePageIndicator_radius,
				defaultRadius);
		mSnap = a.getBoolean(R.styleable.CirclePageIndicator_snap, defaultSnap);

		Drawable background = a
				.getDrawable(R.styleable.CirclePageIndicator_android_background);
		if (background != null) {
			setBackgroundDrawable(background);
		}

		a.recycle();

		final ViewConfiguration configuration = ViewConfiguration.get(context);
	}

	public void setCentered(boolean centered) {
		mCentered = centered;
		invalidate();
	}

	public boolean isCentered() {
		return mCentered;
	}

	public void setPageColor(int pageColor) {
		mPaintPageFill.setColor(pageColor);
		invalidate();
	}

	public int getPageColor() {
		return mPaintPageFill.getColor();
	}

	public void setFillColor(int fillColor) {
		mPaintFill.setColor(fillColor);
		invalidate();
	}

	public int getFillColor() {
		return mPaintFill.getColor();
	}

	public void setOrientation(int orientation) {
		switch (orientation) {
		case HORIZONTAL:
		case VERTICAL:
			mOrientation = orientation;
			requestLayout();
			break;

		default:
			throw new IllegalArgumentException(
					"Orientation must be either HORIZONTAL or VERTICAL.");
		}
	}

	public int getOrientation() {
		return mOrientation;
	}

	public void setStrokeColor(int strokeColor) {
		mPaintStroke.setColor(strokeColor);
		invalidate();
	}

	public int getStrokeColor() {
		return mPaintStroke.getColor();
	}

	public void setStrokeWidth(float strokeWidth) {
		mPaintStroke.setStrokeWidth(strokeWidth);
		invalidate();
	}

	public float getStrokeWidth() {
		return mPaintStroke.getStrokeWidth();
	}

	public void setRadius(float radius) {
		mRadius = radius;
		invalidate();
	}

	public float getRadius() {
		return mRadius;
	}

	public void setSnap(boolean snap) {
		mSnap = snap;
		invalidate();
	}

	public boolean isSnap() {
		return mSnap;
	}

	private Curler curler;

	public void setCurler(Curler curler) {
		this.curler = curler;
		this.curler.setCurlIndicator(this);
	}

	public void setItemCount(int count) {
		this.itemCount = count;
		this.invalidate();
	}

	public void setCurrentPage(int currentPage) {
		this.mCurrentPage = currentPage;
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final int count = itemCount;
		if (count == 0) {
			return;
		}

		if (mCurrentPage >= count) {
			// setCurrentItem(count - 1);
			return;
		}

		int longSize;
		int longPaddingBefore;
		int longPaddingAfter;
		int shortPaddingBefore;
		if (mOrientation == HORIZONTAL) {
			longSize = getWidth();
			longPaddingBefore = getPaddingLeft();
			longPaddingAfter = getPaddingRight();
			shortPaddingBefore = getPaddingTop();
		} else {
			longSize = getHeight();
			longPaddingBefore = getPaddingTop();
			longPaddingAfter = getPaddingBottom();
			shortPaddingBefore = getPaddingLeft();
		}

		final float threeRadius = mRadius * 3;
		final float shortOffset = shortPaddingBefore + mRadius;
		float longOffset = longPaddingBefore + mRadius;
		if (mCentered) {
			longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f)
					- ((count * threeRadius) / 2.0f);
		}

		float dX;
		float dY;

		float pageFillRadius = mRadius;
		if (mPaintStroke.getStrokeWidth() > 0) {
			pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
		}

		// Draw stroked circles
		for (int iLoop = 0; iLoop < count; iLoop++) {
			float drawLong = longOffset + (iLoop * threeRadius);
			if (mOrientation == HORIZONTAL) {
				dX = drawLong;
				dY = shortOffset;
			} else {
				dX = shortOffset;
				dY = drawLong;
			}
			// Only paint fill if not completely transparent
			if (mPaintPageFill.getAlpha() > 0) {
				canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);
			}

			// Only paint stroke if a stroke width was non-zero
			if (pageFillRadius != mRadius) {
				canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
			}
		}

		// Draw the filled circle according to the current scroll
		float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
		if (!mSnap) {
			cx += mPageOffset * threeRadius;
		}
		if (mOrientation == HORIZONTAL) {
			dX = longOffset + cx;
			dY = shortOffset;
		} else {
			dX = shortOffset;
			dY = longOffset + cx;
		}
		canvas.drawCircle(dX, dY, mRadius, mPaintFill);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mOrientation == HORIZONTAL) {
			setMeasuredDimension(measureLong(widthMeasureSpec),
					measureShort(heightMeasureSpec));
		} else {
			setMeasuredDimension(measureShort(widthMeasureSpec),
					measureLong(heightMeasureSpec));
		}
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureLong(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if ((specMode == MeasureSpec.EXACTLY)) {
			// We were told how big to be
			result = specSize;
		} else {
			// Calculate the width according the views count
			final int count = itemCount;
			result = (int) (getPaddingLeft() + getPaddingRight()
					+ (count * 2 * mRadius) + (count - 1) * mRadius + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureShort(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the height
			result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		mCurrentPage = savedState.currentPage;
		mSnapPage = savedState.currentPage;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPage = mCurrentPage;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPage;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPage = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPage);
		}

		@SuppressWarnings("UnusedDeclaration")
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
