package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.ITasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Array.get


/*ServiceLocator class able to construct and store a repository*/
object ServiceLocator {
    private val lock = Any()
private var database: ToDoDatabase ? = null

    @Volatile //for use by multiple threads
    var tasksRepository: ITasksRepository? = null

    //call set method ( not callable on main source set but on the test source set)

    @VisibleForTesting
    set


    fun provideTasksRepository(context: Context): ITasksRepository {

        //ensure no more that one instance is created at a time
        synchronized(this) {

            return tasksRepository ?: createTasksRepository(context)
        }


    }

    private fun createTasksRepository(context: Context): ITasksRepository {


        val newRepo = DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))

        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TasksDataSource {

        val database  = database ?: createDatabase(context)
        return TasksLocalDataSource(database.taskDao())

    }

    private fun createDatabase(context: Context): ToDoDatabase {

        val result = Room.databaseBuilder(context.applicationContext,ToDoDatabase::class.java,
                                          "Tasks.db").build()


        database = result
        return result
    }


    //ensure the repo is reset to the same state each time/ cleaning up any
    // will only need to reset the repository from within the test
    @VisibleForTesting
    fun resetRepository(){

        synchronized(lock){

            runBlocking {

                TasksRemoteDataSource.deleteAllTasks()
            }


            //clear all data to avoid pollution issues
            database?.apply {

                clearAllTables()
                close()
            }

            database = null
            tasksRepository = null
        }
    }


}