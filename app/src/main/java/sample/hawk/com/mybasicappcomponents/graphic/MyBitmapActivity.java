package sample.hawk.com.mybasicappcomponents.graphic;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.FileNotFoundException;

import sample.hawk.com.mybasicappcomponents.R;
import sample.hawk.com.mybasicappcomponents.data_structure.cache.basic.FileCache;
import sample.hawk.com.mybasicappcomponents.graphic.utils.BFeedBlurBuilder;
import sample.hawk.com.mybasicappcomponents.graphic.utils.BitmapUtils;
import sample.hawk.com.mybasicappcomponents.graphic.utils.BlurBuilder;
import sample.hawk.com.mybasicappcomponents.graphic.utils.ImageUtils;
import sample.hawk.com.mybasicappcomponents.utils.SMLog;

import static sample.hawk.com.mybasicappcomponents.graphic.utils.DrawableUtils.drawableToBitmap;


/**
 * Created by ha271 on 2017/8/28.
 */

// public class MyBitmapActivity extends Activity implements View.OnTouchListener, View.OnDragListener {
public class MyBitmapActivity extends Activity {
    private static final String LOG_TAG = MyBitmapActivity.class.getSimpleName();
    Context mContext;
    static DisplayMetrics mDisplayMetrics;
    ImageView mImageView_org;
    ImageView mImageView_blur;
    Drawable mDrawable_org;
    Drawable mDrawable_blur;
    Uri source;
    Bitmap mBitmap_org;
    Bitmap mBitmap_blur;
    Drawable drawable_out = null;
    final int GET_IMAGE = 1;
    int pixelShift = 300;

    public  class BlurRangeSeekBar_Listener implements SeekBar.OnSeekBarChangeListener {

        public BlurRangeSeekBar_Listener(Activity activity) {
        }

        public final void onProgressChanged(SeekBar seekBar, int progress, boolean z) {
            float fWallpaperAlpha = progress * 2.55f;
            int test_color = 0xBF010203;
            test_color = ColorAlphaTransition(test_color, fWallpaperAlpha);
            SMLog.i("New color="+ test_color);
        }

        public final void onStartTrackingTouch(SeekBar seekBar) {
        }

        public final void onStopTrackingTouch(SeekBar seekBar) {
        }

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybitmapactivity);
        mContext = this;
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        SMLog.i("StatusBarHeight="+ ImageUtils.getStatusBarHeight(this));
        mImageView_blur = (ImageView) findViewById(R.id.imageViewBlurred);
        mImageView_org = (ImageView) findViewById(R.id.imageViewOrigin);
        SeekBar blur_pixelrange_seekbar = (SeekBar) findViewById(R.id.bitmap_seekbar);
        int width =  mDisplayMetrics.widthPixels;
        blur_pixelrange_seekbar.setMax(width);
        blur_pixelrange_seekbar.setProgress(pixelShift);
        blur_pixelrange_seekbar.setOnSeekBarChangeListener(new BlurRangeSeekBar_Listener(this ));

        ImageUtils.getImageFromAlbum(this, GET_IMAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_IMAGE:
                    source = data.getData();
                    try {
                        mBitmap_org = BitmapFactory
                                .decodeStream(getContentResolver().openInputStream( source));
                        mDrawable_org= new BitmapDrawable(mContext.getResources(), mBitmap_org);
                        BlurBuilder.asyncBlur(mContext, mBitmap_org, new BlurBuilder.AsyncResponse() {
                            @Override
                            public void processFinish(Bitmap processedbmp) { // AsyncBlur will scale down the bitmap for processing quickly, so the width and height is small.
                                mBitmap_blur = Bitmap.createBitmap(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, Bitmap.Config.ARGB_8888);
                                mBitmap_blur = BitmapUtils.scaleBitmap( processedbmp, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
                                mDrawable_blur = new BitmapDrawable(getResources(), mBitmap_blur);
                                mImageView_org.setBackground(mDrawable_blur);
                            }
                        });

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    public void onClickMyBitmapActivityButtons(View view){
        String Tag = view.getTag().toString();
        int tag = Integer.parseInt(Tag);
        long start_time = SystemClock.uptimeMillis();

        switch(tag) {
            case 0:
                // TODO: Add bitmap test case here

                break;

            case 1: // overlapBitmap
                drawable_out = BitmapUtils.overlapBitmap(mContext, mBitmap_org, BitmapUtils.cropBitmap(mBitmap_blur,pixelShift) , pixelShift);
                break;

            case 2:
                // TODO: Add bitmap test case here
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
                Drawable drawable = wallpaperManager.getDrawable();
                SMLog.i("Intrinsic  Width="+drawable.getIntrinsicWidth()+"  Height="+drawable.getIntrinsicHeight());
                SMLog.i("Minimum    Width="+drawable.getMinimumWidth()+"  Height="+drawable.getMinimumHeight());
                Bitmap OriginWallpaperBmp = drawableToBitmap(drawable);
                FileCache mFileCache = new FileCache(mContext);
                if( mFileCache == null ) { // path at /storage/emulated/0/Android/data/sample.hawk.com.mybasicappcomponents/cache
                    SMLog.i("No FileCache available!!");
                }
                mFileCache.put("OriginWallpaperBmp", OriginWallpaperBmp);

                BFeedBlurBuilder.setContext(mContext);
                Bitmap BFeedWallpaperBmp = BFeedBlurBuilder.getBackgroundBitmap(null);
                mFileCache.put("BFeedWallpaperBmp", BFeedWallpaperBmp);

                break;


        }
        long end_time = SystemClock.uptimeMillis();
        SMLog.i("Effect     TimeCost = " + (end_time - start_time));
        // mImageView_org.setImageDrawable(drawable_out);
    }

    // 0:full transparent, 1:opaque
    private static int ColorAlphaTransition(int color, float alpha) {
        return ((((int)((float)((color >> 24) & 0xFF) * alpha)) << 24)|(color & 0xFFFFFF));
    }

}
