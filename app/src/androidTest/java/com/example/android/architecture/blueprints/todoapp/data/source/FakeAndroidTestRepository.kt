package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class FakeAndroidTestRepository : ITasksRepository {

    //represents the data that comes back from the database and network
    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()


    //contains a list of observableTasks. This is so that we can return something
    //from the observed tasks
    private val observableTasks = MutableLiveData<Result<List<Task>>>()


    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {

        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {


        runBlocking { refreshTasks() }
        return observableTasks
    }

    override suspend fun refreshTask(taskId: String) {

        refreshTasks()

    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        runBlocking { refreshTasks() }

        return observableTasks.map { tasks -> when(tasks){
            is Result.Loading -> Result.Loading
            is Result.Error -> Result.Error(tasks.exception)
            is Result.Success -> {
                val task = tasks.data.firstOrNull(){it.id ==taskId} ?: return@map Result.Error(Exception("Not Found"))
                Result.Success(task)
            }
        }
        }
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {

        //IF-NOT-NULL

        //perform null-check first
        tasksServiceData[taskId]?.let {

            return Result.Success(it)
        }

        //IF-NULL

        return Result.Error(Exception("Could not find the task"))


    }

    override suspend fun saveTask(task: Task) {

        tasksServiceData[task.id] = task
    }

    override suspend fun completeTask(task: Task) {

        val completedTask = Task(task.title, task.description, true,task.id)
        tasksServiceData[task.id] = completedTask

    }

    override suspend fun completeTask(taskId: String) {

       /* val task = tasksServiceData[taskId]
        task?.isCompleted = true*/

        //Not required for the remote data source
        throw NotImplementedError()

    }

    override suspend fun activateTask(task: Task) {
    val activeTask = Task(task.title, task.description, false,task.id)

        tasksServiceData[task.id] = activeTask
    }

    override suspend fun activateTask(taskId: String) {
        //Not required for the remote data source
        throw NotImplementedError()
    }

    override suspend fun clearCompletedTasks() {
        tasksServiceData = tasksServiceData.filterValues {

            !it.isCompleted
        } as LinkedHashMap<String,Task>
    }

    override suspend fun deleteAllTasks() {
        tasksServiceData.clear()
        refreshTasks()
    }

    override suspend fun deleteTask(taskId: String) {

        refreshTasks()
    }

/*When testing you may need some way to modify the state of the repository
* as a convenience this addTasks method is specifically meant for testing
*
* runBlocking is used here but it not actually within the test code.
*
* RunBlocking is a much closer simulation to what a real implementation of the
* repo would do and therefore, it is preferable for fakes so that behaviour
* more closely matches the real implementation*/

    fun addTasks(vararg tasks: Task) {

        for (task in tasks) {

            tasksServiceData[task.id] = task
        }

        runBlocking { refreshTasks() }
    }
}