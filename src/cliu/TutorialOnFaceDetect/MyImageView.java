package cliu.TutorialOnFaceDetect;

/*
 * MyImageView.java
 * 
 * [AUTHOR]: Chunyen Liu
 * [SDK   ]: Android SDK 2.1 and up
 * [NOTE  ]: developer.com tutorial, "Face Detection with Android APIs"
 */

import java.util.Iterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

class MyImageView extends ImageView {
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private int mBitmapWidth = 200;
	private int mBitmapHeight = 200;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int mDisplayStyle = 0;
	private int[] mPX = null;
	private int[] mPY = null;
	private Rect[] faceRects;

	public MyImageView(Context c) {
		super(c);
		init();
	}

	public MyImageView(Context c, AttributeSet attrs) {
		super(c, attrs);
		init();
	}

	private void init() {
		mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.RGB_565);
		mCanvas = new Canvas(mBitmap);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setColor(0x80ff0000);
		mPaint.setStrokeWidth(3);
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (bm != null) {
			mBitmapWidth = bm.getWidth();
			mBitmapHeight = bm.getHeight();

			mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.RGB_565);
			mCanvas = new Canvas();
			mCanvas.setBitmap(mBitmap);
			mCanvas.drawBitmap(bm, 0, 0, null);
		}

		super.setImageBitmap(bm);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mBitmapWidth = (mBitmap != null) ? mBitmap.getWidth() : 0;
		mBitmapHeight = (mBitmap != null) ? mBitmap.getHeight() : 0;
		if (mBitmapWidth == w && mBitmapHeight == h) {
			return;
		}

		if (mBitmapWidth < w)
			mBitmapWidth = w;
		if (mBitmapHeight < h)
			mBitmapHeight = h;
	}

	// set up detected face features for display
	public void setDisplayPoints(int[] xx, int[] yy, int total, int style) {
		mDisplayStyle = style;
		mPX = null;
		mPY = null;

		if (xx != null && yy != null && total > 0) {
			mPX = new int[total];
			mPY = new int[total];

			for (int i = 0; i < total; i++) {
				mPX[i] = xx[i];
				mPY[i] = yy[i];
			}
		}
	}

	public Rect[] getFaceRects() {
		return faceRects;
	}

	public void setFaceRects(Rect[] faceRects, int style) {
		this.faceRects = faceRects;
		mDisplayStyle = style;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
			if (mDisplayStyle == 2) {
				if (faceRects != null) {
					if (faceRects.length > 0) {
						for (Rect rect : faceRects) {
							canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, mPaint);
						}
					}
				}
			} else {
				if (mPX != null && mPY != null) {
					for (int i = 0; i < mPX.length; i++) {
						if (mDisplayStyle == 1) {
							canvas.drawCircle(mPX[i], mPY[i], 10.0f, mPaint);
						} else {
							canvas.drawRect(mPX[i] - 20, mPY[i] - 20, mPX[i] + 20, mPY[i] + 20, mPaint);
						}
					}
				}
			}
		}
	}
}