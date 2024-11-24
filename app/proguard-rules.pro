# قوانین کلی برای Room
-dontwarn androidx.room.**
-keep class androidx.room.** { *; }

# نگه‌داشتن کلاس‌های Entity و DAO
-keepclassmembers class * {
    @androidx.room.* <fields>;
    @androidx.room.* <methods>;
}

# نگه‌داشتن کلاس‌هایی که با Room کار می‌کنند
-keep class ** implements androidx.room.RoomDatabase {
    *;
}

# نگه‌داشتن کامپایلر Room
-keepnames class androidx.room.** { *; }

-dontwarn androidx.lifecycle.**
-keep class androidx.lifecycle.** { *; }

# نگه‌داشتن ViewModelها
-keepclassmembers class ** extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# نگه‌داشتن LiveData
-keep class ** extends androidx.lifecycle.LiveData { *; }

# قوانین کلی برای iText
-dontwarn com.itextpdf.**
-keep class com.itextpdf.** { *; }
-keepclassmembers class com.itextpdf.** { *; }

# نگه‌داشتن کلاس‌هایی که حاوی متادیتا هستند
-keepattributes *Annotation*

# نگه‌داشتن تمام متدها و فیلدهای عمومی
-keepclassmembers class * {
    public *;
}

# جلوگیری از هشدارها
-dontwarn javax.annotation.**
-dontwarn kotlin.**
-dontwarn org.jetbrains.**

# This is generated automatically by the Android Gradle plugin.
-dontwarn org.slf4j.impl.StaticLoggerBinder


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