# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션


## 🚀 실습 - 단위 테스트 작성
### 기능 요구사항
- [x] 지하철 구간 관련 단위 테스트를 완성
    - [x] 구간 단위 테스트 (LineTest)
    - [x] 구간 서비스 단위 테스트 with Mock (LineServiceMockTest)
    - [x] 구간 서비스 단위 테스트 without Mock (LineServiceTest)
- [x] 단위 테스트를 기반으로 비즈니스 로직을 리팩터링

---

## 🚀 1단계 - 구간 추가 요구사항 반영
### 사용자 스토리
```
사용자로서
지하철 노선도를 조금 더 편리하게 관리하기위해
위치에 상관없이 지하철 노선에 역을 추가 할 수 있다
```
### 기능 요구사항(완료 조건)
- [x] 노선에 역 추가시 노선 가운데 추가
- [x] 노선에 역 추가시 노선 처음에 추가
- [x] 이미 등록되어있는 역은 노선에 등록될 수 없음
### 프로그래밍 요구사항
- [x] 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현
  - [x] 기능 요구사항 참고하여 인수 조건을 정의
  - [x] 인수 조건을 검증하는 인수 테스트 작성
  - [x] 인수 테스트를 충족하는 기능 구현
- [x] 조건은 인수 테스트 메서드 상단에 주석으로 작성
  - [x] 뼈대 코드의 인수 테스트를 참고

---
## 🚀 2단계 - 구간 제거 요구사항 반영
### 사용자 스토리
```
사용자로서
지하철 노선도를 조금 더 편리하게 관리하기위해
위치에 상관없이 지하철 노선에 역을 제거 할 수 있다
```
### 기능 요구사항(완료 조건)
- [x] 노선에 등록된 역 제거 시 해당 역이 노선 가운데 있어도 제거
- [x] 노선에 등록된 역 제거 시 해당 역이 상행 종점역이어도 제거
### 프로그래밍 요구사항
- [x] 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현
  - [x] 기능 요구사항 참고하여 인수 조건을 정의
  - [x] 인수 조건을 검증하는 인수 테스트 작성
  - [x] 인수 테스트를 충족하는 기능 구현
- [x] 조건은 인수 테스트 메서드 상단에 주석으로 작성
  - [x] 뼈대 코드의 인수 테스트를 참고

---
🚀 3단계 - 경로 조회 기능

### 기능 요구사항(완료 조건)
- [x] 요구사항 설명에서 제공되는 추가된 요구사항을 기반으로 경로 조회 기능을 구현
- [x] 추가된 요구사항을 정의한 인수 조건을 도출
- [x] 인수 조건을 검증하는 인수 테스트를 작성
### 프로그래밍 요구사항
- [x] 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현
  - [x] 요구사항 설명을 참고하여 인수 조건을 정의
  - [x] 인수 조건을 검증하는 인수 테스트 작성
  - [x] 인수 테스트를 충족하는 기능 구현
- [x] 조건은 인수 테스트 메서드 상단에 주석으로 작성
  - [x] 뼈대 코드의 인수 테스트를 참고
- [x] 인수 테스트 이후 기능 구현은 TDD로 진행
  - [x] 도메인 레이어 테스트는 필수
  - [x] 서비스 레이어 테스트는 선택
### 요구사항 설명
#### Request
- source: 출발역 id 
- target: 도착역 
```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=3
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```
#### Response
- stations: 출발역으로부터 도착역까지의 경로에 있는 역 목록 
- distance: 조회한 경로 구간의 거리
```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 May 2020 14:54:11 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "stations": [
        {
            "id": 1,
            "name": "교대역"
        },
        {
            "id": 4,
            "name": "남부터미널역"
        },
        {
            "id": 3,
            "name": "양재역"
        }
    ],
    "distance": 5
}
```
