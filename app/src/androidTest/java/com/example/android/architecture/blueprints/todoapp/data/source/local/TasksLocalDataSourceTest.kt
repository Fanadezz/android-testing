package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Rule



class TasksLocalDataSourceTest{


    /*Executes each task synchronously using Architecture Components*/
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
}