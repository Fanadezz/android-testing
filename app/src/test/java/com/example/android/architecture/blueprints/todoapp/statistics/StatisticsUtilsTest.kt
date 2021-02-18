package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {

    /*If there is no completed task and one active task is
    then there are 100% active tasks and 0% completed tasks*/


    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsZeroPercent() {


        val tasks = listOf(Task("title" , "description" , isCompleted = false))


        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f , result.completedTasksPercent)
        assertEquals(100f , result.activeTasksPercent)
    }


    /*If there are 2 completed tasks and 3 active tasks
    then there are 40% completed tasks and 60% active tasks*/

    @Test
    fun getActiveAndCompletedStats_TwoCompleted_returnsSixtyPercent() {


        val tasks =
                listOf(Task(title = "task 1" , description = "" , isCompleted = true) ,
                        Task(title = "task 2" , description = "" , isCompleted = false) ,
                        Task(title = "task 3" , description = "" , isCompleted = false) ,
                        Task(title = "task 4" , description = "" , isCompleted = true) ,
                        Task(title = "task 5" , description = "" , isCompleted = false))
        val result = getActiveAndCompletedStats(tasks)

        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }


    @Test
    fun taskListEmpty_returnZero() {

        val tasks = listOf<Task>()

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)

    }

    @Test
   fun taskListNull_returnZero() {

       val tasks = null

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.activeTasksPercent)
        assertEquals(0f, result.completedTasksPercent)

    }

}