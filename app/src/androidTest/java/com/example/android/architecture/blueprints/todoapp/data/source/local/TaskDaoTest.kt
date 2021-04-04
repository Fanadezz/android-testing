package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest {

    //Executes each task synchronously using Architecture Components
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    //in order to get access to DAO we need a database
    lateinit var database: ToDoDatabase

    @Before
    fun initDatabase() {

        /*Normal Databases are meant to persist, in memory database will be completely deleted
        * once the process is killed and that is because it is never stored on disc */

        //initialize DB with Context, DB class
        database = Room.inMemoryDatabaseBuilder(getApplicationContext(),
                                                ToDoDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }


    //Tear down database
    @After
    fun tearDownDatabase() {

        database.close()
    }


    @Test
    fun insertTask_getByID() = runBlockingTest {

        //GIVEN - insert task

        val task1 = Task("Task 1", "Description 1", true)
        val task2 = Task("Task 2", "Description 3", true)

        database.taskDao()
                .insertTask(task1)

        //WHEN - get task by id from the database
        val returnedTask = database.taskDao()
                .getTaskById(task1.id)

        //THEN - the loaded data matches/contains the inserted task


        assertThat(returnedTask as Task, notNullValue()) // assert non-null task came back
        assertThat(returnedTask.id, `is`(task1.id))
        assertThat(returnedTask.title, `is`(task1.title))
        assertThat(returnedTask.description, `is`(task1.description))
        assertThat(returnedTask.isCompleted, `is`(task1.isCompleted))
    }


    @Test
  fun  updateTask_getById() = runBlockingTest {
      //GIVEN - insert task into the DAO
      val oldTask = Task("Task 1", "Description1", false)

        database.taskDao().insertTask(oldTask)

      //WHEN - update the task by creating a new task with the same ID but different attrs
        val newTask = Task("New Task", "New Desc", true, oldTask.id)
        database.taskDao().updateTask(newTask)
        val loadedTask = database.taskDao().getTaskById(newTask.id)

      //THEN - check that when you get the taks by its ID, ih has the update values
        assertThat(loadedTask as Task, notNullValue())
        assertThat(loadedTask.id, `is`(oldTask.id))
        assertThat(loadedTask.title, `is`(newTask.title))
        assertThat(loadedTask.description, `is`(newTask.description))
        assertThat(loadedTask.isCompleted, `is`(newTask.isCompleted))




    }
}