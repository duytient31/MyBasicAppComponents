package sample.hawk.com.mybasicappcomponents.graphic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.os.Bundle;
import android.view.View;

import sample.hawk.com.mybasicappcomponents.graphic.utils.ImageUtils;

/**
 * Created by ha271 on 2017/9/18.
 */

public class MysetXfermode extends Activity {
    static Point ptScreenSize;
    // create a bitmap with a circle, used for the "dst" image
    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w*3/4, h*3/4), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(w/3, h/3, w*19/20, h*19/20, p);
        return bm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ptScreenSize = ImageUtils.getDisplaySize(this, true, true);
        setContentView(new SampleView(this));
    }

    private static class SampleView extends View {
        private static final int space = 40;
        private static final int ROW_MAX = 4;   // number of samples per row
        private static final int W = (ptScreenSize.x - space) / ROW_MAX;
        private static final int H = (ptScreenSize.x - space) / ROW_MAX;

        private Bitmap mSrcBmp;
        private Bitmap mDstBmp;
        private Shader mBkgBmp;     // background checker-board pattern

        private static final Xfermode[] sModes = {
                new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
                new PorterDuffXfermode(PorterDuff.Mode.SRC),
                new PorterDuffXfermode(PorterDuff.Mode.DST),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
                new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
                new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
                new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
                new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
                new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
                new PorterDuffXfermode(PorterDuff.Mode.XOR),
                new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
                new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
                new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
                new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
        };

        private static final String[] sLabels = {
                "Clear", "Src", "Dst", "SrcOver",
                "DstOver", "SrcIn", "DstIn", "SrcOut",
                "DstOut", "SrcATop", "DstATop", "Xor",
                "Darken", "Lighten", "Multiply", "Screen"
        };

        public SampleView(Context context) {
            super(context);

            mSrcBmp = makeSrc(W, H);
            mDstBmp = makeDst(W, H);

            // make a ckeckerboard pattern
            Bitmap bm = Bitmap.createBitmap(new int[] { 0xFFFFFFFF, 0xFFCCCCCC, 0xFFCCCCCC, 0xFFFFFFFF }, 2, 2, Bitmap.Config.RGB_565);
            mBkgBmp = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            Matrix m = new Matrix();
            m.setScale(6, 6);
            mBkgBmp.setLocalMatrix(m);
        }
        private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        @Override protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE); // activity background color

            // labelP for label paint
            Paint labelP = new Paint(Paint.ANTI_ALIAS_FLAG);
            labelP.setTextAlign(Paint.Align.CENTER);
            labelP.setTextSize(60);

            // paint for color paint
            Paint paint = new Paint();
            paint.setFilterBitmap(false);

            canvas.translate(15, 150); // 設定繪製canvas的X,Y座標起始位置(0,0)其相對位置位於(15,150)

            int x = 0;
            int y = 0;
            for (int i = 0; i < sModes.length; i++) {
                // draw the border - 劃出外框區域
                paint.setStyle(Paint.Style.STROKE);
                paint.setShader(null);
                canvas.drawRect(x - 0.5f, y - 0.5f, x + W + 0.5f, y + H + 0.5f, paint);

                // draw the checker-board pattern - 填入背景
                paint.setStyle(Paint.Style.FILL);
                paint.setShader(mBkgBmp);
                canvas.drawRect(x, y, x + W, y + H, paint);

                // draw the src/dst example into our offscreen bitmap - 設定繪製圖層 範圍
                int saveLayerCount = canvas.saveLayer(x, y, x + W, y + H, null, LAYER_FLAGS ); // saveLayerCount++

                // 設定繪製canvas的X,Y座標起始位置(0,0)
                canvas.translate(x, y);
                canvas.drawBitmap(mDstBmp, 0, 0, paint); // 繪製 圓形 完成
                paint.setXfermode(sModes[i]); // 改變繪製模式
                canvas.drawBitmap(mSrcBmp, 0, 0, paint); // 繪製 方形 完成
                paint.setXfermode(null); // 回到預設繪製模式
                canvas.restoreToCount(saveLayerCount); // saveLayerCount--;

                // 在上方寫出 繪製模式 設定字串
                canvas.drawText(sLabels[i], x + W/2, y - labelP.getTextSize()/2, labelP);

                x += W + 10; // 下次繪製起始

                // wrap around when we've drawn enough for one row - 換行繪製
                if ((i % ROW_MAX) == ROW_MAX - 1) {
                    x = 0;
                    y += H + 140;
                }
            }
        }
    }
}
