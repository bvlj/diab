# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/joey/android/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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

# AndroidViewModel
-keep class * extends androidx.lifecycle.AndroidViewModel { <init>(...); }

# Ensure the custom, fast service loader implementation is removed.
-assumevalues class kotlinx.coroutines.internal.MainDispatcherLoader {
  boolean FAST_SERVICE_LOADER_ENABLED return false;
}
# -checkdiscard class kotlinx.coroutines.internal.FastServiceLoader

# Keep fitness classes
-keep class it.diab.fit.** {}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}