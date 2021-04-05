package com.example.android.architecture.blueprints.todoapp.tasks

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.ITasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {


    private lateinit var repository: ITasksRepository

    @Before
    fun initRepository() {

        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository

    }

    @After

    fun cleanUpDB() = runBlockingTest {

        ServiceLocator.resetRepository()
    }

    @Test

    fun clickTask_navigateToDetailFragmentOne() = runBlockingTest {

        //GIVEN - On the tasks screen with 2 tasks

        repository.saveTask(Task("Title1", "Desc1", false, "id1"))
        repository.saveTask(Task("Title2", "Desc2", true, "id2"))

        val fragmentScenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)

        val navController = mock(NavController::class.java)

        //make fragmentScenario use navController i.e. attach NavController to fragment

        fragmentScenario.onFragment {

            //here we can call methods on the fragment iself
            Navigation.setViewNavController(it.view!!, navController)


        }

        //WHEN - Click on the first list item

        //use onView from espresso
        onView(withId(R.id.tasks_list))
                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(
                        withText("Title1")), click()))

        //THEN - Verify that we navigate to the first detail screens
        verify(navController).navigate(TasksFragmentDirections
                                               .actionTasksFragmentToTaskDetailFragment("id1"))

    }
    @Test
    fun clickAddTaskButton_navigateToAddEditFragment(){


        //GIVEN - inside the TasksFragment
        val navController = mock(NavController::class.java)
        val task = Task(title = "New Task", description = "", isCompleted = false)

//val bundle = TasksFragmentArgs(task.id).toBundle()
        val fragmentScenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)

        //WHEN  - click FAB Button
//attach navController to fragment
        fragmentScenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.add_task_fab)).perform(click())
        //THEN - Navigated to EditTaskFragment

        verify(navController).navigate(TasksFragmentDirections
                                               .actionTasksFragmentToAddEditTaskFragment(null,
                                                                                         task.title))

    }

}