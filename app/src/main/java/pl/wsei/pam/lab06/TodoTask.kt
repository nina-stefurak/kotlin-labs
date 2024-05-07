package pl.wsei.pam.lab06

import java.time.LocalDate

data class TodoTask(
    val title: String,
    val deadline: LocalDate,
    val isDone: Boolean,
    val priority: Priority
)