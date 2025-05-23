package com.example.cocktailapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Calendar

@Composable
fun TimerScreenContent(timerViewModel: TimerViewModel) {
    val timerValue by timerViewModel.timer.collectAsState()

    TimerScreen(
        timerValue = timerValue,
        onStartClick = { timerViewModel.startTimer() },
        onPauseClick = { timerViewModel.pauseTimer() },
        onStopClick = { timerViewModel.stopTimer() },
        onSetPreset = { timerViewModel.setTimerValue(it) }
    )
}

@Composable
fun TimerScreen(
    timerValue: Long,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    onSetPreset: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isDialogVisible by remember { mutableStateOf(false) }

        val presets = listOf(
            Triple(10L, "10s", Icons.Outlined.Schedule),
            Triple(20L, "20s", Icons.Outlined.Schedule),
            Triple(30L, "30s", Icons.Outlined.Schedule),
            Triple(60L, "1min", Icons.Outlined.Schedule),
            Triple(120L, "2min", Icons.Outlined.Schedule),
            Triple(300L, "5min", Icons.Outlined.Schedule)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in presets.chunked(3)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { (time, label, icon) ->
                        AssistChip(
                            onClick = { onSetPreset(time) },
                            label = { Text(label) },
                            leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
                            colors = AssistChipDefaults.assistChipColors()
                        )
                    }
                }
            }

            Button(
                onClick = { isDialogVisible = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Set Timer")
            }

            if (isDialogVisible) {
                DialExample(
                    onConfirm = { hour, min ->
                        onSetPreset((((hour * 60) + min) * 60).toLong())
                        isDialogVisible = false
                    },
                    onDismiss = {
                        isDialogVisible = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Timer(timerValue, onStartClick, onPauseClick, onStopClick)
    }
}


@Composable
fun Timer(
    timerValue: Long,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = timerValue.formatTime(), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onStartClick) {
                Text("Start")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onPauseClick) {
                Text("Pause")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onStopClick) {
                Text("Stop")
            }
        }
    }
}

fun Long.formatTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialExample(
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(
                state = timePickerState,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onDismiss) {
                    Text("Dismiss")
                }

                Button(onClick = {
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    onConfirm(hour, minute)
                }) {
                    Text("Confirm")
                }
            }
        }
    }
}
