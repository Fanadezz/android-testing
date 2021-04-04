package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
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
}