package pl.wsei.pam.lab06

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.wsei.pam.lab06.ui.theme.Lab06Theme
import java.time.LocalDate

class NavigationActivity : ComponentActivity() {
    private val taskList = mutableListOf(
        TodoTask("Programming", LocalDate.of(2024, 4, 18), false, Priority.Low),
        TodoTask("Teaching", LocalDate.of(2024, 5, 12), false, Priority.High),
        TodoTask("Learning", LocalDate.of(2024, 6, 28), true, Priority.Low),
        TodoTask("Cooking", LocalDate.of(2024, 8, 18), false, Priority.Medium),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab06Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onContentChanged() {
        super.onContentChanged()
    }

    @Composable
    @Preview(showBackground = true)
    fun MainScreen() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "list") {
            composable("list") { ListScreen(navController = navController) }
            composable("form") { FormScreen(navController = navController) }
        }
    }

    @Composable
    fun ListScreen(navController: NavController) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add task",
                            modifier = Modifier.scale(1.5f)
                        )
                    },
                    onClick = {
                        navController.navigate("form")
                    }
                )
            },
            topBar = {
                AppTopBar(
                    navController = navController,
                    title = "Lista",
                    showBackIcon = false,
                    route = "list",
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "")
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = "")
                        }
                    }
                )
            }) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(items = taskList) { item ->
                    ListItem(item = item)
                }
            }
        }
    }

    enum class Priority {
        High, Medium, Low
    }

    data class TodoTask(
        val title: String,
        val deadline: LocalDate,
        val isDone: Boolean,
        val priority: Priority
    )

    @Composable
    fun ListItem(item: TodoTask, modifier: Modifier = Modifier) {
        ElevatedCard(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(120.dp)
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Row(modifier = Modifier.padding(8.dp)) {
                Column {
                    Text(text = "Title", style = MaterialTheme.typography.titleSmall)
                    Text(text = item.title, style = MaterialTheme.typography.titleLarge)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = "Deadline",
                        textAlign = TextAlign.Right,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = item.deadline.toString(),
                        textAlign = TextAlign.Right,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Row(modifier = Modifier.padding(8.dp)) {
                Column {
                    Text(text = "Priority")
                    Text(
                        text = item.priority.toString(),
                        textAlign = TextAlign.Right,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                if(item.isDone) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
                } else {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

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

    @Composable
    fun FormScreen(navController: NavController) {
        var title by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        var deadline by rememberSaveable { mutableStateOf(LocalDate.now()) }
        var isDone by rememberSaveable { mutableStateOf(false) }
        var selectedPriority by rememberSaveable { mutableStateOf(Priority.Low) }

        Scaffold(
            topBar = {
                AppTopBar(
                    navController = navController,
                    title = "Form",
                    showBackIcon = true,
                    route = "list",
                    actions = {
                        OutlinedButton(
                            onClick = {
                                taskList.add(
                                    TodoTask(
                                        title = title.text,
                                        deadline = deadline,
                                        isDone = isDone,
                                        priority = selectedPriority
                                    )
                                )
                                navController.navigate("list")
                            }
                        )
                        {
                            Text(
                                text = "Zapisz",
                                fontSize = 18.sp
                            )
                        }
                    }
                )
            }) { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            Column(modifier = modifier) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Enter title") },
                    singleLine = true
                )
                MyDatePicker(
                    onDateSelected = { deadline = LocalDate.parse(it) },
                    onDismiss = { "" },
                    modifier = Modifier.fillMaxWidth()
                )
                Column {
                    Text("Priority", style = MaterialTheme.typography.titleSmall)
                    Priority.entries.forEach { priority ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { selectedPriority = priority }
                                .fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedPriority == priority,
                                onClick = { selectedPriority = priority }
                            )
                            Text(text = priority.name)
                        }
                    }
                }
                Column {
                    Text("Is done?", style = MaterialTheme.typography.titleSmall)
                    Checkbox(
                        checked = isDone,
                        onCheckedChange = { isDone = it }
                    )
                }
            }

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppTopBar(
        navController: NavController,
        title: String,
        showBackIcon: Boolean,
        route: String,
        actions: @Composable RowScope.() -> Unit = {}
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            title = { Text(text = title) },
            navigationIcon = {
                if (showBackIcon) {
                    IconButton(onClick = { navController.navigate(route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
            actions = actions
        )
    }

}




