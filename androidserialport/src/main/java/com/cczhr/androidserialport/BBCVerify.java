package com.cczhr.androidserialport;

public class BBCVerify {

    /**
     * byte数组异或(BBC校验)
     * @param datas
     * @return
     */
    public static byte bbcVal(byte[] datas){
        byte temp=datas[0];
        for (int i = 1; i <datas.length; i++) {
            temp ^=datas[i];
        }
        return temp;
    }
}
