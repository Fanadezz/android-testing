package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
@ExperimentalCoroutinesApi
class StatisticsViewModelTest{


    /*since we are testing Architecture Component we get InstantTaskExecutorRule*/
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    /*Since we are testing Coroutines and View Model we get mainCoroutineRule*/


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
}