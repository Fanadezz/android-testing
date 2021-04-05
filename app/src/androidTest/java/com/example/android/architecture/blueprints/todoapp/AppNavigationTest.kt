package com.example.android.architecture.blueprints.todoapp

import android.app.Activity
import android.view.Gravity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.ITasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import com.example.android.architecture.blueprints.todoapp.util.DataBindingIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest {

    private lateinit var tasksRepository: ITasksRepository

    //An Idling Resource that waits for Data Binding to have no pending bindings

    private val dataBindingIdlingResource = DataBindingIdlingResource()


    @Before
    fun init() {
        tasksRepository = ServiceLocator.provideTasksRepository(getApplicationContext())
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    /*Idling resources tell espresso that the app is idle or busy. This is needed when operations
    * are not scheduled in the main Looper (for example when executed on a different thread).*/


    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance()
                .register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance()
                .register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance()
                .unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance()
                .unregister(dataBindingIdlingResource)
    }


    @Test
    fun tasksScreen_clickOnDrawerIcon() {

        //Start the TasksScreen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //Check that left drawer is closed at startup

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)))
        //Open drawer by clicking on drawer icon

        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription())).perform(
                click())
        //Check if drawer is open

        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)))

        //When Using ActivityScenario.launch(), always call close()

        activityScenario.close()
    }

    @Test
    fun taskDetailScreen_doubleUpButton() = runBlocking {

        val task = Task("Up Button", "Description")
        tasksRepository.saveTask(task)


        //Start the TasksScreen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)


        //Click on the task on the list.

        onView(withText("Up Button")).perform(click())

        //click on the edit task button.

        onView(withId(R.id.edit_task_fab)).perform(click())
        //Confirm that if we click Up Button once, we end up back at the task details
        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription()))
                .perform(click())

        onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))

        //Confirm that if we click up button a second time we end up at the home screen

        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription()))
                .perform(click())

        //check with view
        //onView(withText("Up Button")).check(matches(isDisplayed()))

        //check with layout
onView(withId(R.id.tasks_container_layout)).check(matches(isDisplayed()))

        //When Using ActivityScenario.launch(), always call close()

        activityScenario.close()

    }

    @Test
    fun taskDetailScreen_doubleBackButton() = runBlocking {
        val task = Task("Up Button", "Description")
        tasksRepository.saveTask(task)

        //Start the TasksScreen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task on the list
        onView(withText("Up Button")).perform(click())

        //Click on the Edit task button.
        onView(withId(R.id.edit_task_fab)).perform(click())
        //Confirm that if we click Back once, we end up back at the task details page.

        pressBack()
        onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
        //Confirm that if we click Back a second time, we end up back at the home screen.
        pressBack()
        onView(withId(R.id.tasks_container_layout)).check(matches(isDisplayed()))
        //When Using ActivityScenario.launch(), always call close()
        activityScenario.close()

    }


}

fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription(): String {

    var description = ""
    onActivity {
        description = it.findViewById<Toolbar>(R.id.toolbar).navigationContentDescription as
                String
    }

    return description
}