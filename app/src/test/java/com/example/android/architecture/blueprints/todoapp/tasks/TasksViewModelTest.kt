package com.example.android.architecture.blueprints.todoapp.tasks

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
//@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {


     //Subject under test
    private lateinit var viewModel:TasksViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpViewModel() {


        val tasksRepository = FakeTestRepository()

        val task1 = Task(title = "Title1", description = "Description 1")
        val task2 = Task(title = "Title2", description = "Description 2")
        val task3 = Task(title = "Title3", description = "Description 3")

        tasksRepository.addTasks(task1, task2, task3)

        viewModel = TasksViewModel(tasksRepository)
    }


    @Test
    fun addNewTask_setsUpNewTasks() {
        //GIVEN a fresh ViewModel
        //val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())


        //WHEN adding a new task
       viewModel.addNewTask()

        //THEN the new task event is triggered
        val value = viewModel.newTaskEvent.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))

    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible(){

        //GIVEN a fresh viewModel
        //val viewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        //WHEN the filter type is ALL_TASKS
       viewModel.setFiltering(TasksFilterType.ALL_TASKS)

        //THEN "Add Task" action is visible
        /*(getOrAwaitValue() is from the LiveDataTestUtil class)*/
        val value =viewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(value, `is` (true))
    }
}