package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
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


    /*Creating a TestCoroutineDispatcher that we can use with these tests*/
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()


    @Before
    fun createRepository() {


        val remoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        val localDataSource = FakeDataSource(localTasks.toMutableList())
        //create a test repo method to be called at the start of each test
        tasksRepository = DefaultTasksRepository(remoteDataSource,
                                                 localDataSource,
                                                 Dispatchers.Main)
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



    /*Whenever you call runBlockingTest, it creates a new TestCoroutineDispatcher if you don't
    specify one. MainCoroutineRule includes a TestCoroutineDispatcher. So, to ensure that you
    don't accidentally crate multiple instances of TestCoroutineDispatcher use mainCoroutineRule
    .runBlockingTest{}*/
    fun getTasks_requestsAllTasksFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
//when tasks are requested from tasks repository
        val tasks = tasksRepository.getTasks(false) as Result.Success
        //then tasks are loaded from remote data source
        assertThat(tasks.data, IsEqual(localTasks))

    }

/*{


    

        assertThat(tasks.data, IsEqualTo(remoteTasks))
    }*/

}