# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 기능 요구사항
### 1단계 - 구간 추가 요구사항 반영
- 노선에 역 추가 시 노선 가운데 추가 할 수 있다.
  - Given: 특정 노선에 A-C 구간이 등록되어 있고,
  - When: A-C 사이에  A-B 구간을 추가하면,
  - Then: 노선 조회 시 A-B-C 구간이 등록되어있다.
- 노선에 역 추가 시 노선 처음에 추가 할 수 있다.
  - Given: 특정 노선에 B-C 구간이 등록되어 있고,
  - When: B-C 앞에  A-B 구간을 추가하면,
  - Then: 노선 조회 시 A-B-C 구간이 등록되어있다.
- 이미 등록되어있는 역은 노선에 등록될 수 없다.
- ### 2단계 - 구간 제거 요구사항 반영
- 위치에 상관없이 지하철 노선에 역을 제거 할 수 있다
  - Given: 특정 노선에 구간이 2개 이상 등록되어 있고,
  - When: 지하철 노선에 위치에 상관없이 구간을 제거하면.
  - Then: 노선을 조회했을 때 구간이 제거된다.

## 작업 목록
### 1단계 - 구간 추가 요구사항 반영
- 노선에 역 추가시 노선 가운데 추가 할 수 있다.
  - [x] 기존 인수 테스트 수정
  - [x] 인수테스트 작성
  - [x] Section.addSection() 유닛 테스트 작성
  - [x] 기존 distance보다 새로운 distance가 작아야한다.
  - [x] 사용하지 않는 예외 제거
- 노선에 역 추가 시 노선 처음에 추가 할 수 있다.
  - [x] 인수 테스트 작성
  - [x] 유닛 테스트 작성
  - [x] 기능 구현
  - [x] 구간 추가 메서드 추출로 리팩터링
  - [x] 구간 추가 테스트 간소화
  - [x] 구간 추가 = addSection 으로 통일
  - [x] Sections 생성 시 첫 구간 판단을 Sections 본인이 하도록 수정
- 이미 등록되어있는 역은 노선에 등록될 수 없다.
  - [x] 가운데 추가하는 구간의 하행역은 기존 구간에 중복되는 역이 존재해선 안된다.
  - [x] 마지막에 추가하는 구간의 하행역은 기존 구간에 중복되는 역이 존재해선 안된다.
  - [x] 처음에 추가하는 구간의 상행역은 기존 구간에 중복되는 역이 존재해선 안된다.
  - [x] 중복 역 에러가 발생하면 400을 리턴하는 컨트롤러 테스트 추가
- ### 2단계 - 구간 제거 요구사항 반영
- 이전 미션 리뷰어 피드백 반영
  - [x] 상수명 네이밍 컨벤션 지키기
  - [x] ~~createStation() 메서드 명 createSection() 으로 수정~~
  - [x] 빌더 패턴의 장점을 살리지 못하는 createSection() 수정
  - [x] if 문에서 '{}' 생략 된 부분 추가
  - [x] Sections of(Section... section) 가변인수 사용하는 부분 개선
  - [x] 구간 생성 분기 조건 부분 메서드 추출로 가독성 높이기
  - [x] @MethodSource 사용으로 인한 오류 테스트 pure function 으로 수정
- 위치에 상관없이 지하철 노선에 역을 제거 할 수 있다
  - [x] 마지막만 제거 가능하던 제약조건 삭제
  - [x] 인수 테스트 작성
  - [x] 유닛 테스트 작성
  - [x] 기능 구현
