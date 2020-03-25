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

## 18.3 네트워크

1. 소개

* 정의: 여러 대의 PC를 통신 회선으로 연결한 것
  * 지역 네트워크: 회사, 건물 등 특정 영역의 PC를 통신 회선으로 연결한 것
  * 인터넷: 지역 네트워크를 통신 회선으로 연결한 것

2. 서버와 클라이언트

* Server: 서비스를 제공하는 프로그램
  * Client의 request를 처리하고 response를 보냄
* Client: 서비스를 받는 프로그램
  * 서비스를 받기위해 연결을 요청

3. IP와 port

* IP: PC의 고유한 주소
  * 프로그램은 DNS를 이용해 PC의 IP 주소를 찾음
* Port: Server 선택을 위한 번호
  * PC내 여러 개의 server 중 적절한 port 번호를 찾아서 연결
    * 웹 서버 - 80, FTP - 21 등
  * Client도 server에서 정보를 받기위해 port 번호가 필요
    * OS에서 할당해줌

## 18.4 TCP

1. 소개

* 정의: Transmission Control Protocol
  * 연결 지향적 protocol: server와 client가 연결된 상태에서 data를 주고 받음
  * client가 요청 -> server가 수락 -> data 전달(3 hand shaking)
  * 단점
    * 반드시 연결되어야 함 -> 시간이 오래 걸림
    * UDP보다 느릴 수 있음
* ServerSocket & Socket class 사용
  * ServerSocket: Client의 요청을 수락할 지 여부를 처리
  * Socket: 연결된 client와 통신을 처리

2. 연결 요청 및 수락

* 연결 수락: ServerSocket에 port 번호를 parameter로 줌
* 연결 요청: 연결하려는 server의 IP와 port를 명시

    ```Java
    // 연결 수락
    // 5001: port 번호
    ServerSocket ss = new ServerSocket(5001);
    try {
        // accept()는 blocking 됨
        Socket s = ss.accept();
    }
    catch(Exception e) {

    }

    // 연결 요청
    try {
        // Socket 생성자는 blocking됨
        Socket socket = new Socket("localhost", 5001);
        Socket socket = new Socket();
        // connect()는 blocking 됨
        socket.connect(new InetSocketAddress("localhost", 5001))
    }
    catch(UnknownHostException e) {

    }
    catch(IOException e) {

    }
    ```

3. Thread 병렬 처리

* 목적: Main thread가 직접 입출력을 담당할 경우, Socket 생성자 또는 connect()에서 blocking 됨 -> 다른 작업이 불가능
  * Server가 지속적으로 client의 연결 수락 여부 처리가 불가능
  * Client1과 입출력하는 동안엔 client2의 입출력이 불가능
    * 따라서 server는 별도의 worker thread 생성이 필요
    * 접속 요청하는 client가 늘면 thread가 계속 늘어날 수 있음
      * ThreadPool로 관리가 필요

## 18.5 UDP

1. 소개

* 정의: User Datagram Protocol
  * 비연결 지향적 protocol: 연결을 거치지 않고 발신자가 일방적으로 data를 발신
    * 연결 과정이 없어 tcp 보다 빠름
    * 보낸 패킷들이 서로 다른 길로 감 -> 보낸 data와 실제 수신한 data의 순서가 다를 수 있음
      * 패킷 손실의 가능성도 존재

* TCP와의 비교: 속도는 UDP, 신뢰성은 TCP
