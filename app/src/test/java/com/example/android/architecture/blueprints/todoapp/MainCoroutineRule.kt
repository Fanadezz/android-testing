package com.example.android.architecture.blueprints.todoapp

import android.text.TextWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
/*JUnit Rule are classess where you define generic testing code that can be used to test
* that can execute before and after or during a test*/



/*By implementing TestCoroutineScope gives teh JUnit Rule the ability to control Coroutine
* timing using the testDispatcher*/
@ExperimentalCoroutinesApi
class MainCoroutineRule   constructor(val dispatcher: TestCoroutineDispatcher=
        TestCoroutineDispatcher()): TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher){
//BEFORE
    override fun starting(description: Description?) {
        super.starting(description)

    //swapping in the dispatcher
        Dispatchers.setMain(dispatcher)
    }


    //AFTER
    override fun finished(description: Description?) {
        super.finished(description)
//cleaning up
        Dispatchers.resetMain()
    }
}