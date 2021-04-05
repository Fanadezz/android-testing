package com.example.android.architecture.blueprints.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
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
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class) //we are using  AndroidX Test
@LargeTest //end to end test
class TasksActivityTest {
    private lateinit var repository: ITasksRepository

    @Before
    fun  init() {
        //no fake repository used
        repository = ServiceLocator.provideTasksRepository(getApplicationContext())


        /*this is not really a test so it doesn't neede access to test coroutine dispatcher*/
        runBlocking{

            //clean up to ensure that there are no previous state from the other tests
            repository.deleteAllTasks()
        }
    }

    @After
    fun reset() {

        ServiceLocator.resetRepository()
    }

    private val dataBindingIdlingResource = DataBindingIdlingResource()
    @Before
    fun registerIdlingResource() {

        /*By registering these 2 resources in your test, when either of these 2
        * resources is busy espresso will wait until they are idle before moving
        * to the next command*/

        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)

    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun editTask() = runBlocking {
        //set Initial State
        repository.saveTask(Task("Title 1", "Description"))

        //Start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        //monitor activity for pending data binding
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //Click on the task on the list and verify that all data is correct
        onView(withText("Title 1")).perform(click())

        onView(withId(R.id.task_detail_title_text)).check(matches(withText("Title 1")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("Description")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        //Click on the edit button, edit and save
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("New Title"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("New Description"))
        onView(withId(R.id.save_task_fab)).perform(click())

        //Verify task is displayed on the screen in the task
        onView(withText("New Title")).check(matches(isDisplayed()))

        //Verify previous task is not displayed

        onView(withText("Title 1")).check(doesNotExist())




        //Ensure the activity is closed before resetting the database
        activityScenario.close()
    }

    @Test
    fun createOneTask_deleteTask() {

        // 1. Start TasksActivity.

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        //monitor activity for pending data binding
        dataBindingIdlingResource.monitorActivity(activityScenario)
        // 2. Add an active task by clicking on the FAB and saving a new task.

        onView(withId(R.id.add_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("Title 1"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("Description"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // 3. Open the new task in a details view.
        onView(withText("Title 1")).perform(click())

        // 4. Click delete task in menu.
        onView(withId(R.id.menu_delete)).perform(click())

        // 5. Verify it was deleted.
        onView(withText("Title 1")).check(doesNotExist())

        // 6. Make sure the activity is closed.

        activityScenario.close()

    }


}