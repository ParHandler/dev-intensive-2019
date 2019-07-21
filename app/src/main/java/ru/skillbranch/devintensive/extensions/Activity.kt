package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToLong
import android.util.TypedValue

fun Activity.hideKeyboard() {
    val imm:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}
/*
see details https://github.com/enyciaa/AndroidKeyboardStateChecker/blob/master/app/src/main/java/com/example/androidkeyboardstatechecker/KeyboardExtensions.kt
and https://proandroiddev.com/how-to-detect-if-the-android-keyboard-is-open-269b255a90f5
 */
fun Activity.isKeyboardOpen(): Boolean {
    val rootView = findViewById<View>(android.R.id.content)
    val visibleBounds = Rect()
    rootView.getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = rootView.height - visibleBounds.height()
    val marginOfError = convertDpToPx(50F).roundToLong()

    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed(): Boolean  = !isKeyboardOpen()

fun Context.convertDpToPx(dp: Float): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    )