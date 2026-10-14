package com.example.unit4cse226

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.unit4cse226.ui.theme.Unit4Cse226Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentScreen()

        }
    }
}

@Entity
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val course: String
)

@Dao
interface StudentDao {

    @Insert
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM Student")
    suspend fun getAllStudents(): List<Student>
}

@Database(
    entities = [Student::class],
    version = 1
)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
}

object DatabaseProvider {

    fun getDatabase(context: Context): StudentDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StudentDatabase::class.java,
            "student_database"
        ).build()
    }
}
class StudentViewModel(application: Application) :
    AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)

    var students by mutableStateOf<List<Student>>(emptyList())
        private set

    fun addStudent(name: String, course: String) {
        viewModelScope.launch {
            db.studentDao().insertStudent(
                Student(name = name, course = course)
            )
            loadStudents()
        }
    }

    fun loadStudents() {
        viewModelScope.launch {
            students = db.studentDao().getAllStudents()
        }
    }
}
@Composable
fun StudentScreen(
    viewModel: StudentViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadStudents()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Student Name") }
        )

        OutlinedTextField(
            value = course,
            onValueChange = { course = it },
            label = { Text("Course") }
        )

        Button(
            onClick = {
                if (name.isNotBlank() && course.isNotBlank()) {
                    viewModel.addStudent(name, course)
                    name = ""
                    course = ""
                }
            }
        ) {
            Text("Add Student")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(viewModel.students) { student ->
                Text(
                    text = "${student.name} - ${student.course}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}