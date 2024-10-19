//
//  StylusManager.swift
//  iosApp
//
//  Created by Ferdinand Bada on 18/10/2024.
//

// To receive updates, implement the delegate:
class StylusManager: NSObject, ApplePencilDetectorDelegate {
    let detector = ApplePencilDetector.shared()
    
    func setup() {
        detector.delegate = self
        detector.startMonitoring()
    }
    
    func pencilConnectionDidChange(_ isConnected: Bool) {
        // Handle connection state change
    }
    
    func pencilDrawingStateDidChange(_ isDrawing: Bool) {
        // Handle drawing state change
    }
}
