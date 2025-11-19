# Happy Hour iOS App

A SwiftUI iOS app that displays HTML content from the app bundle using WKWebView.

## Features

- ✅ Modern SwiftUI app structure
- ✅ Loads HTML content from bundle
- ✅ Uses WKWebView for web content display
- ✅ Configured for external URL loading (APIs, images, etc.)
- ✅ Location permissions for geolocation features
- ✅ Google AdMob integration for banner ads
- ✅ Compatible with Xcode

## Project Structure

```
Happy Hour/
├── Happy_HourApp.swift    # Main app entry point (@main)
├── ContentView.swift       # SwiftUI view with WKWebView
├── AdMobBannerView.swift   # AdMob banner ad component
└── index.html             # HTML file to display
```

## Setup Instructions

### 1. Create New Xcode Project

1. Open Xcode
2. Select **File** → **New** → **Project**
3. Choose **iOS** → **App**
4. Configure your project:
   - **Product Name**: `Happy Hour`
   - **Interface**: SwiftUI
   - **Language**: Swift
   - **Use Core Data**: No
   - **Include Tests**: Optional

### 2. Add Files to Project

1. **Replace the default app file** with `Happy_HourApp.swift`
   - Delete the default `App.swift` or `Happy_HourApp.swift` if it exists
   - Add the provided `Happy_HourApp.swift` to your project

2. **Add ContentView.swift**
   - Delete the default `ContentView.swift` if it exists
   - Add the provided `ContentView.swift` to your project

3. **Add AdMobBannerView.swift**
   - Add the provided `AdMobBannerView.swift` to your project

4. **Add index.html**
   - Right-click on your project in the navigator
   - Select **Add Files to "Happy Hour"...**
   - Select `index.html`
   - **Important**: In the dialog, make sure:
     - ✅ **Copy items if needed** is checked
     - ✅ Your app target is checked under **Add to targets**
     - Click **Add**

### 3. Add Google Mobile Ads SDK

1. **Using Swift Package Manager (Recommended)**:
   - In Xcode, select **File** → **Add Packages...**
   - Enter the URL: `https://github.com/googleads/swift-package-manager-google-mobile-ads.git`
   - Click **Add Package**
   - Select **Google-Mobile-Ads-SDK** and click **Add Package**
   - Make sure your app target is selected

2. **Alternative: Using CocoaPods**:
   - If you prefer CocoaPods, create a `Podfile` in your project root:
     ```ruby
     platform :ios, '14.0'
     use_frameworks!
     
     target 'Happy Hour' do
       pod 'Google-Mobile-Ads-SDK'
     end
     ```
   - Run `pod install` in Terminal
   - Open the `.xcworkspace` file instead of `.xcodeproj`

### 4. Configure AdMob App ID

1. **Get your AdMob App ID**:
   - Go to [Google AdMob](https://admob.google.com/)
   - Create an account or sign in
   - Create a new app (or use existing)
   - Copy your **App ID** (format: `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`)

2. **Add App ID to Xcode**:
   - Select your project → Target → **Info** tab
   - Under **Custom iOS Target Properties**, add:
     - Key: `GADApplicationIdentifier`
     - Type: `String`
     - Value: Your AdMob App ID (e.g., `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`)

### 5. Configure Ad Unit ID

1. **Get your Banner Ad Unit ID**:
   - In AdMob dashboard, go to your app
   - Click **Ad units** → **Add ad unit**
   - Select **Banner**
   - Copy the **Ad unit ID** (format: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`)

2. **Update ContentView.swift**:
   - Open `ContentView.swift`
   - Find the line: `private let adUnitID = "ca-app-pub-3940256099942544/2934735716"`
   - Replace the test ad unit ID with your actual AdMob banner ad unit ID

   **Note**: The default value is a Google test ad unit ID. Replace it with your own for production.

### 6. Configure Permissions and App Transport Security

In Xcode, permissions and security settings are configured directly in the target settings:

1. **Select your project** in the navigator
2. **Select your app target** (Happy Hour)
3. Go to the **Info** tab
4. Under **Custom iOS Target Properties**, add the following keys:

   **Location Permissions:**
   - Key: `Privacy - Location When In Use Usage Description`
     - Type: `String`
     - Value: `This app needs access to your location to show nearby happy hour spots.`
   - Key: `Privacy - Location Always and When In Use Usage Description`
     - Type: `String`
     - Value: `This app needs access to your location to show nearby happy hour spots.`

5. **Configure App Transport Security** (for external URLs):
   - In the same **Info** tab, expand **App Transport Security Settings**
   - Add **Exception Domains** for each external domain:
     - **maps.googleapis.com**
       - `NSIncludesSubdomains`: `YES`
       - `NSExceptionAllowsInsecureHTTPLoads`: `NO`
       - `NSExceptionRequiresForwardSecrecy`: `YES`
       - `NSExceptionMinimumTLSVersion`: `TLSv1.2`
     - **wcu.co**
       - `NSIncludesSubdomains`: `YES`
       - `NSExceptionAllowsInsecureHTTPLoads`: `NO`
       - `NSExceptionRequiresForwardSecrecy`: `YES`
       - `NSExceptionMinimumTLSVersion`: `TLSv1.2`
     - **images.unsplash.com**
       - `NSIncludesSubdomains`: `YES`
       - `NSExceptionAllowsInsecureHTTPLoads`: `NO`
       - `NSExceptionRequiresForwardSecrecy`: `YES`
       - `NSExceptionMinimumTLSVersion`: `TLSv1.2`

   **Note**: If you don't see "App Transport Security Settings" in the Info tab, you can add it manually:
   - Click the **+** button
   - Type: `App Transport Security Settings`
   - Type: `Dictionary`
   - Then add `Exception Domains` as a Dictionary inside it

### 7. Verify Bundle Membership

1. Select `index.html` in the project navigator
2. In the File Inspector (right panel), verify:
   - **Target Membership**: Your app target should be checked ✅
   - This ensures the HTML file is included in the app bundle

### 8. Build and Run

1. Select your target device or simulator
2. Press **⌘R** (or click the Run button)
3. The app should launch and display "Hello World" from the HTML file

## How It Works

- **Happy_HourApp.swift**: Uses the `@main` attribute to mark the app entry point. This is the modern SwiftUI app lifecycle. Initializes the Google Mobile Ads SDK on app launch.

- **ContentView.swift**: Contains a SwiftUI view that wraps a `WKWebView` (via `UIViewRepresentable`) to load and display the HTML content from the bundle. The WebView is configured to allow external resources and JavaScript execution. Includes a banner ad at the bottom.

- **AdMobBannerView.swift**: A SwiftUI wrapper for Google AdMob banner ads. Displays banner ads using `GADBannerView` and handles ad loading events.

- **index.html**: The HTML file is loaded from the app bundle at runtime and displayed in the WebView. It can load external resources like APIs, images, and scripts.

- **Target Settings**: In Xcode, permissions and App Transport Security are configured directly in the target's Info tab, not through a separate Info.plist file.

## Customization

### Modify HTML Content

Edit `index.html` to change the displayed content. The file supports:
- HTML5 markup
- CSS styling
- JavaScript (if needed)

### Modify App Appearance

Edit `ContentView.swift` to customize:
- WebView configuration
- Safe area handling
- Additional SwiftUI views

## Requirements

- Xcode 12.0 or later
- iOS 14.0 or later
- Swift 5.0 or later

## Troubleshooting

### HTML file not loading?

1. Verify `index.html` is added to the target:
   - Select `index.html` in project navigator
   - Check File Inspector → Target Membership

2. Verify the file is in the bundle:
   - The file should appear in your project navigator
   - Make sure "Copy items if needed" was checked when adding

3. Check the file name:
   - Must be exactly `index.html` (case-sensitive)

### Build errors?

- Ensure you're using Xcode 12.0 or later
- Clean build folder: **Product** → **Clean Build Folder** (⇧⌘K)
- Verify all Swift files compile without errors
- If using Swift Package Manager, ensure the Google Mobile Ads SDK package is properly added
- If using CocoaPods, make sure you're opening the `.xcworkspace` file, not `.xcodeproj`

### AdMob ads not showing?

1. **Verify AdMob App ID**:
   - Check that `GADApplicationIdentifier` is set in your target's Info tab
   - Ensure the App ID format is correct (starts with `ca-app-pub-`)

2. **Verify Ad Unit ID**:
   - Check `ContentView.swift` has your actual ad unit ID (not the test ID)
   - Ensure the ad unit ID format is correct

3. **Check AdMob account**:
   - Make sure your AdMob account is approved
   - Verify your app is added in AdMob dashboard
   - Check that ad units are created and active

4. **Test ads**:
   - Use Google's test ad unit IDs for testing:
     - Banner: `ca-app-pub-3940256099942544/2934735716`
   - Test ads will always load, even if your account isn't approved yet

5. **Check console logs**:
   - Look for AdMob error messages in Xcode console
   - Common issues: invalid ad unit ID, network errors, account issues

## External URL Permissions

The app needs to load external resources from:
- **Google Maps API** (`maps.googleapis.com`) - For map functionality
- **API Server** (`wcu.co`) - For location data
- **Image CDN** (`images.unsplash.com`) - For images
- **Google AdMob** (`googleads.g.doubleclick.net`, `googlesyndication.com`) - For ads

These domains must be configured in your target's **Info** tab under **App Transport Security Settings** → **Exception Domains** (see Setup Instructions step 6).

**Note**: AdMob domains are typically handled automatically by the SDK, but you may need to add them if you encounter loading issues:
- `googleads.g.doubleclick.net`
- `googlesyndication.com`
- `googleadservices.com`

### Adding More Domains

To add additional domains:
1. Select your project → Target → **Info** tab
2. Expand **App Transport Security Settings** → **Exception Domains**
3. Click the **+** button to add a new domain
4. Configure the same settings as the existing domains

### Location Permissions

The app requests location permissions to:
- Get the user's current location
- Show nearby happy hour spots
- Calculate distances

The permission descriptions are configured in the target's **Info** tab (see Setup Instructions step 3):
- `Privacy - Location When In Use Usage Description`
- `Privacy - Location Always and When In Use Usage Description`

## AdMob Integration

The app includes Google AdMob for displaying banner ads:

- **Banner Ad**: Displayed at the bottom of the screen
- **SDK Initialization**: Automatically initialized when the app launches
- **Test Mode**: Uses Google's test ad unit ID by default (replace with your own for production)

### Getting Started with AdMob

1. **Create AdMob Account**: Sign up at [admob.google.com](https://admob.google.com/)
2. **Create App**: Add your iOS app in the AdMob dashboard
3. **Create Ad Unit**: Create a banner ad unit and copy the ad unit ID
4. **Configure**: Add your App ID and Ad Unit ID as described in Setup Instructions

### AdMob Requirements

- iOS 14.0 or later
- Google Mobile Ads SDK (added via Swift Package Manager or CocoaPods)
- Valid AdMob App ID and Ad Unit ID
- AdMob account approval (for production ads)

## Notes

- This app uses the modern SwiftUI app lifecycle with `@main` (no Info.plist required)
- The HTML file is loaded from the app bundle but can fetch external data
- WKWebView provides full web rendering capabilities including CSS and JavaScript
- App Transport Security (ATS) is configured through Xcode's target settings (Info tab)
- Location permissions are configured through Xcode's target settings (Info tab)
- All permissions and security settings are managed directly in Xcode without a separate Info.plist file
- AdMob SDK is initialized automatically on app launch

