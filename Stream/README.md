# Stream

## 16.1 Stream 소개

1. 개요

* 목적: Collection의 요소를 lambda expression으로 처리하고자 함

    ```Java
    // Stream 이전
    List<String> list = Arrays.asList("A", "B", "C");
    Iterator<String> iterator = list.iterator();
    while(iterator.hasNext()) {
        String name = iterator.next();
        System.out.println(name);
    }

    // Stream
    List<String> list = Arrays.asList("A", "B", "C");
    Stream<String> stream = list.stream();
    stream.forEach(name -> System.out.println(name));
    ```

* 특징
  * Lambda expression: code의 간소화
  * 병렬 처리: 내부 iterator를 사용해 collection이 반복 작업 수행
    * 효율이 좋아짐
  * 중간 처리: Collection의 요소들을 필터링(중간 처리)하고 최종 작업 가능

* 종류
  * 기본 자료형(int, long 등) + 자주 쓰는 자료형(string, array 등)
  * Collection
  * 숫자 범위
  * File, directory
  
    ```Java
    // File
    Path path = Paths.get("src/data.txt");
    Stream<String> stream = Files.lines(path, Charset.defaultCharset())
    // Dir
    Path path = Paths.get("C:/src/");
    Stream<Path> stream = Files.list(path);
    ```

## 16.2 Stream Pipeline

1. 개요

* 정의: 여러 개의 stream이 연결된 구조
  * Reduction: 대량의 data에서 필요한 정보만을 축소하여 가져옴
    * ex) 다수의 학생에게서 수학 점수 평균을 산출
    * 이 때 stream을 여러 개 연결하여 처리
      * 학생 -> 수학 성적 -> 평균
  * 최종 처리를 제외한 모든 stream은 중간 처리 stream
    * 최종 처리 stream은 마지막에 하나만 호출 가능

    ```Java
    Stream<Student> studentStream = list.stream();
    Intstream scoreStream = studentStream.mapToInt(Student :: getScore);
    OptionalDouble od = scoreStream.average();
    double scoreAvg = od.getAsDouble();
    ```
