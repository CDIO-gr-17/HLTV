package com.example.hltv

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.hltv", appContext.packageName)
    }
}

class NavControllerTest {
    /*
    @Test
    fun testNavigateToMatches() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(startDestinationArgs = null)
        navController.setCurrentDestination(Home.route)
        assertEquals("Current destination in not home ", navController.currentDestination?.route, Home.route)
        navController.navigate(Matches.route)
        assertEquals("Current destination in not matches ", navController.currentDestination?.route, Matches.route)
    }*/

}