package com.cczhr.androidserialport;



public interface OnDataReceiverListener {

    /**
     * @param buffer 收到的字节数组
     * @param size   长度
     */
    void onDataReceiver(byte[] buffer, int size);
}
