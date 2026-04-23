package com.bitsycore.lib.pulse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitsycore.lib.pulse.container.Container
import com.bitsycore.lib.pulse.container.ContainerContract
import com.bitsycore.lib.pulse.container.ContainerHost
import com.bitsycore.lib.pulse.internal.ExperimentalPulse
import com.bitsycore.lib.pulse.internal.UntypedIntentBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class PulseViewModel<STATE : Any, INTENT : Any, EFFECT : Any>(
	val containerContract: ContainerContract<STATE, INTENT, EFFECT>,
	initialState : STATE,
	restoredState: STATE? = null
) : ViewModel(), ContainerHost<STATE, INTENT, EFFECT> {

	override val effectFlow: Flow<EFFECT> get() = container.effectFlow
	override val stateFlow: StateFlow<STATE> get() = container.stateFlow

	private val container = object : Container<STATE, INTENT, EFFECT>(
		containerContract,
		initialState,
		restoredState,
		viewModelScope,
	) {
		override suspend fun handleIntent(intent: INTENT) = this@PulseViewModel.handleIntent(intent)
		override fun reduce(state: STATE, intent: INTENT): STATE = this@PulseViewModel.reduce(state, intent)
	}

	override fun dispatch(intent: INTENT) = container.dispatch(intent)

	@ExperimentalPulse
	protected fun dispatchCustom(block: UntypedIntentBuilder<STATE>.() -> Unit) = container.dispatchCustom(block)

	protected open fun reduce(state: STATE, intent: INTENT): STATE = containerContract.reduce(state, intent)
	protected open suspend fun handleIntent(intent: INTENT) {}

	protected fun updateState(block: STATE.() -> STATE) = container.updateState(block)
	protected fun emitEffect(effect: EFFECT) = container.emitEffect(effect)
}
