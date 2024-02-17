# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## Step0. 단위 테스트 작성

### 기능 요구사항
- [x] 지하철 구간 관련 단위 테스트를 완성하세요.
  - [x] 구간 단위 테스트 (LineTest)
  - [x] 구간 서비스 단위 테스트 with Mock (LineServiceMockTest)
  - [x] 구간 서비스 단위 테스트 without Mock (LineServiceTest)
- [x] 단위 테스트를 기반으로 비즈니스 로직을 리팩터링 하세요.

---

## Step1. 구간 추가 요구사항 반영

### 기능 요구사항
- [x] 노선에 역 추가시 노선 가운데 추가 할 수 있다.
- [x] 노선에 역 추가시 노선 처음에 추가 할 수 있다.
- [x] 이미 등록되어있는 역은 노선에 등록될 수 없다.
 
### 프로그래밍 요구사항
- [x] 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현하세요.
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.

## Step2. 구간 제거 요구사항 반영

### 기능 요구사항(완료 조건)
- [x] 노선에 등록된 역 제거 시 해당 역이 노선 가운데 있어도 제거할 수 있다.
- [x] 노선에 등록된 역 제거 시 해당 역이 상행 종점역이어도 제거할 수 있다.

### 프로그래밍 요구사항
- [x] 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현하세요.
- [x] 요구사항 설명을 참고하여 인수 조건을 정의
- [x] 인수 조건을 검증하는 인수 테스트 작성
- [x] 인수 테스트를 충족하는 기능 구현
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
- [x] 뼈대 코드의 인수 테스트를 참고
