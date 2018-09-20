package android_serialport_api;

import android.os.SystemClock;
import android.util.Log;


import com.cczhr.androidserialport.OnDataReceiverListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MachineControl {

    private static final String TAG = "MachineControl";

    private SerialPortFinder mSerialPortFinder;

    private String mDeviceName;
    private int mBaudRate;
    private SerialPort mSerialPort;

    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadCOMThread mReadCOMThread;

    private OnDataReceiverListener onDataReceiverListener;

    /**
     * 机器控制
     *
     * @param devName  串口设备名
     * @param baudRate 波特率
     *                 <p>
     *                 例如 devName = "/dev/ttyS3"，baudRate =9600。
     */
    public MachineControl(String devName, int baudRate) {
        mSerialPortFinder = new SerialPortFinder();
        mDeviceName = devName;
        mBaudRate = baudRate;
        mSerialPort = null;
    }

    /**
     * 枚举所有串口的设备名。
     *
     * @return 串口的设备名数组
     */
    private String[] getCOMList() {
        return mSerialPortFinder.getAllDevicesPath();
    }

    /**
     * 打开串口
     *
     * @return 是否成功
     */
    public boolean openCOM() {
        String[] comList = getCOMList();
        for (String comname : comList) {
            Log.i(TAG, "所有串口设备名：" + comname);
        }
        if (mSerialPort == null) {
            try {
                mSerialPort = new SerialPort(new File(mDeviceName), mBaudRate, 0);
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();
                // 开启读取串口数据线程
                mReadCOMThread = new ReadCOMThread();
                mReadCOMThread.start();
                return true;
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
                mSerialPort = null;
            }
        }
        return false;
    }

    /**
     * 关闭串口
     */
    public void closeCOM() {
        if (mSerialPort != null) {
            mReadCOMThread.interrupt();
            mSerialPort.closeIOStream();
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    /**
     * 发送报文
     *
     * @param data 报文
     * @return 是否成功
     */
    public boolean sendCMD(byte[] data) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(data);
                mOutputStream.flush();
            } else {
                return false;
            }
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 读取串口数据
     */
    private class ReadCOMThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    byte[] buffer = new byte[1024];
                    if (mInputStream == null) {
                        break;
                    }
                    int available = mInputStream.available();

                    if (available > 0) {
                        int size = mInputStream.read(buffer);
                        if (size > 0) {
                            onDataReceiverListener.onDataReceiver(buffer, size);
                        }
                    }
                    else {
                        SystemClock.sleep(1);
                    }
                } catch (IOException e) {
                    Log.i(TAG, e.getMessage());
                    break;
                }
            }
        }
    }

    /**
     * 设置回调监听
     *
     * @param onDataReceiverListener onDataReceiverListener
     */
    public void setOnDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        this.onDataReceiverListener = onDataReceiverListener;
    }

}
