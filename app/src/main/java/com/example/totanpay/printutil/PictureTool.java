package com.example.totanpay.printutil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.urovo.sdk.utils.Funs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class PictureTool {
	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {

		if (base64Data != null) {
			byte[] bytes = null;
			try {
				bytes = Base64.decode(base64Data, Base64.DEFAULT);
			} catch (Exception e) {

			}
			if (bytes != null) {
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			} else
				return null;
		} else {
			return null;
		}
	}

	/**
	 * 获取压缩后的图片
	 * @param resPath 处理后的图片存储位置
	 * @param reqWidth 需要的宽度
	 * @param reqHeight 需要的高度
	 * @return
	 */
	public static Bitmap getSuitBitmat(String resPath, int reqWidth,int reqHeight) {
		if (resPath == null) {
			return null;
		}
		File file = new File(resPath);
		if (file.exists()) {
			// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			//			Matrix matrix=new Matrix();
			//			matrix.postRotate(-90);

			BitmapFactory.decodeFile(resPath, options);
			// 通过calculateInSampleSize计算出一个合适的压缩率
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			// 使用获取到的inSampleSize值再次解析图片
			options.inJustDecodeBounds = false;


			return BitmapFactory.decodeFile(resPath, options);
			//					Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), 
			//					bitmap.getWidth(), matrix, true);
		}
		return null;
	}

	/**
	 * @param options 
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
//
//	/**
//	 * 获取配置参数 String
//	 * 
//	 * @return
//	 */
//	public static String getStringPerferences(Context context, String name,
//			String defValues) {
//		SharedPreferences preferences = context.getSharedPreferences(name,
//				Context.MODE_PRIVATE);
//		synchronized (preferences) {
//
//			return preferences.getString(name, defValues);
//		}
//	}
//	/*
//	 * String 设置配置参数
//	 */
//	public static void setStringsave(Context context, String name, String values) {
//		SharedPreferences preferences = context.getSharedPreferences(name,
//				Context.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		editor.putString(name, values);
//		boolean b = editor.commit();
//	}

	/**
	 * 改变一组开关状态 
	 */
	/*public static void changState(int[] asy,int state,Context context){
		for (int i = 0; i < asy.length; i++) {
			TmsDealManage.updateByDealCode(asy[i], state, context);
		}
	}*/
	
	public static String SetStrTlv(String tag, String value) {
		try {
			byte[] srcbytes = value.getBytes("GBK");
			long l = srcbytes.length;
			if (l == 0)
				return "";
			String tempstrvalue = "";// 长度
			String strlen = String.format(Locale.ENGLISH, "%02X", l);
			byte[] dstbytes = new byte[(int) (l * 2)];
			Funs.BcdToAsc(dstbytes, srcbytes, dstbytes.length);
			tempstrvalue = new String(dstbytes);
			return tag + strlen + tempstrvalue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String SetNTlv(String tag, String value) {
		int l = (value.length() + 1) / 2;
		if (l == 0)
			return "";
		String strlen = String.format(Locale.ENGLISH, "%02X", l);
		if (value.length() % 2 != 0)
			value += "F";
		return tag + strlen + value;
	}
	

	//111 1260133144008200358    154008200358    164008200358    177             188191120921122�°�����ɷ����޹�˾�°�                23124125126��

	//16进账转2进制
	public static String hexString2binaryString(String hexString)  
    {  
        if (hexString == null || hexString.length() % 2 != 0)  
            return null;  
        String bString = "", tmp;  
        for (int i = 0; i < hexString.length(); i++)  
        {  
            tmp = "0000"  
                    + Integer.toBinaryString(Integer.parseInt(hexString  
                            .substring(i, i + 1), 16));  
            bString += tmp.substring(tmp.length() - 4);  
        }  
        return bString;  
    }  
	
	
	
	
}
