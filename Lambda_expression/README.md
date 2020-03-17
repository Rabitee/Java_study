# Lambda Expression

## 14.1  소개

* 용도: Anonymous function 생성을 위함
  * 실제로는 interface의 anonymous implement instance를 생성
    * 람다식은 하나의 method만 정의하므로 abstract method가 하나인 interface만 람다식으로 구현 가능
      * Abstract method가 하나인 interface를 functional interface라고 함
    * Interface variable에 assign 가능
      * 해당 interface를 람다식의 target type이라 함
* 형태: (parameter) -> {code}
  * 예시 ```(int x) -> {System.out.println(x);}```

## 14.2 기본 문법

* 기본 형태: (parameter) -> {code}
  * parameter를 사용해 {}안의 code를 실행
    * 람다식에서는 일반적으로 parameter의 타입을 명시하지 않음
        ```(x) -> {System.out.println(x);}```
    * parameter나 실행문이 하나인 경우 괄호 생략 가능
        ```x -> System.out.println(x)```
    * Parameter가 없으면 ()로 표기
        ```() -> {code}```
    * Target type이 return 값이 있는 경우 {} 안에 return 가능
        ```(x, y) -> {return x+y;}```
    * {} 안에 return문만 있는 경우 다음과 같이 사용
        ```(x, y) -> x + y```

## 14.3 특징

* Class member & method
  * 사용 여부: 자유롭게 사용 가능
  * this 키워드: 람다식 내부의 this는 람다식을 실행한 instance를 참조함
* Local variable
  * 사용 여부: read는 가능하지만 write는 불가능 -> final 특성을 가지기 때문
    * Anonymous instance의 local variable 사용과 동일

## 14.4 자주 쓰이는 API의 functional interface

1. Consumer
   * 형태: parameter는 있으나 return은 없음
   * Method: accept(T t)
   * 예시
        ```Consumer<String> consumer = t -> System.out.println(t)```
2. Supplier
   * 형태: parameter는 없으나 return은 있음
   * Method: T get()
   * 예시
        ```Supplier<String> supplier = () -> {return 0;}```
3. Function
   * 형태: parameter가 있고 return도 있음
     * parameter와 return value의 type이 다름
   * Method: R apply(T t)
   * 예시
        ```Function<Student, String> f = t -> t.getName();```
4. Operator
   * 형태: parameter가 있고 return도 있음
     * parameter와 return value의 type이 같음
   * Method: T apply(T t)
   * 예시
        ```IntBinaryOperator op = (a, b) -> a + b```
5. Predicate
   * 형태: parameter가 있고 boolean type의 return이 있음
   * Method: boolean test(T t)
   * 예시
        ```Predicate<Student> p = t -> t.getName().equals("Tom");```

## 14.5 Method 참조

* 개요: 람다식은 기존 method를 호출만 하는 경우가 많은데, 이 경우를 단순화하고자 함
* 형태: :: 사용
* 예시
  
    ```Java
    (left, right) -> Math.max(left, right); // 기존 람다식
    Math :: max;                            // static method 참조
    Var :: max;                             // instance를 통한 참조
    ```

* Pamameter의 method 참조
  * 형태: static method와 동일, class 이름은 첫 번째 parameter의 class를 사용
  * 예시

    ```Java
    (a, b) -> {a.methodOfA(b);}             // 기존 람다식
    ClassOfA :: methodOfA                   // parameter의 method 참조
    ```

* 생성자 참조
  * 형태: Class :: new
  * 예시

    ```Java
    (a, b) -> {return new ClassOfA(a, b);}  // 기존 람다식
    ClassOfA :: new                         // 생성자 참조
    ```
