# Image-Uploading-Using-MultipartImage
Image-Uploading-Using-MultipartImage-Android

//Add this permission to manifest

    uses-permission android:name="android.permission.INTERNET" 
    uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
    uses-permission android:name="android.permission.ACCESS_WIFI_STATE"
    uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    uses-permission android:name="android.permission.CAMERA"


//Add this to you Gradle file.

    packagingOptions {
        exclude 'META-INF/DEPENDENCIES.txt'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/notice.txt'
        exclude 'META-INF/license.txt'
        exclude 'META-INF/dependencies.txt'
        exclude 'META-INF/LGPL2.1'
    }

    defaultConfig {
        applicationId ""
        minSdkVersion 15
        targetSdkVersion 21
        versionCode 1
        versionName "1.0"
    }


//Add this dependencies

    compile files('libs/android-async-http-1.4.4.jar')
    compile files('libs/httpclient-4.3.6.jar')
    compile files('libs/httpmime-4.3.6.jar')
    compile files('libs/httpcore-4.3.3.jar')

