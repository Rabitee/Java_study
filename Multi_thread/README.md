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

```mermaid
mermaid("
graph LR;
    s1((스레드 객체 생성<br/>new))--"start()"-->s2((실행 대기<br/>runnable));
    s2((실행 대기<br/>runnable))--"반복"-->s3((실행));
    s3((실행))--"반복"-->s2((실행 대기<br/>runnable));
    s3((실행))-->s4((종료));
")
```
