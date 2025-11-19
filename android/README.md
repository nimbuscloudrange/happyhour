# Happy Hour WebView - Android App

An Android application that uses HTML5 as its complete UI, with full support for:
- Receiving input from the HTML5 interface
- Loading data from the web
- Bidirectional communication between HTML5 and Android

## Features

- **HTML5 UI**: Complete UI built with modern HTML5, CSS3, and JavaScript
- **WebView Integration**: Native Android WebView with JavaScript enabled
- **JavaScript Bridge**: Two-way communication between HTML5 and Android native code
- **Web Data Loading**: Fetch and display data from web APIs
- **Input Handling**: Receive and process user input from HTML5 forms
- **Modern Design**: Beautiful gradient UI with glassmorphism effects
- **AdMob Integration**: Google AdMob banner ads support for monetization

## Project Structure

```
app/
├── src/
│   └── main/
│       ├── java/com/happyhour/webview/
│       │   └── MainActivity.java    # Main activity with WebView
│       ├── assets/
│       │   └── index.html           # HTML5 UI
│       ├── res/
│       │   └── values/
│       │       └── strings.xml      # App strings
│       └── AndroidManifest.xml      # App manifest with permissions
├── build.gradle                     # App-level build config
└── proguard-rules.pro              # ProGuard rules

build.gradle                        # Project-level build config
settings.gradle                     # Gradle settings
gradle.properties                  # Gradle properties
```

## Setup Instructions

1. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to this directory

2. **Sync Gradle**:
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

3. **Run the App**:
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift+F10
   - The app will install and launch

## How It Works

### HTML5 to Android Communication

The HTML5 interface can send data to Android using the JavaScript interface:

```javascript
// Send data to Android
Android.receiveData("Hello from HTML5!");

// Show toast message
Android.showToast("Message");

// Get data from Android
const info = Android.getDeviceInfo();
```

### Android to HTML5 Communication

Android can execute JavaScript in the WebView:

```java
webView.evaluateJavascript("javascript:updateUI('data')", null);
```

### Loading Web Data

The HTML5 interface uses the Fetch API to load data from web URLs. The app has internet permissions configured to allow this.

## Permissions

- `INTERNET`: Required for loading web data and AdMob ads
- `ACCESS_NETWORK_STATE`: To check network availability

## Requirements

- Android SDK 24 (Android 7.0) or higher
- Target SDK 34 (Android 14)
- Java 8 or higher

## Customization

### Modify HTML5 UI

Edit `app/src/main/assets/index.html` to customize the UI.

### Add More JavaScript Functions

Add methods to the `WebAppInterface` class in `MainActivity.java`:

```java
@JavascriptInterface
public void myCustomMethod(String data) {
    // Your code here
}
```

Then call it from HTML5:

```javascript
Android.myCustomMethod("data");
```

## AdMob Configuration

This app includes Google AdMob integration for displaying banner ads. The AdMob SDK is already integrated and configured.

### Current Setup

- **AdMob SDK Version**: 22.6.0 (Google Play Services Ads)
- **Ad Format**: Banner ads displayed at the bottom of the screen
- **Ad Lifecycle**: Properly handled (pause/resume/destroy) in MainActivity

### Configuration Files

1. **AndroidManifest.xml**: Contains the AdMob App ID
   - Location: `app/src/main/AndroidManifest.xml`
   - Current value: Test App ID (`ca-app-pub-3940256099942544~3347511713`)

2. **Layout File**: Contains the AdView component
   - Location: `app/src/main/res/layout/activity_main.xml`
   - Current ad unit ID: Test Banner ID (`ca-app-pub-3940256099942544/6300978111`)

3. **MainActivity.java**: Initializes AdMob and loads ads
   - AdMob is initialized in `onCreate()`
   - Banner ads are loaded automatically
   - Ad lifecycle is managed in `onPause()`, `onResume()`, and `onDestroy()`

### Setting Up Your AdMob Account

Before publishing, you need to replace the test IDs with your real AdMob IDs:

1. **Create an AdMob Account**:
   - Go to [Google AdMob](https://admob.google.com/)
   - Sign in with your Google account
   - Create a new app or select an existing one

2. **Get Your App ID**:
   - In AdMob console, go to Apps → Your App
   - Copy your App ID (format: `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`)

3. **Create an Ad Unit**:
   - Go to Ad units → Add ad unit
   - Select "Banner" format
   - Copy your Ad Unit ID (format: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`)

4. **Update Configuration**:
   - Replace the App ID in `AndroidManifest.xml` (line 24)
   - Replace the Ad Unit ID in `activity_main.xml` (line 20)

### Testing

The app currently uses Google's test ad IDs, which will show test ads. These are safe to use during development. Make sure to replace them with your real IDs before publishing to the Play Store.

## Notes

- The app uses `usesCleartextTraffic="true"` to allow HTTP connections (for development). For production, consider using HTTPS only.
- JavaScript is enabled in the WebView for full HTML5 functionality.
- The WebView handles back button navigation through its history.
- AdMob ads require an active internet connection to load.

