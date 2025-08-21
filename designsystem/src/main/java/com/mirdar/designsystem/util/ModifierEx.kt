package com.mirdar.designsystem.util

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester

fun FocusRequester.safeRequestFocus(onCatch: ((IllegalStateException) -> Unit)? = null) =
    try {
        requestFocus()
    } catch (t: IllegalStateException) {
        onCatch?.invoke(t)
    }

@OptIn(ExperimentalComposeUiApi::class)
fun FocusRequester.safeRestoreFocusedChild(onCatch: ((IllegalStateException) -> Unit)? = null): Boolean {
    return try {
        restoreFocusedChild()
    } catch (e: IllegalStateException) {
        onCatch?.invoke(e)
        false
    }
}

inline fun Modifier.conditional(
    condition: Boolean,
    ifFalse: Modifier.() -> Modifier = { this },
    ifTrue: Modifier.() -> Modifier,
): Modifier = if (condition) {
    then(ifTrue(Modifier))
} else {
    then(ifFalse(Modifier))
}
