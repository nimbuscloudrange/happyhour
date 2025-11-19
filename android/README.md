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

- `INTERNET`: Required for loading web data
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

## Notes

- The app uses `usesCleartextTraffic="true"` to allow HTTP connections (for development). For production, consider using HTTPS only.
- JavaScript is enabled in the WebView for full HTML5 functionality.
- The WebView handles back button navigation through its history.

