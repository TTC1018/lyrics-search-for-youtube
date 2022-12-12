# LyricsSearchForYoutube



## 프로젝트 소개

|구분| Description                                            |
| --------- | ----------------------------------------------- |
| 한줄 소개 | Youtube Music Application에 가사를 띄워주는 어플리케이션 |
| 진행 기간 | 2021.02.11 ~                           |
| Tech Stack |<img src="https://img.shields.io/badge/WindowManager-3DDC84?style=forthebadge&logo=Android&logoColor=white"/> <img src="https://img.shields.io/badge/Service-3DDC84?style=forthebadge&logo=Android&logoColor=white"/> <img src="https://img.shields.io/badge/AccessibilityService-3DDC84?style=forthebadge&logo=Android&logoColor=white"/> <img src="https://img.shields.io/badge/JSoup-FF6550?style=forthebadge"/>|



## 프로젝트 개요

- Yotube Music 어플리케이션의 많은 한국 음원들이 등록된 한글 가사가 없어 불편함
- Yotube Music 어플리케이션위에 가사를 띄워주는 어플리케이션의 필요성을 느낌
- 정식 음원곡에 대한 가사는 Melon, Genie등 음원 사이트에서 쉽게 얻을 수 있음

## 한눈에 보기

|<img src="https://user-images.githubusercontent.com/39405316/205969721-fd321668-5d97-4452-bf0f-5cd0f6d2eb78.gif" width=180>|<img src="https://user-images.githubusercontent.com/39405316/205970019-eb3a50a6-ab78-4d72-9ad1-635c8f28df90.gif" width=180>|<img src="https://user-images.githubusercontent.com/39405316/205970037-1704e413-38d0-4adb-82d2-3bdf546d8790.gif" width=180>|
|:--:|:--:|:--:|
|사용법 설명화면|권한 설정|플로팅 위젯 모드|
|<img src="https://user-images.githubusercontent.com/39405316/205970029-ebb4deaf-f258-4191-bae1-30142783be0a.gif" width=180>|<img src="https://user-images.githubusercontent.com/39405316/205972374-67270d7a-abb4-4f31-8107-4902f70f03d1.gif" width=180>|<img src="https://user-images.githubusercontent.com/39405316/205971302-23e51070-8ad4-417e-a208-05db4ad43e79.gif" width=180>|
|플로팅 위젯 드래그 및 팝업|가사 검색|가사 재검색|


## 기능 소개

#### ☝️ 접근성, 앱 오버레이 권한 필요 
|접근성 권한 요청|
|:---:|
|<img src="https://user-images.githubusercontent.com/39405316/205970019-eb3a50a6-ab78-4d72-9ad1-635c8f28df90.gif" width="250"/>|

- Youtube Music 어플 내 음원의 제목을 읽어들이기 위해 접근성 권한이 필요합니다.
- 플로팅 위젯 형태로 사용하기 위해 화면 오버레이 권한이 필요합니다.

---
### 사용법 설명
---
|사용법 설명화면|
|:---:|
|<img src="https://user-images.githubusercontent.com/39405316/205969721-fd321668-5d97-4452-bf0f-5cd0f6d2eb78.gif" width="250"/>|

- 앱을 처음 사용하는 사용자라면 앱 사용법을 확인할 수 있습니다.

---
### 플로팅 위젯 모드
---
|플로팅 위젯 모드|플로팅 위젯 드래그 및 팝업|
|:---:|:---:|
|<img src="https://user-images.githubusercontent.com/39405316/205970037-1704e413-38d0-4adb-82d2-3bdf546d8790.gif" width="250"/>|<img src="https://user-images.githubusercontent.com/39405316/205970029-ebb4deaf-f258-4191-bae1-30142783be0a.gif" width="250"/>|

- 앱을 플로팅 위젯 형태로 변환합니다.
- 플로팅 위젯은 자유롭게 드래그 및 팝업이 가능합니다.

---
### 가사 검색 및 Re-load
---
|가사 검색|가사 재검색|
|:---:|:---:|
|<img src="https://user-images.githubusercontent.com/39405316/205972374-67270d7a-abb4-4f31-8107-4902f70f03d1.gif" width="250"/>|<img src="https://user-images.githubusercontent.com/39405316/205971302-23e51070-8ad4-417e-a208-05db4ad43e79.gif" width="250"/>|

- 화면 내 곡 제목을 활용하여 가사를 검색합니다.
- 다른 곡을 재생해도 기능은 동일하게 작동합니다.
