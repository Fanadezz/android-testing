package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest

class TasksLocalDataSourceTest {


    /*Executes each task synchronously using Architecture Components*/
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    //subjects under test
    private lateinit var localDataSource: TasksLocalDataSource
    private lateinit var database: ToDoDatabase


    @Before
    fun initDatabase() {

        database = Room.inMemoryDatabaseBuilder(getApplicationContext(), ToDoDatabase::class
                .java)
                .allowMainThreadQueries()
                .build()

        localDataSource = TasksLocalDataSource(database.taskDao(), Dispatchers.Main)
    }


    @After
    fun closeDatabase() {

        database.close()
    }


    @Test
    fun saveTask_retrievesTask() = runBlocking{

        //GIVEN - A new task saved in the database
        val newTask = Task("Title", "Description", false)

        localDataSource.saveTask(newTask)


        //WHEN - Task retrieved by ID
        val result =  localDataSource.getTask(newTask.id)


        //THEN - Same task is returned
        assertThat(result.succeeded, `is`(true))


result as Result.Success
        assertThat(result.data.description, `is` ("Description"))
        assertThat(result.data.title, `is` ("Title"))
        assertThat(result.data.isCompleted, `is` (false))
    }

    @Test
    fun completedTask_retrievedTaskIsComplete() = runBlocking{

        //GIVEN - Save a new active task in the local data source

        //isCompleted is false so the task is activeTask
val task = Task("Title", "Description", false)
        localDataSource.saveTask(task)


        //WHEN - Mark it as complete
        localDataSource.completeTask(task)

        //THEN - Check that the task can be retrieved from the local data source and is complete

        val loadedTask = localDataSource.getTask(task.id)


        assertThat(loadedTask.succeeded, `is`(true))
        loadedTask as Result.Success
        assertThat(loadedTask.data.isCompleted, `is`(true))
    }
}