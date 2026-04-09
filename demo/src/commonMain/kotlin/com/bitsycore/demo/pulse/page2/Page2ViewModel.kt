package com.bitsycore.demo.pulse.page2

import androidx.lifecycle.Lifecycle
import com.bitsycore.lib.pulse.viewmodel.PulseViewModel

class Page2ViewModel : PulseViewModel<Page2Contract.UiState, Page2Contract.Intent, Page2Contract.Effect>(Page2Contract) {

	override val initialState: Page2Contract.UiState
		get() = Page2Contract.UiState()

	override suspend fun handleIntent(intent: Page2Contract.Intent) {
		when (intent) {
			// Log all lifecycle events
			is Page2Contract.Intent.OnLifecycle -> {
				when(intent.event) {
					Lifecycle.Event.ON_CREATE -> {
						emitEffect(Page2Contract.Effect.ShowToast("onCreate"))
						println("[Page2][Lifecycle] onCreate")
					}
					Lifecycle.Event.ON_START -> {
						emitEffect(Page2Contract.Effect.ShowToast("onStart"))
						println("[Page2][Lifecycle] onStart")
					}
					Lifecycle.Event.ON_RESUME -> {
						emitEffect(Page2Contract.Effect.ShowToast("onResume"))
						println("[Page2][Lifecycle] onResume")
					}
					Lifecycle.Event.ON_PAUSE -> println("[Page2][Lifecycle] onPause")
					Lifecycle.Event.ON_STOP -> println("[Page2][Lifecycle] onStop")
					Lifecycle.Event.ON_DESTROY -> println("[Page2][Lifecycle] onDestroy")
					else -> {}
                }
            }

			// Log all composition events
			Page2Contract.Intent.OnScreenEntered -> println("[Page2][Composition] onEnter")
			Page2Contract.Intent.OnScreenExited -> println("[Page2][Composition] onExit")
        }
	}

	override fun onCleared() = println("[Page2][ViewModel] onCleared")

	fun tick() = dispatchCustom {
		reduce {
			copy(count = count + 1)
		}
		handle {
			if (stateFlow.value.count % 5 == 0)
				emitEffect(Page2Contract.Effect.ShowToast("Counter incremented!"))
		}
	}
}
