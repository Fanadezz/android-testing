package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Before

class DefaultTasksRepositoryTest {

    private val task1 = Task(title = "Title1", description = "Description1" )
    private val task2 = Task(title = "Title2", description = "Description2" )
    private val task3 = Task(title = "Title3", description = "Description3" )

    //simulate tasks inside local and remote repositories
    private val remoteTasks = listOf(task1, task2).sortedBy { it.id}
    private val localTasks = listOf(task3).sortedBy { it.id}
    //new tasks
    private val newTasks = listOf(task3).sortedBy{it.id}


    //class under test
    private lateinit var tasksRepository: DefaultTasksRepository

    //create a test repo method to be called at the start of each test

    @Before
    fun createRepository(){


        val remoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        val localDataSource = FakeDataSource(localTasks.toMutableList())

        tasksRepository = DefaultTasksRepository(remoteDataSource, localDataSource, Dispatchers.Unconfined)
    }


}