//
//  File.swift
//  iosApp
//
//  Created by Ferdinand Bada on 15/10/2024.
//

import CoreBluetooth

class PencilDetector: NSObject, CBCentralManagerDelegate {
    private var centralManager: CBCentralManager?

    override init() {
        super.init()
        centralManager = CBCentralManager(delegate: self, queue: nil)
    }

    func centralManagerDidUpdateState(_ central: CBCentralManager) {
        if central.state == .poweredOn {
            // UUID for Device Information Service
            let serviceUUID = CBUUID(string: "180A")
            let peripherals = centralManager?.retrieveConnectedPeripherals(withServices: [serviceUUID])

            if let peripherals = peripherals {
                for peripheral in peripherals {
                    if peripheral.name == "Apple Pencil" {
                        // The Apple Pencil is connected
                        pencilIsConnected = true
                    }
                }
            }
        }
    }

    // Shared property to determine if Pencil is connected
    private var pencilIsConnected: Bool = false

    func isPencilConnected() -> Bool {
        return pencilIsConnected
    }
}

