package android_serialport_api;

import android.os.SystemClock;
import android.util.Log;


import com.cczhr.androidserialport.OnDataReceiverListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ComControl {

    private static final String TAG = "ComControl";

    private static final String DEFAULT_SU_PATH = "/system/bin/su";
    private static String sSuPath = DEFAULT_SU_PATH;
    private static final long SLEEP = 1L;
    private static final long BUFFER_SLEEP = -1L;
    private long mSleep = SLEEP;
    private long mBufferSleep = BUFFER_SLEEP;
    private String mDeviceName;
    private int mBaudRate;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadCOMThread mReadCOMThread;

    private OnDataReceiverListener onDataReceiverListener;

    private static final int[] BAUDRATES = {110, 300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 38400, 56000, 57600, 115200, 128000, 256000};

    /**
     * 机器控制
     *
     * @param devName  串口设备名
     * @param baudRate 波特率
     *                 <p>
     *                 例如 devName = "/dev/ttyS3"，baudRate =9600。
     */
    public ComControl(String devName, int baudRate) {

        mDeviceName = devName;
        mBaudRate = baudRate;
        mSerialPort = null;
    }

    /**
     * 设置线程空闲休眠时间 减少cpu占用
     *
     * @param mSleep 休眠时间 毫秒
     */
    public void setmSleep(long mSleep) {
        this.mSleep = mSleep;
    }


    /**
     * 设置读取数据前等候时间 用于处理串口断包
     *
     * @param mBufferSleep 等候时间 毫秒
     */
    public void setmBufferSleep(long mBufferSleep) {
        this.mBufferSleep = mBufferSleep;
    }

    /**
     * 获取所有串口设备名
     *
     * @return 设备名数组
     */
    public static String[] getDeviceList() {
        return new SerialPortFinder().getAllDevicesPath();
    }

    /**
     * 获取常用波特率
     *
     * @return 波特率数组
     */
    public static int[] getBaudrateList() {
        return BAUDRATES;
    }

    /**
     * 设置su路径
     */
    public static void setsSuPath(String sSuPath) {
        ComControl.sSuPath = sSuPath;
    }

    /**
     * 打开串口
     *
     * @return 是否成功
     */
    public boolean openCOM() {
        if (mSerialPort == null) {
            try {
                mSerialPort = new SerialPort(new File(mDeviceName), mBaudRate, 0, sSuPath);
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
    public boolean sendData(byte[] data) {
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
        private byte[] buffer = new byte[1024];

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    if (mInputStream == null) {
                        break;
                    }
                    int available = mInputStream.available();
                    if (available > 0) {
                        if (mBufferSleep > 0L) {
                            SystemClock.sleep(mBufferSleep);
                        }
                        int size = mInputStream.read(buffer);
                        if (size > 0) {
                            onDataReceiverListener.onDataReceiver(subBytes(buffer, 0, size), size);
                        }
                    } else {
                        SystemClock.sleep(mSleep);
                    }
                } catch (IOException e) {
                    if (e.getMessage() != null)
                        Log.i(TAG, e.getMessage());
                    break;
                }
            }
        }
    }


    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
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
