import UIKit
import SwiftUI
import shared


struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.mainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    
    init () {
        IExportHelperKt.triggerIosSharing = {
         print("IOS sharing triggered")
        }
    }
    
    var body: some View {
        ComposeView()
    }
}
