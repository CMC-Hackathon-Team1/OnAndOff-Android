![image](https://github.com/CMC-Hackathon-Team1/OnAndOff-Android/assets/60818655/7c68cf2a-ed49-428f-9e33-b117796b2969)

# 온앤오프

## 🚥 Introduction

🏆CMC 주최 제 2회 Ne(o)rdinary Hackathon 1등 수상 팀🏆<br>
자신의 취미, 자기 계발등을 기록하고 관리할 수 있는 서비스입니다.

CMC OnAndOff 팀에서 제작한 서비스입니다.

<br>

운영체제: Android 8.0 이상

구동 조건: Google play service, 네트워크 연결

Google Play : <a href="https://play.google.com/store/apps/details?id=com.onandoff.onandoff_android" target="_blank">온앤오프</a>

## 📱 Feature ##
- 자신만의 페르소나를 만들어서 사람들과 공유할 수 있습니다.
- 페르소나별로 일상을 기록할 수 있습니다.

## ⚡Stack
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white"> <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white">

## 📕 Used library
* LiveData
* Paging
* Retrofit2
* Flow
* Databinding
* Glide
* ElasticViews
* SwipeRefreshLayout

## 🐾 Architecture
MVVM 패턴을 적용하여, 크게 Model <- ViewModel <- View의 구조를 가집니다.

## ✏️ Release Note

**2024.01.10 (수)**
- 로그인되지 않는 오류를 수정

**2024.01.05 (금)**
- 페르소나가 선택되지 않는 버그 수정

**2023.06.01 (목)**
- 사용자 차단/해제 기능 추가
- 알림 기능 추가
- 일부 기능을 사용시 비정상 종료되는 버그 수정

**2023.03.17 (금)**
- 플레이스토어 첫 배포
- Version 1 출시

## License
```
MIT License

Copyright (c) 2024 온앤오프

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
