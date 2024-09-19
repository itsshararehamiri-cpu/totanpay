package com.example.totanpay.printutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.urovo.sdk.print.EncodingHandler;
import com.urovo.sdk.print.PrintFormat;

import java.io.File;
import java.util.List;

public class PaintView {

    public static final String TAG = "PaintView===>";

    private static Context mContext;
    private static Paint mPaint;
    private static String mFontNameLast;
    private static Typeface mTypefaceLast;
    private static Canvas cacheCanvas;
    private static Bitmap cachebBitmap;
    private static PaintView mPaintView;
    public static String fontName_default = "simsun";

    public static final int MAX_PAGEWIDTH = 380;
    public static final int DEF_FONT_SIZE_SMALL = 16;
    public static final int DEF_FONT_SIZE = 24;
    public static final int DEF_FONT_SIZE_BIG = 32;

    public static PaintView getInstance(Context context) {
        if (mPaintView == null) {
            mPaintView = new PaintView();
        }
        if (mContext == null) {
            mContext = context;
        }
        return mPaintView;
    }

    public void init(int height) {
        cachebBitmap = Bitmap.createBitmap(MAX_PAGEWIDTH, height, Config.ARGB_8888);//高清，650, 380
        cachebBitmap.eraseColor(0xffffffff);
        cacheCanvas = new Canvas(cachebBitmap);
        //cacheCanvas.drawColor(Color.parseColor("#ffffff"));
        cacheCanvas.save();
        cacheCanvas.restore();

        mPaint = new Paint();
        mPaint.reset();
        mPaint.setColor(Color.BLACK);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setUnderlineText(false);
        mPaint.setStrikeThruText(false);
    }

    public float drawText(PrintContentBean bean, int y) {
        String text = bean.getContent();
        int font = bean.getFont();
        int align = bean.getAlign();
        boolean isBold = bean.isBold();
        String fontName = bean.getFontName();
        float lineHeight = 0;
        int x = 0;
        if (align == PrintFormat.ALIGN_CENTER || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
            x = MAX_PAGEWIDTH / 2;
        } else if (align == PrintFormat.ALIGN_RIGHT) {
            x = MAX_PAGEWIDTH;
        }
        int fontSize = DEF_FONT_SIZE;
        if (font == PrintFormat.FONT_SMALL) {
            fontSize = DEF_FONT_SIZE_SMALL;
        } else if (font == PrintFormat.FONT_LARGE) {
            fontSize = DEF_FONT_SIZE_BIG;
        }
        if (bean.getFontSize() > 0) {
            fontSize = bean.getFontSize();
        }

        fontName = checkFontFileExist(fontName);
        Typeface typeface = null;
        if (TextUtils.equals(fontName, fontName_default)) {
            typeface = TypefaceHelper.getDefault(isBold, fontName);
        } else {
            typeface = TypefaceHelper.get(isBold, fontName);
        }

        mPaint.setTypeface(typeface);
        mPaint.setFakeBoldText(isBold);
        mPaint.setTextSize(fontSize);
        mPaint.setTextAlign(getAlign(align));
        //通过设置Flag来应用抗锯齿效果
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        cacheCanvas.drawText(text, x, y, mPaint);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        float fonthight = fm.bottom - fm.top;
        lineHeight = fonthight;
        if (fontSize < DEF_FONT_SIZE) {
            lineHeight += 5;
        } else if (fontSize >= DEF_FONT_SIZE_BIG) {
            lineHeight -= 5;
        }
        return lineHeight;
    }

    /**
     * 获取每一行的高度
     *
     * @param font
     * @return
     */
    public float getTextHeight(int font, boolean isBold) {
        float height = 0;
        int fontSize = DEF_FONT_SIZE;
        if (font == PrintFormat.FONT_SMALL) {
            fontSize = DEF_FONT_SIZE_SMALL;
        } else if (font == PrintFormat.FONT_LARGE) {
            fontSize = DEF_FONT_SIZE_BIG;
        }
        String fontName = fontName_default;
        fontName = checkFontFileExist(fontName);
        Typeface typeface = null;
        if (TextUtils.equals(fontName, fontName_default)) {
            typeface = TypefaceHelper.getDefault(isBold, fontName);
        } else {
            typeface = TypefaceHelper.get(isBold, fontName);
        }

        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setFakeBoldText(isBold);
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        height = fm.bottom - fm.top;
        if (font == PrintFormat.FONT_LARGE) {
            //    height+=5;
        }
        return height;
    }

    /**
     * 获取每一行的高度
     *
     * @param bean
     * @return
     */
    public float getTextHeight(PrintContentBean bean) {
        String text = bean.getContent();
        int font = bean.getFont();
        int align = bean.getAlign();
        boolean isBold = bean.isBold();
        String fontName = bean.getFontName();
        float lineHeight = 0;
        int fontSize = DEF_FONT_SIZE;
        if (font == PrintFormat.FONT_SMALL) {
            fontSize = DEF_FONT_SIZE_SMALL;
        } else if (font == PrintFormat.FONT_LARGE) {
            fontSize = DEF_FONT_SIZE_BIG;
        }
        if (bean.getFontSize() > 0) {
            fontSize = bean.getFontSize();
        }
        fontName = checkFontFileExist(fontName);
        Typeface typeface = null;
        //第三方应用无法访问系统字库文件，所以直接默认Typeface且不保存此次typeface。
        if (TextUtils.equals(fontName, fontName_default)) {
            typeface = TypefaceHelper.getDefault(isBold, fontName);
        } else {
            typeface = TypefaceHelper.get(isBold, fontName);
        }

        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setFakeBoldText(isBold);
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        lineHeight = fm.bottom - fm.top;
        if (font == PrintFormat.FONT_LARGE) {
            //    lineHeight+=5;
        }
        if (align == PrintFormat.ALIGN_LEFT_RIGHT || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
            return 0;
        }
        if (fontSize < DEF_FONT_SIZE) {
            lineHeight += 5;
        } else if (fontSize >= DEF_FONT_SIZE_BIG) {
            lineHeight -= 5;
        }
        return lineHeight;
    }

    /**
     * 获取每一行的高度:只用于drawPrintBitmap
     *
     * @param bean
     * @return
     */
    private float getTextHeight_single(PrintContentBean bean) {
        int font = bean.getFont();
        int align = bean.getAlign();
        boolean isBold = bean.isBold();
        String fontName = bean.getFontName();
        float lineHeight = 0;
        int fontSize = DEF_FONT_SIZE;
        if (font == PrintFormat.FONT_SMALL) {
            fontSize = DEF_FONT_SIZE_SMALL;
        } else if (font == PrintFormat.FONT_LARGE) {
            fontSize = DEF_FONT_SIZE_BIG;
        }
        if (bean.getFontSize() > 0) {
            fontSize = bean.getFontSize();
        }
        fontName = checkFontFileExist(fontName);
        Typeface typeface = null;
        if (TextUtils.equals(fontName, fontName_default)) {
            typeface = TypefaceHelper.getDefault(isBold, fontName);
        } else {
            typeface = TypefaceHelper.get(isBold, fontName);
        }

        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setFakeBoldText(isBold);
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        lineHeight = fm.bottom - fm.top;
        return lineHeight;
    }

    public int getBitmapHeight(List<PrintContentBean> contentBeanList) {
        if (contentBeanList == null || contentBeanList.size() == 0) {
            return 0;
        }
        int paintY = 0;
        PrintContentBean printContentBean = null;
        for (int i = 0; i < contentBeanList.size(); i++) {
            printContentBean = contentBeanList.get(i);
            if (printContentBean == null) {
                continue;
            }
            if (i == 0) {
                if (printContentBean.getPrintType() == PrintContentBean.PrintType_Text) {
                    paintY = (int) getTextHeight_single(printContentBean);
                } else if (printContentBean.getPrintType() == PrintContentBean.PrintType_BLANK) {
                    paintY = printContentBean.getHeight();
                }
            }
            int align = printContentBean.getAlign();
            switch (printContentBean.getPrintType()) {
                case PrintContentBean.PrintType_BarCode:
                case PrintContentBean.PrintType_QRCode:
                case PrintContentBean.PrintType_Bitmap:
                    paintY += printContentBean.getHeight() + 5;
                    break;
                case PrintContentBean.PrintType_BLANK:
                    paintY += printContentBean.getHeight();
                    break;
                default:
                    if (align == PrintFormat.ALIGN_LEFT_RIGHT || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
                        paintY += 0;
                    } else {
                        paintY += getTextHeight(printContentBean);
                    }
                    break;
            }
        }

        Log.e(TAG, "getBitmapHeight:paintY=" + paintY);
        return paintY;
    }

    /**
     * 格绘制打印图片
     *
     * @param contentBeanList
     * @param height
     * @return
     */
    public Bitmap drawPrintBitmap(List<PrintContentBean> contentBeanList, int height) {
        if (contentBeanList == null || contentBeanList.size() == 0) {
            return null;
        }
        init(height);
        int paintY = 0;
        PrintContentBean printContentBean = null;
        for (int i = 0; i < contentBeanList.size(); i++) {
            printContentBean = contentBeanList.get(i);
            if (printContentBean == null) {
                continue;
            }
            //Canvans.drawText方法的坐标是以文字基准线为准，不是左上角，所以当你的坐标为0，是看不见的，起始坐标y要有一定的深度。
            if (i == 0) {
                if (printContentBean.getPrintType() == PrintContentBean.PrintType_Text) {
                    paintY = (int) getTextHeight_single(printContentBean);
                } else if (printContentBean.getPrintType() == PrintContentBean.PrintType_BLANK) {
                    paintY = printContentBean.getHeight();
                }
            }
            int align = printContentBean.getAlign();
            switch (printContentBean.getPrintType()) {
                case PrintContentBean.PrintType_BarCode:
                    paintY += drawBitmap(printContentBean, getBarCodeBitmap(printContentBean.getContent(), printContentBean.getWidth(), printContentBean.getHeight()),
                            printContentBean.getOffset(), paintY, align) + 5;
                    break;
                case PrintContentBean.PrintType_QRCode:
                    paintY += drawBitmap(printContentBean, getQRCodeBitmap(printContentBean.getContent(), printContentBean.getWidth(), printContentBean.getHeight()),
                            printContentBean.getOffset(), paintY, align) + 5;
                    break;
                case PrintContentBean.PrintType_Bitmap:
                    paintY += drawBitmap(printContentBean, printContentBean.getBitmap(), printContentBean.getOffset(), paintY, align) + 5;
                    break;
                case PrintContentBean.PrintType_BLANK:
                    paintY += printContentBean.getHeight();
                    break;
                default:
                    if (align == PrintFormat.ALIGN_LEFT_RIGHT || align == PrintFormat.ALIGN_LEFT_RIGHT_CENTER) {
                        drawText(printContentBean, paintY);
                    } else {
                        paintY += drawText(printContentBean, paintY);
                    }
                    break;
            }
        }
        Log.e(TAG, "drawPrintBitmap:paintY=" + paintY);
        Bitmap mBitmap = getBitmap();
        //保存图片 测试验证
        String path = SignFiles.createSignFile(mBitmap);
        return mBitmap;
    }

    public float drawBitmap(PrintContentBean bean, Bitmap bitmap, int offset, int y, int align) {
        Log.e(TAG, "drawBitmap:" + bitmap);
        if (bitmap == null) {
            Log.e(TAG, "drawBitmap bitmap is NULL");
            return 0;
        }
        float height = 40;
        if (offset <= 0) {
            offset = 0;
            if (align == PrintFormat.ALIGN_LEFT) {
                offset = 0;
            } else if (align == PrintFormat.ALIGN_CENTER) {
                offset = (MAX_PAGEWIDTH - bitmap.getWidth()) / 2 - 8;
            } else if (align == PrintFormat.ALIGN_RIGHT) {
                offset = MAX_PAGEWIDTH - bitmap.getWidth();
            }
        }
        mPaint.setTextAlign(getAlign(align));
        cacheCanvas.drawBitmap(bitmap, offset, y, mPaint);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bean.getHeight();
    }

    private int getFontHight(String text, int fontSize) {
        // FontMetrics对象
        Paint pFont = new Paint();
        pFont.setTextSize(fontSize);
        Paint.FontMetrics fontMetrics = pFont.getFontMetrics();
        int fontHeight = (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
        return fontHeight;
    }

    public Rect getTextSize(String text, int fontSize) {
        Paint pFont = new Paint();
        pFont.setTextSize(fontSize);
        Rect rect = new Rect();
        pFont.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    public Align getAlign(int align) {
        if (align == 1 || align == 4) {
            return Align.CENTER;
        } else if (align == 2) {
            return Align.RIGHT;
        } else {
            return Align.LEFT;
        }
    }

    public Bitmap getBitmap() {
        return cachebBitmap;
    }

    public void close() {
        if (cachebBitmap != null) {
            cachebBitmap.recycle();
            cachebBitmap = null;
        }
        cacheCanvas = null;
        mPaint = null;
        mPaintView = null;
    }

    public String checkFontFileExist(String fontName) {
        File file = null;
        if (TextUtils.isEmpty(fontName)) {
            fontName = fontName_default;
            return fontName;
        }
//        if (TextUtils.equals(fontName, mFontNameLast)) {
//            return fontName;
//        }
        try {
            file = new File(fontName);
            if (!file.exists()) {
                fontName = fontName_default;
            }
        } catch (Exception e) {
            e.printStackTrace();
            fontName = fontName_default;
        }
        return fontName;
    }

    private Paint getPaintCache(int size, boolean bold, boolean fontItalic, String fontName) {
        //如果当前fontName和上一个一样，则不需要再重新生成Paint
        mPaint.setTextSize(size);
        mPaint.setFakeBoldText(bold);
        Typeface typeface = null;
        try {
            //第三方应用无法访问系统字库文件，所以直接默认Typeface且不保存此次typeface。
            if (TextUtils.equals(fontName, fontName_default)) {
                typeface = TypefaceHelper.getDefault(bold, fontName);
            } else {
                typeface = TypefaceHelper.get(bold, fontName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            typeface = TypefaceHelper.getDefault(bold, fontName);
        }

        mPaint.setTypeface(typeface);
        return mPaint;
    }

    public Bitmap getQRCodeBitmap(String qrCode, int offset, int height) {
        if (offset > 55) {
            offset -= 55;
        }
        int calHeight = height - offset * 2;
        if (height > MAX_PAGEWIDTH) {
            height = MAX_PAGEWIDTH;
        }
        try {
            if (qrCode != null && qrCode.length() != 0) {
                Bitmap qrCodeBmp = EncodingHandler.createQRImage(qrCode, height, height);
                return qrCodeBmp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getBarCodeBitmap(String barCode, int width, int height) {
        if (height > MAX_PAGEWIDTH) {
            height = MAX_PAGEWIDTH;
        }
        try {
            if (barCode != null && barCode.length() != 0) {
                Bitmap bitmap = EncodingHandler.creatBarcode(barCode, 1, height,
                        false, 1, BarcodeFormat.CODE_128);
                // 获得图片的宽高
                int sourceWidth = bitmap.getWidth();
                int sourceHeight = bitmap.getHeight();
                if (height > 0 && width > 0) {
                    // 计算缩放比
                    float scaleWidth = ((float) width) / sourceWidth;
                    float scaleHeight = ((float) height) / sourceHeight;
                    // 取得想要缩放的matrix参数
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
                    // 得到新的图片
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void drawTextRect(float left, float top, float right, float bottom) {
        int x = 0;
        Paint paint = new Paint();
        // 设置样式-空心矩形
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        cacheCanvas.drawRect(left, top, right, bottom, paint);
    }

}
