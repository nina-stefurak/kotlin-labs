package pl.wsei.pam.lab06

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier?
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var initDate = LocalDate.now().toEpochDay() * 86400000
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initDate)
    val source = remember { MutableInteractionSource() }
    source.interactions.onEach { interaction ->
        if (interaction is PressInteraction.Press) {
            Log.d("MyDatePicker", "Released...")
            showDatePicker = true
        }
    }.launchIn(CoroutineScope(Dispatchers.Default))
    val selectedDate = datePickerState.selectedDateMillis?.let {
        LocalDate.ofEpochDay(it / 86400000).toString()
    } ?: ""

    Row(modifier = modifier ?: Modifier
        .fillMaxWidth()
        .clickable {
            Log.d("MyDatePicker", "Clicked")
            showDatePicker = true
        }) {
        if (!showDatePicker) {
            TextField(
                value = TextFieldValue(selectedDate),
                interactionSource = source,
                onValueChange = {},
                readOnly = true,
                label = { Text("Deadline") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )
        } else {
            DatePickerDialog(
                onDismissRequest = { onDismiss() },
                confirmButton = {
                    Button(onClick = {
                        onDateSelected(selectedDate)
                        onDismiss().also { showDatePicker = false }
                    }

                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        onDismiss().also { showDatePicker = false }
                    }) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }
}