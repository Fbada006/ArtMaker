//
//  iosAppApp.swift
//  iosApp
//
//  Created by Caleb Langat on 14/10/2024.
//

import SwiftUI
import shared

@main
struct iosAppApp: App {
    
    init() {
        InputUtilsIosKt.doesIosDeviceHaveAStylus = {
            let detector = ApplePencilDetector.shared()
            let isConnected = detector.isPencilConnected
            print(isConnected)
            return KotlinBoolean(bool: isConnected)
        }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
