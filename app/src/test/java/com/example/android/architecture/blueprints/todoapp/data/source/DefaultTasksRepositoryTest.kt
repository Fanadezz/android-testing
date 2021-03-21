package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test

class DefaultTasksRepositoryTest {

    private val task1 = Task(title = "Title1", description = "Description1")
    private val task2 = Task(title = "Title2", description = "Description2")
    private val task3 = Task(title = "Title3", description = "Description3")

    //simulate tasks inside local and remote repositories
    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }

    //new tasks
    private val newTasks = listOf(task3).sortedBy { it.id }


    //class under test
    private lateinit var tasksRepository: DefaultTasksRepository

    //create a test repo method to be called at the start of each test

    @Before
    fun createRepository() {


        val remoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        val localDataSource = FakeDataSource(localTasks.toMutableList())
//create repository
        tasksRepository = DefaultTasksRepository(remoteDataSource,
                                                 localDataSource,
                                                 Dispatchers.Unconfined)
    }


    //create test
    @ExperimentalCoroutinesApi
    @Test

            /*Coroutine test lib gives us access access to a function called Run Blocking Test
                 Run-Blocking Test lets us take a block of code and runs that block of code in a special
                test coroutine context which make sure that the code is run synchronously and
                 immediately

                 You should you Run Blocking Test when calling a suspend function
                */
    fun getTasks_requestsAllTasksFromRemoteDataSource() = runBlocking {
//when tasks are requested from tasks repository
        val tasks = tasksRepository.getTasks(false) as Result.Success
        //then tasks are loaded from remote data source
        assertThat(tasks.data, IsEqual(localTasks))

    }

/*{


    

        assertThat(tasks.data, IsEqualTo(remoteTasks))
    }*/

}