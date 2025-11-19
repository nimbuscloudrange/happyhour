import SwiftUI
import WebKit

struct ContentView: View {
    // Replace with your actual AdMob banner ad unit ID
    // For testing, you can use: "ca-app-pub-3940256099942544/2934735716"
    private let adUnitID = "ca-app-pub-3940256099942544/2934735716"
    
    var body: some View {
        VStack(spacing: 0) {
            WebView()
                .edgesIgnoringSafeArea(.all)
            
            // Banner ad at the bottom
            AdMobBannerView(adUnitID: adUnitID)
                .frame(height: 50)
                .background(Color.white)
        }
    }
}

struct WebView: UIViewRepresentable {
    func makeCoordinator() -> Coordinator {
        Coordinator()
    }
    
    func makeUIView(context: Context) -> WKWebView {
        // Configure WKWebView to allow external resources
        let configuration = WKWebViewConfiguration()
        configuration.allowsInlineMediaPlayback = true
        configuration.mediaTypesRequiringUserActionForPlayback = []
        
        // Allow loading external resources
        let preferences = WKWebpagePreferences()
        preferences.allowsContentJavaScript = true
        configuration.defaultWebpagePreferences = preferences
        
        let webView = WKWebView(frame: .zero, configuration: configuration)
        
        // Enable navigation delegate to handle external URLs
        webView.navigationDelegate = context.coordinator
        
        // Load index.html from bundle
        if let htmlPath = Bundle.main.path(forResource: "index", ofType: "html") {
            let htmlURL = URL(fileURLWithPath: htmlPath)
            let htmlString = try? String(contentsOf: htmlURL, encoding: .utf8)
            webView.loadHTMLString(htmlString ?? "", baseURL: htmlURL.deletingLastPathComponent())
        }
        
        return webView
    }
    
    func updateUIView(_ webView: WKWebView, context: Context) {
        // No updates needed
    }
    
    // Coordinator to retain the navigation delegate
    class Coordinator: NSObject, WKNavigationDelegate {
        func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
            // Allow all navigation including external URLs
            decisionHandler(.allow)
        }
        
        func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
            print("WebView navigation error: \(error.localizedDescription)")
        }
    }
}

#Preview {
    ContentView()
}

