package pl.wsei.pam.lab06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

}




