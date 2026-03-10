import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {

        let bridge = StripePaymentBridge.shared

        MainViewControllerKt.registerBridge(
            initialize: { publishableKey in
                bridge.initialize(publishableKey: publishableKey)
            },
            processPayment : { clientSecret, completion in
                bridge.presentPaymentSheet(clientSecret: clientSecret){ result in
                    completion(result)
                }
            }
        )

        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}



