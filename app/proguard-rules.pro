# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn com.google.common.base.**
-keep class com.google.common.base.** {*;}
-dontwarn com.google.errorprone.annotations.**
-keep class com.google.errorprone.annotations.* {*;}
-dontwarn   com.google.j2objc.annotations.**
-keep class com.google.j2objc.annotations.** { *; }
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation


-dontwarn retrofit2.**
-keep class retrofit2.** { *; }


-keep class com.musify.app.domain.models.* { *; }
-keep class com.musify.app.player.* { *; }

-keep class androidx.datastore.*.** {*;}
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep,allowobfuscation class * extends com.google.protobuf.GeneratedMessageLite { *; }

-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
  <fields>;
}