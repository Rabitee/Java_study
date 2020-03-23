# JAVA IO

## 18.1 IO 소개

1. 개요

* IO: 키보드 또는 파일, 네트워크 등으로부터 일어나는 입출력
  * Java의 data는 stream을 통해 이루어짐
  * Stream은 단뱡향성을 가짐
    * Data 교환을 위해서는 입력스트림과 출력스트림 모두 필요
  * Stream은 byte 기반과 char 기반으로 나뉨
* InputStream: Byte 기반 data 입력 스트림
* OutputStream: Byte 기반 data 출력 스트림
* Reader: Char 기반 data 입력 스트림
* Writer: Char 기반 data 출력 스트림

    ```Java
    InputStream is = new FileInputStream("test.txt");
    OutputStream os = new FileOutputStream("test.txt");
    // read(), write(int b)
    // read()는 1byte를 읽고 4byte int type으로 return
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

    ```Java
    // read(), write(int c)
    // read()는 2byte를 읽고 4byte int type으로 return
    // write(int c)는 parameter의 int 값 중 끝의 2byte를 출력
    Reader reader = new FileReader("test.txt");
    Writer writer = new FileWriter("test.txt");
    int readData = 0;
    while((readData = reader.read()) != -1) {
        char charData = (char)readData;
        ...
    }
    char[] data = "ABC".toCharArray();
    for(int i=0; i<data.length; i++) {
        writer.write(data[i]);
    }
    // read(char[] buf), write(char[] buf)
    int readCharNo = 0;
    char[] buf = new char[2];
    while((readCharNo = reader.read(buf)) != -1) {
        ...
    }
    writer.write(data);
    // read(char[] buf, int off, int len), write(char[] buf, int off, int len)
    while((readCharNo = reader.read(buf, 0, 2)) != -1) {
        ...
    }
    writer.write(data, 1, 2)
    // write(String str), write(String str, int off, int len)
    String data = "ABCD";
    writer.write(data);
    writer.flush();

    reader.close();
    writer.close();
    ```

## 18.2 입출력

1. Console 입출력

* System.in: InputStream type
  
  ```Java
  InputStream is = System.in;
  int a = is.read();
  byte[] byteData = new byte[15];
  int readbyteNo = System.in.read(byteData);
  // enter 입력 제외를 위해 -2 함
  String str = new String(byteData, 0, readByteNo - 2);
  ```

* System.out: PrintStream type

    ```Java
    OutputStream os = System.out;
    byte b = 97;
    os.write(b);
    os.flush();
    ```

* Console class: console 문자열 입력 지원용 class
* Scanner: 기본 type 지원 class
  * console, File, InputStream, Path 등 다양한 source 지원

2. 객체 입출력

* 소개: 객체는 문자가 아님 -> byte stream 이용
  * Serialization: 객체의 field값을 연속적인 byte로 변환하는 것
    * ObjectOutputStream 사용
  * Deserialization: 입력 스트림에서 읽은 연속 byte를 객체로 복원하는 것
    * ObjectInputStream 사용
* 주의점
  * 객체 입출력시 객체를 출력한 순서와 동일하게 읽어야 함
  * 생성자나 method는 serialization 대상이 아님
  * static 또는 transient가 선언된 field는 serialization 안됨
* Serializable: serialization 가능 여부를 나타내는 interface
  * Serializable을 implement한 class만 serialization 가능
* serialVersionUID: Deserialization할 때는 serialization 했을 때와 같은 class인 경우만 가능
  * Serializable class를 컴파일할 때 마다 serialVersionUID field 생성
    * 해당 field가 같은 경우에만 deserialization 가능
* Serialization의 상속: Child class만 serializable을 구현한 경우 parent class의 field들은 serialization 불가능
  * readObject(), writeObject() method 이용
    * readObject()는 deserialization시, writeObject()는 serialization시 자동으로 호출됨
  * 주의점: 반드시 private로 선언

    ```Java
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(parentField);
        ...
        out.defaultWriteObject(); // Child class의 모든 field를 serialization
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        parentField = in.readUTF();
        ...
        in.defaultReadObject(); // Child class의 모든 field를 deserialization
    }
    ```