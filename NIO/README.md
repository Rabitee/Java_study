# NIO

## 19.1 NIO 소개

1. 소개

* 정의: New IO
* IO와의 차이점

구분 | IO | NIO |
|---|---|---|
입출력 방식 | Stream | Channel |
버퍼 방식 | non-buffer | buffer |
비동기 방식 | 지원 X | 지원 O |
blocking 여부 | blocking만 지원 | non-blocking도 지원 |

* Stream vs channel
  * Stream: 입출력을 위해서 입력 stream과 출력 stream을 따로 생성해야 함
  * Channel: 양방향 입출력이 가능해 channel 하나로 입출력 가능
* Buffer vs non-buffer
  * Buffer를 사용하면 입출력 성능이 좋아짐
  * Buffer는 buffer 내부에서 data를 다시 접근 가능
* Blocking vs non-blocking
  * IO는 입출력 작업인 read(), write()시 blocking 됨
    * 해당 stream을 close하는 것 이외에는 blocking 상태를 벗어나지 못함
    * Interrupt도 불가능
  * NIO는 blocking상태에서도 interrupt로 나올 수 있음
  * NIO는 non-blocking도 지원
    * Selector를 이용
* IO vs NIO
  * NIO: 입출력 성능이 빠름 + 비동기 처리로 과도한 thread 생성 방지 가능
    * 연결 client 수가 많고, 입출력 처리 시간이 짧은 작업에 유리
      * 입출력 시간이 길면 thread 개수가 제한된 상태에서 대기하는 시간이 길어짐
  * IO: Data를 받은 즉시 처리 + 작업마다 thread 생성
    * 연결 client 수가 적고, 입출력 처리 시간이 긴 작업에 유리
      * 입출력 시간이 짧으면 빠르게 처리가능한 buffer 효율이 더 좋음
      * Client 수가 많으면 thread가 과도하게 생성될 수 있음

2. Buffer

* 종류: Direct와 non-direct로 구분

    구분 | non-direct | direct |
    |------|----|----|
    사용하는 메모리 공간 | JVM의 heap memory | OS의 memory |
    Buffer 생성 시간 | 빠름 | 느림 |
    Buffer 크기 | 작음 | 큼
    입출력 성능 | 낮음 | 높음
    생성 방법 | allocate() | allocateDirect()

* Byte order
  * Big-endian: 앞쪽 byte부터 처리
    * JVM은 항상 big-endian으로 동작
  * Little-endian: 뒤쪽 byte부터 처리

* 위치 속성
  * position: 현재 위치
  * limit: 읽거나 쓸 수 있는 위치의 한계점
    * 최초 buffer 생성시 capacity와 같음
    * flip()등 관련 method 호출 시 조정됨
  * capacity: 버퍼의 크기
  * mark: reset()을 호출했을 때 돌아올 위치
    * 0 <= mark <= position <= limit <= capacity

* Buffer 변환
  * Channel은 data 입출력을 bytebuffer로 처리
    * data를 복원하려면 적절한 type의 buffer로 변환해야 함
      * String의 경우는 Charset class를 이용한 encoding도 필요

## 19.2 TCP Non-Blocking Channel

1. 특징

* Non-blocking
  * Client가 요청할 때까지 무한 루프로 code 실행
    * CPU가 과도하게 소비됨 -> Selector로 문제 해결

* Selector
  * 역할: 유사 event listener + multiplexer
    * 처리할 event가 들어온 경우 channel이 selector에 통보
    * Selector는 channel을 선택하여 worker thread가 accept() 또는 read()를 호출하여 즉시 작업을 처리
    * Multiplexer 역할 가능
      * Multi channel을 single thread로 처리 가능

* Multi channel 처리 과정
  * Channel은 selector에 자신을 key로 생성하여 등록
  * Selector는 client의 요청이 들어오면 key set의 key중 즉시 작업 처리가 가능한 key들을 골라 따로 selected key set에 저장
  * Worker thread는 selected key set에 있는 key를 하나 꺼내서 작업 처리
    * Selected key set이 빌 때까지 처리
  * Selected key set이 비게 되면 selector는 다시 즉시 작업 처리 가능한 key들을 골라 set을 채움

    ```Java
    // Selector open
    try {
        Selector selector = Selector.open();
    }
    catch(IOException e) {

    }
    // Non-blocking setting
    // Selector는 non-blocking 설정된 channel만 등록 가능
    ServerSocketChannel ssc = ServerSocketChannel.open();
    SocketChannel sc = SocketChannel.open();
    ssc.configureBlocking(false);
    sc.configureBlocking(false);

    // Channel register
    SelectionKey sk = ssc.register(selector, SelectionKey.OP_ACCEPT);
    SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
    // SelectionKey는 selector에서 얻을 수 있어서 따로 관리없이 사용 가능
    SelectionKey key = sc.keyFor(selector)
    // 작업 유형 변경 가능
    sk.interestOps(SelectionKey.OP_WRITE);
    selector.wakeup();
    
    // Selected key set 획득
    // select()는 blocking 됨 -> 별도의 worker thread 필요
    // 작업 유형이 변경되면 select()를 다시 실행해야 함
    // select()는 준비된 channel이 있거나, selector.wakeup()이 호출되거나, select()를 호출한 thread가 interrupt되면 return 함
    int keyCnt = selector.select();
    if(keyCnt > 0) {
        Set<SelectionKey> keySet = selector.selectedKeys();
    }

    Iterator<SelectionKey> itr = keySet.iterator();
    while(itr.hasNext()) {
        SelectionKey sk = itr.next();
        // 작업 유형에 따른 key 처리
        if(sk.isAcceptable()) {

        }
        else if(sk.isReadable()) {

        }
        else if(sk.isWritable()) {

        }
        itr.remove()    // 처리한 key를 key set에서 제거
    }
    // 다른 객체를 SelectionKey에 붙였다가 가져올 수 있음
    Client client = new Client();
    sk.attach(client);
    Client client = (Client)sk.attachment();
    ```

## 19.3 TCP Asynchronous Channel

1. 특징

* Blocking이 걸리는 각종 method를 호출하면 즉시 return
  * Non-blocking 방식과 동일
  * ThreadPool이 해당 작업들을 처리
    * Threadpool 내부의 worker thread가 작업을 완료하면 callback method가 자동으로 호출됨
* Asynchronous channel 관리을 위한 channelgroup 존재
  * 한 channelgroup은 하나의 threadpool 사용
* Callback method까지 사용한 예시

    ```Java
    // read 및 write도 아래와 유사
    AsynchronousServerSocketChannel assc = AsynchrousServerSocketChannel.open();
    assc.bind(new InetSocketAddress(5001));
    assc.accept(null, new CompletionHandler<AsynchrousSocketChannel, Void>() {
        // callback method
        public void completed(AsynchronousSocketChannel asc, Void attachment) {
            // 연결 수락 후 실행
            ...
            // Client의 연결 요청을 반복적으로 수행하기 위해 재호출
            assc.accept(null, this);
        }
        public void failed(Throwable e, Void attachment) {
            // 연결 실패 시 실행
            ...
        }
    })
    ```
