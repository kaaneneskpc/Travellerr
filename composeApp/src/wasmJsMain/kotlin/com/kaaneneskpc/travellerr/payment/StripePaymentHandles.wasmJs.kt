package com.kaaneneskpc.travellerr.payment

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class StripePaymentHandler actual constructor() {
    actual fun initialize(publishableKey: String) {
        storedPublishableKey = publishableKey
    }

    actual suspend fun processPayment(clientSecret: String): PaymentResult {
        return suspendCancellableCoroutine { cont ->
            processPaymentJs(
                clientSecret,
                storedPublishableKey,
                onSucces = {

                    cont.resume(PaymentResult.Success)
                },
                onError = { errorMessage ->
                    cont.resume(PaymentResult.Failure(errorMessage))
                },
                onCancel = {
                    cont.resume(PaymentResult.Cancelled)
                }
            )
        }
    }

    companion object {
        private var storedPublishableKey: String = ""
    }
}

@JsFun(
    """
    (clientSecret, publishableKey, onSuccess, onError, onCancel) => {
      
      const windowOpen = window.open('payment.html', 'stripePayment', 'width=500,height=600, menubar=no,toolbar=no,location=no,status=no,scrollbars=yes');
      if (!windowOpen) {
        onError('Failed to open payment window');
        return;
      }
      
     let settled = false;
     function finish(fn){ 
    if(settled) return;
    settled = true;
    window.removeEventListener('message', onMessage);
    clearInterval(checkWindowClosed);
    fn();
     }
      
    function onMessage(event) {
         if(event.source !== windowOpen) return;
         if(event.data.type === 'stripe_ready') {
            windowOpen.postMessage({ type: 'stripe_init', clientSecret, publishableKey }, location.origin);
         } else if (event.data.type === 'stripe_success') {
            finish(onSuccess);
         } else if (event.data.type === 'stripe_error') {
            finish(() => onError(event.data.errorMessage));
        
         } else if (event.data.type === 'stripe_cancel') {
            finish(onCancel);
         }
      }
      
      window.addEventListener('message', onMessage);
      
      const checkWindowClosed = setInterval(() => {
        if (windowOpen.closed) finish(onCancel);
      }, 500);
    
    }
"""
)
private external fun processPaymentJs(
    clientSecret: String,
    publishableKey: String,
    onSucces: () -> Unit,
    onError: (String) -> Unit,
    onCancel: () -> Unit
)