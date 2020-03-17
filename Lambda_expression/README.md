# Lambda Expression
### 14.1  소개
* 용도: Anonymous function 생성을 위함
    * 실제로는 interface의 anonymous implement instance를 생성
* 형태: (parameter) -> {code}
    * 예시: <pre><code>(int x) -> {System.out.println(x);} </code></pre>

### 14.2 기본 문법
* 기본 형태: (parameter) -> {code}
    * parameter를 사용해 {}안의 code를 실행
    * 람다식에서는 일반적으로 parameter의 타입을 명시하지 않음
        <pre><code> (x) -> {System.out.println(x);} </code></pre>
        