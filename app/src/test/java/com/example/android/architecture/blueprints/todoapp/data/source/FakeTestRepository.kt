package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking

class FakeTestRepository : ITasksRepository {


    //represents the data that comes back from the database and network
    var tasksServiceData:LinkedHashMap<String,Task> = LinkedHashMap()


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
        TODO("Not yet implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

/*When testing you may need some way to modify the state of the repository
* as a convenience this addTasks method is specifically meant for testing
*
* runBlocking is used here but it not actually within the test code.
*
* RunBlocking is a much closer simulation to what a real implementation of the
* repo would do and therefore, it is preferable for fakes so that behaviour
* more closely matches the real implementation*/

    fun addTasks(vararg tasks: Task){

        for (task in tasks){

            tasksServiceData[task.id] = task
        }

        runBlocking { refreshTasks() }
    }

}