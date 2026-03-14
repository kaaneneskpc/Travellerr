package com.kaaneneskpc.domain.session

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SessionManager {
    private val _sessionExpiredFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)
    val sessionExpiredFlow: SharedFlow<Unit> = _sessionExpiredFlow.asSharedFlow()

    fun emitSessionExpired() {
        _sessionExpiredFlow.tryEmit(Unit)
    }
}
