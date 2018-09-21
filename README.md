[![](https://img.shields.io/badge/androidserialport-1.0.0-brightgreen.svg)](https://bintray.com/cczhr/android-serialport/AndroidSerialPort)
# AopTest
Android AOP编程以及Android串口库
## Android串口使用例子
```java
获取串口设备
ComControl.getDeviceList()
获取常用波特率
ComControl.getBaudrateList()

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
```
