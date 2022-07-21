package com.mood.moodnews.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*

/**
 * Intercepts a key event rather than passing it on to children
 */
fun Modifier.interceptKey(key: Key, onKeyEvent: () -> Unit): Modifier {
    return this.onPreviewKeyEvent {
        if (it.key == key && it.type == KeyEventType.KeyUp) { // fire onKeyEvent on KeyUp to prevent duplicates
            onKeyEvent()
            true

        } else it.key == key // only pass the key event to children if it's not the chosen key
    }
}