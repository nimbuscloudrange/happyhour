import SwiftUI
import GoogleMobileAds

@main
struct Happy_HourApp: App {
    init() {
        // Initialize Google Mobile Ads SDK
        GADMobileAds.sharedInstance().start(completionHandler: nil)
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

