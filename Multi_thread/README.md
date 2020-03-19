# Multi Thread

## 12.1 소개

### 12.1.1 Process & Thread

1. Process

* 정의: OS에서 실행 중인 app
  * User가 app를 실행하면 OS에서 memory를 할당하여 process를 생성
* 특징
  * 하나의 process는 동시에 둘 이상의 task를 수행 가능(multi tasking)
  * Process는 multi tasking을 multi thread 환경에서 수행
  * Multi process로 multi tasking 수행도 가능
  * 각 process는 서로 독립적으로 실행
    * 한 process의 error가 다른 process에 영향 X
  
2. Thread

* 정의: 하나의 code 흐름
  * Process 내부에 생성
* 특징
  * 다수의 요청을 한 번에 처리할 때 multi thread로 사용
  * 한 thread의 error가 다른 thread에 영향 O
    * Thread error로 해당 process가 종료되면 내부의 모든 thread도 종료

## 12.2 Main Thread & Worker Thread

1. Main thread

* 정의: Main method를 실행하는 thread
  * 필요할 경우 worker thread를 생성하여 multi tasking 수행
  * Worker thread를 포함해 모든 thread가 종료될 때까지 process는 유지됨

2. Worker thread

* 정의: Main thread 이외에 추가로 생성된 thread
* 특징
  * Java는 worker thread를 객체로 생성
  * 생성 방법이 다양함
  * Thread에 이름 부여가 가능
    * ```thread.getName(), thread.setName()```
* 실행 방법: start() method 호출
* 생성 방법   
  I) Thread class로부터 직접 생성

    ```Java
    // Runnable interface 직접 구현
    Class Task implements Runnable {
        public void run() {
            ...
        }
    }
    Runnable task = new Task();
    Thread thread = new Thread(task);

    // Anonymous instance로 구현
    Thread thread = new Thread(new Runnable() {
        public void run() {
            ...
        }
    });

    // Lambda expression
    Thread thread = new Thread( () -> {
        ...
    });

    // start
    thread.start();
    ```

  II) Thread의 하위 class에서 생성

    ```Java
    public class WorkerThread extends Thread {
        public void run(){
            ...
        }
    }
    Thread thread = new WorkerThread();
    ```

3. Thread priority

* 배경 지식
  * Concurrency: 한 core에서 여러 thread가 돌아가면서 작업 수행
  * Parallelism: 여러 core에서 각 thread를 동시에 수행
* Thread scheduler
  * 개요: Thread가 core보다 많으면, concurrency 수행을 위한 순서 결정이 필요 -> Java는 priority를 부여하여 결정
* Priority
  * 1~10 사이의 값을 가짐
  * 높을 수록 우선순위가 높음
  * default는 5

## 12.3 Synchronized Method & Block

1. 배경

* 공유 객체: 여러 thread가 하나의 객체에 동시에 접근하면 문제가 발생
  * A가 사용하던 객체가 B에 의해 변경되면 A의 의도와는 다른 결과가 나올 수 있음
* 임계 영역(Critical Section)
  * 정의: Multi thread 환경에서 단 하나의 thread만 실행 가능한 code 영역
  * 방법: Java는 synchronized method와 block을 제공
    * Thread가 해당 method 또는 block에 들어가면 즉시 객체가 잠김
    * 이 동안 다른 thread는 해당 method 또는 block 실행 불가능
  
2. Synchronized keyword

* Method: method 전체가 critical section으로 바뀜
  * ```public synchronized void method() { }```
* Block: 해당 block만 critical section으로 바뀜

    ```Java
    public void method() {
        ...
        // synchronized block
        synchronized(var) {
            // var: 공유 객체
            // critical section
            ...
        }
        ...
    }
    ```

## 12.4 Thread의 상태

1. 종류
  
* 실행 대기 상태: start() method를 호출한 thread는 실행 대기 상태가 됨
* 실행 상태: 실행 대기 상태인 thread 중 scheduler에 의해 선택된 thread가 run() method를 호출하면 실행 상태가 됨
* 종료 상태: run() method가 종료되면 종료 상태가 됨
* 일시 정지 상태: 실행 상태 도중 특별한 경우 일시 정지 상태가 됨
  * 이 때 다시 실행 상태가 되려면 우선 실행 대기 상태가 되어야 함
  * 예시: thread가 critical section에 진입했는데 이미 다른 thread가 사용중 일 경우.

2. 제어

* 정의: thread의 상태를 변경하는 것
* 방법: 제어를 위한 method를 적절히 호출

## 12.5 Deamon Thread

1. 개요

* 정의: 주 thread의 작업을 돕는 보조 thread
  * 주 thread가 종료되면 같이 종료됨
  * 예시: 문서의 자동 저장, garbage collector
* 생성 방법: setDaemon(true)를 호출
  * 주의 사항: start() method 호출 이후에 daemon을 생성하면 exception이 발생하므로 start() 전에 호출해야 함
* 예시
  
  ```Java
  public static void main(String[] args) {
    // main: 주 thread, AutoSaveThread: daemon thread
    AutoSaveThread thread = new AutoSaveThread();
    thread.setDeamon(true);
    thread.start();
    ...
  }
  ```
## 12.6 Thread Group

1. 개요

* 목적: 관련된 thread를 묶어서 관리하고자 함
  * thread는 반드시 하나의 thread group에 속하게 됨
    * 명시하지 않을 경우 자신을 생성한 thread와 같은 group
    * main에서 생성하면 main thread와 같은 group이 됨
* 생성 방법: ThreadGroup class의 생성자를 호출
  * 생성시 parent threadgroup을 지정하지 않으면 현재 group의 하위로 생성됨
  * Thread 생성 시 특정 group에 포함시키고자 할 경우 객체 생성 단계에서 parameter로 group을 지정
  
      ```Thread t = new Thread(group);```

2. 관리

* Interrupt: ThreadGroup의 interrupt() method를 이용하면 group에 속한 모든 thread가 interrupt됨
  * 일일히 thread마다 interrupt하는 것 보다 효율적임
  * 기타 관리를 위한 다양한 method 제공

## 12.7 Thread Pool

1. 개요

* 정의: 지정한 개수만큼 thread가 모여있는 객체
* 목적: 병렬 처리시 작업량에 비례해 thread 개수가 증가하는 것을 방지하기 위함
  * Thread가 늘어나면 thread 생성 및 scheduling 처리로 인해 app의 성능이 저하됨
  * Threadpool은 미리 지정한 thread 개수만큼만 thread가 있어 작업량이 늘어나도 thread 수가 늘어나지 않음
* 동작 방식: Task queue에 task들을 넣고 queue에서 task를 가져와 thread에 할당
  * 한 thread가 하나의 task 담당
  * Thread의 task가 끝나면 queue에서 새로운 task를 가져옴
* 생성 방법: ExecutorService 객체 이용
  
    ```Java
    // parameter로 최대 thread 개수를 지정 가능
    ExecutorService es = Executors.newCachedThreadPool();
    ```

* 종료 방법: shutdown() method 호출
  * Threadpool의 thread는 daemon이 아니므로 main thread가 종료되어도 계속 작업을 수행함.
    * 따라서 Threadpool의 thread를 따로 종료해야 함

2. Task 생성과 처리

* 생성: Task는 Runnable과 Callable class로 표현
  * Runnable은 return이 없고 Callable은 리턴이 있음

  ```Java
  Runnable task = new Runnable() {
    public void run() {
      ...
    }
  }

  Callable<T> task = new Callable<T>() {
    public T call() throws Exception {
      ...
      return T;
    }
  }
  ```

* 작업 처리: Exceutor가 처리
  * 두 가지 생성 method중 submit()을 권장
    * return으로 처리 결과를 얻을 수 있음
    * 예외 발생 시 thread가 종료되지 않고 재사용
      * execute() method는 예외 발생 시 해당 thread를 종료하고 새로 생성함 -> overhead 발생

* 처리 결과