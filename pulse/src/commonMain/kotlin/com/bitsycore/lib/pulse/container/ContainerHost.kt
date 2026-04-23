package com.bitsycore.lib.pulse.container

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ContainerHost<STATE : Any, INTENT : Any, EFFECT : Any> {
	val stateFlow: StateFlow<STATE>
	val effectFlow: Flow<EFFECT>
	fun dispatch(intent: INTENT)
}