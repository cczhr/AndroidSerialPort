[![](https://img.shields.io/badge/androidserialport-1.0.1-brightgreen.svg)](https://bintray.com/cczhr/android-serialport/AndroidSerialPort)
# AndroidSerialPort
Android串口封装
## Android串口使用例子
在app build.gradle中添加
```xml
implementation 'com.cczhr:androidserialport:1.0.1'
```
```java
获取串口设备
ComControl.getDeviceList()
获取常用波特率
ComControl.getBaudrateList()
修改su路径 默认为/system/bin/su
ComControl.setsSuPath("/system/xbin/su");

ComControl comControl=new ComControl("/dev/ttySAC3",9600);
  if(comControl.openCOM()){
      //打开串口成功
  }else {
      //打开串口失败
  }
comControl.setOnDataReceiverListener(new OnDataReceiverListener() {
  @Override
  public void onDataReceiver(byte[] buffer, int size) {
                  //收到的数据
    }
  });
 设置线程休眠时间减少cpu占用 默认1毫秒
 comControl.setmSleep(100);
 发送数据
 comControl.sendData(bytes);
 关闭串口
 comControl.closeCOM();
```
