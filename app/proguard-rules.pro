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
-keep class coil.** { *; }
-keep class android.graphics.PostProcessor { *; }
-dontwarn android.graphics.PostProcessor
-keep class org.jpos.iso.** { *; }
-keep class com.urovo.sdk.** { *; }

-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-keepattributes *Annotation*
-dontwarn ch.qos.logback.core.net.*
-keep class org.xml.sax.** { *; }


# Preserve annotations
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes Signature

# Preserve classes that may be loaded reflectively
-keepclassmembers class * {
    @** *;
}

-keep class java.awt.** {*;}

-dontwarn org.xmlpull.v1.**
-dontwarn org.kxml2.io.**
-dontwarn android.content.res.**

-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }
-dontwarn org.xmlpull.v1.**
-dontwarn com.sun.net.ssl.internal.ssl.Provider

-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn javax.management.**
-dontwarn java.lang.management.**

-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn javax.script.ScriptEngineFactory
-dontwarn javax.transaction.xa.Xid


-dontwarn com.decodelibrary.R$raw
-dontwarn com.google.zxing.BarcodeFormat
-dontwarn com.google.zxing.EncodeHintType
-dontwarn com.google.zxing.MultiFormatWriter
-dontwarn com.google.zxing.Result
-dontwarn com.google.zxing.ResultPoint
-dontwarn com.google.zxing.WriterException
-dontwarn com.google.zxing.common.BitMatrix
-dontwarn com.google.zxing.qrcode.QRCodeWriter


-dontwarn com.imagealgorithm.**
-keep class com.imagealgorithm.**{*;}
-keep interface com.imagealgorithm.**{*;}

-dontwarn com.imagealgorithmlab.barcode.**
-keep class com.imagealgorithmlab.barcode.**{*;}
-keep interface com.imagealgorithmlab.barcode.**{*;}

-dontwarn com.jniexport.**
-keep class com.jniexport.**{*;}
-keep interface com.jniexport.**{*;}

-dontwarn com.urovo.**
-keep class com.urovo.**{*;}
-keep interface com.urovo.**{*;}

-dontwarn android.content.pm.**
-keep class android.content.pm.**{*;}
-keep interface android.content.pm.**{*;}

-dontwarn android.device.**
-keep class android.device.**{*;}
-keep interface android.device.**{*;}

-dontwarn com.android.device.**
-keep class com.android.device.**{*;}
-keep interface com.android.device.**{*;}

-dontwarn android.os.**
-keep class android.os.**{*;}
-keep interface android.os.**{*;}

-dontwarn android.udroid.content.pm.**
-keep class android.udroid.content.pm.**{*;}
-keep interface android.udroid.content.pm.**{*;}
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
