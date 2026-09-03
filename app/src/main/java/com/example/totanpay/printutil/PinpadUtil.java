package com.example.totanpay.printutil;

import android.util.Log;

import com.urovo.sdk.pinpad.PinPadProviderImpl;
import com.urovo.sdk.pinpad.utils.Constant;
import com.urovo.sdk.utils.BytesUtil;
import com.urovo.sdk.utils.Funs;

public class PinpadUtil {

    private static final String TAG = "PinpadUtil";

    public static String getPinData(int keyIndex, String Pan, byte[] data) {

        String pinBlock = "";
        try {
            int iRet = PinPadProviderImpl.getInstance().calculateDes(Constant.DesMode.DEC, Constant.Algorithm.DES_ECB, Constant.KeyType.PIN_KEY, keyIndex, data, data);
            Log.e(TAG, "calculateDes:" + iRet);
            if (iRet != 0) {
                return pinBlock;
            }
            Log.e(TAG, "pinBlock:" + Funs.bytesToHexString(data));

            String panStr = Pan.substring(0, Pan.length() - 1);
            panStr = panStr.substring(panStr.length() - 12);
            panStr = "0000" + panStr;
            Log.e(TAG, "panStr:" + panStr);
            byte[] panBuff2 = Funs.StrToHexByte(panStr);

            do_xor_urovo(panBuff2, data, 8);
            pinBlock = Funs.bytesToHexString(panBuff2);
            int pinLen = Integer.parseInt(pinBlock.substring(0, 2), 16);
            pinBlock = pinBlock.substring(2, 2 + pinLen);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlock;
    }

    public static String getPinData_SM4(int keyIndex, String Pan, byte[] data) {

        String pinBlock = "";
        try {
            int iRet = PinPadProviderImpl.getInstance().calculateDes(Constant.DesMode.DEC, Constant.Algorithm.SM4, Constant.KeyType.PIN_KEY, keyIndex, data, data);
            if (iRet != 0) {
                return pinBlock;
            }
            String panStr = Pan.substring(0, Pan.length() - 1);
            panStr = panStr.substring(panStr.length() - 12);
            panStr = BytesUtil.FormatWithZero(panStr, "00000000000000000000000000000000");

            Log.e(TAG, "panStr:" + panStr);
            byte[] panBuff2 = BytesUtil.hexString2Bytes(panStr);

            do_xor_urovo(panBuff2, data, 16);

            Log.e(TAG, "pinBlock 2:" + BytesUtil.bytes2HexString(panBuff2));
            pinBlock = BytesUtil.bytes2HexString(panBuff2);
            int pinLen = Integer.parseInt(pinBlock.substring(0, 2), 16);
            Log.e(TAG, "Pin length:" + pinLen);
            pinBlock = pinBlock.substring(2, 2 + pinLen);

            Log.e(TAG, "clear pin:" + pinBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlock;
    }

    public static String getPinData_AES(int keyIndex, String Pan, byte[] data) {
        Log.e(TAG, "getPinData_AES--------->");
        String pinBlock = "";
        try {
            Log.e(TAG, "Enciphered PIN Block:" + BytesUtil.bytes2HexString(data));
            int iRet = PinPadProviderImpl.getInstance().calculateDes(Constant.DesMode.DEC, Constant.Algorithm.AES_ECB, Constant.KeyType.PIN_KEY, keyIndex, data, data);
            Log.e(TAG, "decryptData:" + iRet);
            Log.e(TAG, "Intermediate Block B:" + BytesUtil.bytes2HexString(data));

            String panStr = BytesUtil.FormatWithZero(Pan, "00000000000000000000000000000000");
            int panLen = Pan.length();
            int panC = panLen - 12;
            panStr = panC + panStr;

            Log.e(TAG, "Plain text PAN Field:" + panStr);

            byte[] panBuff2 = BytesUtil.hexString2Bytes(panStr);

            do_xor_urovo(panBuff2, data, 16);

            Log.e(TAG, "Intermediate Block A:" + BytesUtil.bytes2HexString(panBuff2));

            iRet = PinPadProviderImpl.getInstance().calculateDes(Constant.DesMode.DEC, Constant.Algorithm.AES_ECB, Constant.KeyType.PIN_KEY, keyIndex, panBuff2, panBuff2);
            Log.e(TAG, "decryptData:" + iRet);

            pinBlock = BytesUtil.bytes2HexString(panBuff2);
            Log.e(TAG, "Plain text PIN Field:" + pinBlock);

            int pinLen = Integer.parseInt(pinBlock.substring(1, 2), 16);
            Log.e(TAG, "Pin length:" + pinLen + "");
            pinBlock = pinBlock.substring(2, 2 + pinLen);

            Log.e(TAG, "clear pin:" + pinBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlock;
    }

    public static String getPinDataDUKPT_AES(String key, String Pan, byte[] data) {
        Log.e(TAG, "getPinData_AES--------->");
        String pinBlock = "";
        try {
            Log.e(TAG, "Enciphered PIN Block:" + BytesUtil.bytes2HexString(data));

            data = AESUtil.decrypt(data, key);

            Log.e(TAG, "Intermediate Block B:" + BytesUtil.bytes2HexString(data));

            String panStr = com.urovo.i9000s.api.emv.Funs.FormatWithvalueR(Pan, "0000000000000000000000000000000");//15个0
            int panLen = Pan.length();
            int panC = panLen - 12;
            panStr = panC + panStr;

            Log.e(TAG, "Plain text PAN Field:" + panStr);

            byte[] panBuff2 = BytesUtil.hexString2Bytes(panStr);

            do_xor_urovo(panBuff2, data, 16);

            Log.e(TAG, "Intermediate Block A:" + BytesUtil.bytes2HexString(panBuff2));

            panBuff2 = AESUtil.decrypt(panBuff2, key);


            pinBlock = BytesUtil.bytes2HexString(panBuff2);
            Log.e(TAG, "Plain text PIN Field:" + pinBlock);

            int pinLen = Integer.parseInt(pinBlock.substring(1, 2), 16);
            Log.e(TAG, "Pin length:" + pinLen + "");
            pinBlock = pinBlock.substring(2, 2 + pinLen);

            Log.e(TAG, "clear pin:" + pinBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlock;
    }

    public static void do_xor_urovo(byte[] src1, byte[] src2, int num) {
        int i;
        for (i = 0; i < num; i++) {
            src1[i] ^= src2[i];
        }
    }

}
