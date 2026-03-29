package com.bitsycore.demo.pulse.page1

import androidx.lifecycle.Lifecycle
import com.bitsycore.demo.pulse.colorpicker.ColorPickerComponent
import com.bitsycore.lib.pulse.container.ContainerContract
import com.bitsycore.lib.pulse.savedstate.PulseSavedStateViewModel
import kotlinx.serialization.Serializable

object Page1Contract : ContainerContract<Page1Contract.UiState, Page1Contract.Intent, Page1Contract.Effect>() {

	override val initialState = UiState()

	typealias VM = PulseSavedStateViewModel<UiState, Intent, Effect>

	@Serializable
	data class UiState(
		val count: Int = 0,
		val colorPicker: ColorPickerComponent.State = ColorPickerComponent.initialState,
	)

	sealed interface Intent {
		data object Increment : Intent
		data object Decrement : Intent
		data object Reset : Intent
		data class ColorPicker(val intent: ColorPickerComponent.Intent) : Intent

		// Lifecycle-driven
		// Prefer Intent without lifecycle related name but for demo, simplify it
		data class OnLifecycle(val event: Lifecycle.Event) : Intent

		// Composition-driven
		data object OnScreenEntered : Intent
		data object OnScreenExited : Intent
	}

	sealed interface Effect {
		data class ShowToast(val message: String) : Effect
	}
}
