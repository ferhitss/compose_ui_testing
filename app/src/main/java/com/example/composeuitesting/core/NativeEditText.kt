package com.example.composeuitesting.core

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.TypedValue
import android.view.KeyEvent.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.compose.foundation.focusable
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import com.example.composeuitesting.R

object KeyboardVisibility {

    private val isVisible = mutableStateOf(false)

    fun setVisibility(visibility: Boolean) {
        isVisible.value = visibility
    }

    fun isKeyboardOpen(): State<Boolean> = isVisible
}

@Composable
fun NativeEditText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "",
    isPassword: Boolean = false,
    textSize: TextUnit = 18.sp,
    textColor: Int = R.color.input_text,
    placeholderColor: Int = R.color.input_placeholder,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    delayTimeMillis: Long = 0,
    lastChangeText: (String) -> Unit = {},
    icon: Int = 0
) {
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }
    val dpadCenterClicked = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val _textFlow = MutableStateFlow("")
    val textFlow: StateFlow<String> = _textFlow
    var inputMethodManager: InputMethodManager? = null

    LaunchedEffect(Unit) {
        scope.launch {
            @OptIn(FlowPreview::class)
            textFlow.debounce(delayTimeMillis)
                .collect { value ->
                    run {
                        lastChangeText(value)
                    }
                }
        }
    }

    AndroidView(
        modifier = Modifier
            .then(modifier)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused.value = it.isFocused }
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KEYCODE_DPAD_CENTER) {
                    dpadCenterClicked.value = true
                    return@onKeyEvent true
                }
                return@onKeyEvent false
            }
            .focusable(),
        factory = { context ->
            inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            EditText(context).apply {
                var countKeyPressed = 0

                background = null
                isSingleLine = true
                setPadding(0)

                setOnFocusChangeListener { et, isFocused ->
                    if (et !is EditText) return@setOnFocusChangeListener
                    if (isFocused) et.setSelection(et.length())
                    else hideKeyboard(et, inputMethodManager!!)
                    KeyboardVisibility.setVisibility(isFocused)
                }
                setOnEditorActionListener { _, actionId, _ ->
                    return@setOnEditorActionListener onEditorAction(actionId, keyboardActions) {
                        onValueChange(text.toString())
                        clearFocus()
                        focusRequester.requestFocus()
                    }
                }

                setOnKeyListener { et, _, keyEvent ->
                    countKeyPressed++
                    if (et !is EditText) return@setOnKeyListener false
                    if (countKeyPressed < 2) return@setOnKeyListener false

                    return@setOnKeyListener onKeyListener(et, keyEvent.keyCode, inputMethodManager) {
                        countKeyPressed = 0
                        onValueChange(text.toString())
                        clearFocus()
                        focusRequester.requestFocus()
                    }
                }

                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(editable: Editable?) {
                        if (!editable.isNullOrEmpty()) {
                            _textFlow.value = editable.toString()
                        }
                    }
                })
            }
        },
        update = {
            it.setText(value)
            it.isEnabled = enabled
            it.hint = placeholder
            it.setHintTextColor(ContextCompat.getColor(it.context, placeholderColor))
            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.value)
            it.setTextColor(ContextCompat.getColor(it.context, textColor))
            it.inputType = getInputType(keyboardOptions.keyboardType)
            it.imeOptions = getImeOptions(keyboardOptions.imeAction)
            if(it.text.isNotEmpty())
                it.setSelection(it.text.length)

            if (isPassword) {
                it.transformationMethod = PasswordTransformationMethod()
            } else {
                it.transformationMethod = null
            }

            if (isFocused.value && dpadCenterClicked.value) {
                it.requestFocus()
                dpadCenterClicked.value = false
            }
            if(icon != 0) {
                it.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
                it.compoundDrawablePadding = 20
            }
        }
    )
}


private fun onKeyListener(
    et: EditText,
    keyCode: Int,
    inputMethodManager: InputMethodManager?,
    resetFocus: () -> Unit
): Boolean {

    if (keyCode == KEYCODE_DPAD_UP && (inputMethodManager?.isActive == false)) {
        resetFocus()
        return true
    }

    if (keyCode == KEYCODE_DPAD_LEFT && et.selectionStart == 0 && (inputMethodManager?.isActive == false)) {
        resetFocus()
        return true
    }

    if (keyCode == KEYCODE_DPAD_DOWN && (inputMethodManager?.isActive == false)) {
        resetFocus()
        return true
    }

    if (keyCode == KEYCODE_DPAD_RIGHT && et.selectionStart == et.text.length && (inputMethodManager?.isActive == false)) {
        resetFocus()
        return true
    }

    if (keyCode == KEYCODE_BACK || keyCode == KEYCODE_ENTER) {
        resetFocus()
        return true
    }

    return false
}

private fun getInputType(keyboardType: KeyboardType): Int {
    return when (keyboardType) {
        KeyboardType.Text -> InputType.TYPE_CLASS_TEXT
        KeyboardType.Email -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        KeyboardType.Number -> InputType.TYPE_CLASS_NUMBER
        KeyboardType.Phone -> InputType.TYPE_CLASS_PHONE
        KeyboardType.Password -> InputType.TYPE_TEXT_VARIATION_PASSWORD
        KeyboardType.Uri -> InputType.TYPE_TEXT_VARIATION_URI
        else -> InputType.TYPE_CLASS_TEXT
    }
}

private fun getImeOptions(imeAction: ImeAction): Int {
    return when (imeAction) {
        ImeAction.Done -> EditorInfo.IME_ACTION_DONE
        ImeAction.Next -> EditorInfo.IME_ACTION_NEXT
        ImeAction.Previous -> EditorInfo.IME_ACTION_PREVIOUS
        ImeAction.Go -> EditorInfo.IME_ACTION_GO
        ImeAction.Search -> EditorInfo.IME_ACTION_SEARCH
        ImeAction.Send -> EditorInfo.IME_ACTION_SEND
        else -> EditorInfo.IME_ACTION_NONE
    }
}

private fun onEditorAction(
    actionId: Int,
    keyboardActions: KeyboardActions,
    resetFocus: () -> Unit
): Boolean {
    resetFocus()
    return when (actionId) {
        EditorInfo.IME_ACTION_DONE -> {
            keyboardActions.onDone?.invoke(EmptyKeyboardActionScope())
            true
        }
        EditorInfo.IME_ACTION_NEXT -> {
            keyboardActions.onNext?.invoke(EmptyKeyboardActionScope())
            true
        }
        EditorInfo.IME_ACTION_PREVIOUS -> {
            keyboardActions.onPrevious?.invoke(EmptyKeyboardActionScope())
            true
        }
        EditorInfo.IME_ACTION_GO -> {
            keyboardActions.onGo?.invoke(EmptyKeyboardActionScope())
            true
        }
        EditorInfo.IME_ACTION_SEARCH -> {
            keyboardActions.onSearch?.invoke(EmptyKeyboardActionScope())
            true
        }
        EditorInfo.IME_ACTION_SEND -> {
            keyboardActions.onSend?.invoke(EmptyKeyboardActionScope())
            true
        }
        else -> {
            false
        }
    }
}

private class EmptyKeyboardActionScope : KeyboardActionScope {
    override fun defaultKeyboardAction(imeAction: ImeAction) {}
}

private fun hideKeyboard(et: EditText, inputMethodManager: InputMethodManager) {
    inputMethodManager.hideSoftInputFromWindow(et.windowToken, 0)
}