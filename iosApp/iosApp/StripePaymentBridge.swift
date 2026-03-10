import Foundation
import UIKit
import StripePaymentSheet

public class StripePaymentBridge {

    public static let shared = StripePaymentBridge()

    private init() {}

    func initialize(publishableKey: String) {

        StripeAPI.defaultPublishableKey  = publishableKey
    }

    func presentPaymentSheet( clientSecret: String, completion: @escaping (String) -> Void) {

        var config = PaymentSheet.Configuration()
        config.merchantDisplayName = "Trevnor"


        let paymentSheet = PaymentSheet(
            paymentIntentClientSecret: clientSecret, configuration: config
        )

        DispatchQueue.main.async {

            guard let viewController = self.topViewController() else {
                completion("No Screen Available")
                return
            }

            paymentSheet.present(from: viewController) { result in

                switch result {
                case .completed: completion("success")
                case .canceled: completion("canceled")
                case .failed : completion("failed")
                }
            }
        }

    }


    private func topViewController() -> UIViewController? {
         guard let windowScene = UIApplication.shared.connectedScenes
             .compactMap({ $0 as? UIWindowScene })
             .first,
               let rootViewController = windowScene.windows
                 .first(where: { $0.isKeyWindow })?
                 .rootViewController
         else {
             return nil
         }

         var topVC = rootViewController
         while let presented = topVC.presentedViewController {
             topVC = presented
         }
         return topVC
     }

}