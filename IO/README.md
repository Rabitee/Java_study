# JAVA IO

## 18.1 IO 소개

1. 개요

* IO: 키보드 또는 파일, 네트워크 등으로부터 일어나는 입출력
  * Java의 data는 stream을 통해 이루어짐
  * Stream은 단뱡향성을 가짐
    * Data 교환을 위해서는 입력스트림과 출력스트림 모두 필요
  * Stream은 byte 기반과 char 기반으로 나뉨
* InputStream: Data를 입력받을 때 사용하는 byte 기반
* OutputStream: Data를 보낼 때 사용하는 byte 기반

    ```Java
    InputStream is = new FileInputStream("test.txt");
    OutputStream os = new FileOutputStream("test.txt");
    // read(), write(int b)
    int readByte = 0;
    byte[] data = "ABC".getBytes();
    while((readByte = is.read()) != -1) {
        ...
    }
    for(int i=0; i<data.length; i++) {
        os.write(data[i]);
    }
    // read(byte[] b), write(byte[] b)
    int readByteNo;
    byte[] readBytes = new byte[100];
    while((readByteNo = is.read(readBytes)) != -1) {
        ...
    }
    os.write(data);
    // read(byte[] b, int off, int len), write(byte[] b, int off, int len)
    while((readByteNo = is.read(readBytes, 0, 100)) != -1) {
        ...
    }
    os.write(data, 1, 2);
    os.flush();

    is.close();
    os.close();
    ```

* Reader: Char 기반 data 입력 스트림
* Writer: Char 기반 data 출력 스트림