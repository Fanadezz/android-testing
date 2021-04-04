package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.ITasksRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest{


    /*since we are testing Architecture Components we get InstantTaskExecutorRule*/
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    /*Since we are testing Coroutines and a ViewModel we get mainCoroutineRule*/

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //Subjects under test
    private lateinit var viewModel: StatisticsViewModel

    //Use a fake repository to be injected into the ViewModel
    private lateinit var repo: FakeTestRepository



    @Before
    fun initViewModel(){
        repo = FakeTestRepository()
        viewModel = StatisticsViewModel(repo)
    }

    @Test
    fun loadTasks_showLoading (){

        //GIVEN -  pause Coroutine right before Launch

        //pause dispatcher so you can verify initial values
        mainCoroutineRule.pauseDispatcher()

        //WHEN - when refreshing data icon swings by

        //load tasks in the viewModel
        viewModel.refresh()


        //THEN

        //assert athat the progress indicator is shown
        assertThat(viewModel.dataLoading.getOrAwaitValue(), `is` (true))

        //execute pending coroutine actions
        mainCoroutineRule.resumeDispatcher() // the codes executes immediately
        //after sometime the loading icon should disappear

        //Assert that the progress indicator is hidden
        assertThat(viewModel.dataLoading.getOrAwaitValue(), `is` (false))



    }

    @Test
    fun loadStatisticsWhenTasksAreUnavailable_callErrorToDisplay(){

        //Make the repository return errors
   repo.setReturnError(true)
viewModel.refresh()
        //Then assert that an error is displayed

        assertThat(viewModel.empty.getOrAwaitValue(), `is` (true))
        assertThat(viewModel.error.getOrAwaitValue(), `is` (true))
    }
}