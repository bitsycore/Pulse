package com.bitsycore.lib.pulse.container

import com.bitsycore.lib.pulse.internal.ExperimentalPulse
import com.bitsycore.lib.pulse.internal.UntypedIntentBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

/**
 * Base class for MVI ViewModels.
 *
 * @param STATE  Immutable UI state type.
 * @param INTENT  Intent (user action or lifecycle event) type.
 * @param EFFECT  One-time Effect (navigation, toasts, etc.) type.
 *
 * The [containerContract] provides [ContainerContract.initialState], making the contract the single
 * source of truth for State, Intent, Effect, and the starting state value.
 *
 * Data flow:
 *   UI → dispatch(Intent) → reduce() → new State → UI recomposes
 *                        ↘ handleIntent() → async work → emitEffect() → Screen reacts
 */
abstract class Container<STATE : Any, INTENT : Any, EFFECT : Any>(
	val containerContract: ContainerContract<STATE, INTENT, EFFECT>,
	initialState: STATE,
	restoredState: STATE? = null,
	val coroutineScope: CoroutineScope,
) : ContainerHost<STATE, INTENT, EFFECT> {

	private val stateMutableFlow = MutableStateFlow(restoredState ?: initialState)
	override val stateFlow: StateFlow<STATE> = stateMutableFlow.asStateFlow()

	private val effectMutableFlow = MutableSharedFlow<EFFECT>(extraBufferCapacity = 8)
	override val effectFlow: Flow<EFFECT> = effectMutableFlow.asSharedFlow()

	/** Entry point for all UI-originated actions. Thread-safe. */
	override fun dispatch(intent: INTENT) {
		stateMutableFlow.update { reduce(it, intent) }
		coroutineScope.launch { handleIntent(intent) }
	}

	@ExperimentalPulse
	fun dispatchCustom(block: UntypedIntentBuilder<STATE>.() -> Unit) {
		UntypedIntentBuilder(
			stateMutableFlow,
			coroutineScope
		).apply(block).build()
	}

	/** Pure, synchronous state reducer. Override to handle state transitions. */
	protected open fun reduce(state: STATE, intent: INTENT): STATE = containerContract.reduce(state, intent)

	/** Long operation handler. Override to perform async work (network, NFC, etc.). */
	protected open suspend fun handleIntent(intent: INTENT) {}

	/** Emits a one-time effect to the screen. Thread-safe */
	fun emitEffect(effect: EFFECT) {
		coroutineScope.launch { yield(); effectMutableFlow.emit(effect) }
	}

	/** Convenience for updating state outside of the reducer (e.g., inside callbacks). */
	fun updateState(block: STATE.() -> STATE) = stateMutableFlow.update(block)
}