-keep class com.kakao.sdk..model. { ; }
-keep class extends com.google.gson.TypeAdapter
# https://github.com/square/okhttp/pull/6792
-dontwarn org.bouncycastle.jsse.
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

-keep interface com.kakao.sdk.**.*Api