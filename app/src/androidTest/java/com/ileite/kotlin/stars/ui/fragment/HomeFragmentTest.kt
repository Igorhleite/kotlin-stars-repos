package com.ileite.kotlin.stars.ui.fragment

import android.content.Context
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.SmallTest
import com.ileite.kotlin.stars.AndroidTestUtils.getEntityMockList
import com.ileite.kotlin.stars.R
import com.ileite.kotlin.stars.launchFragmentInHiltContainer
import com.ileite.kotlin.stars.ui.adapter.RemoteRepositoriesAdapter
import com.ileite.kotlin.stars.utils.fromEntitiesToModels
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@SmallTest
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var fragment: HomeFragment

    private lateinit var testContext: Context

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Before
    fun setup() {
        hiltRule.inject()
        launchFragment()
    }

    private fun launchFragment() {
        launchFragmentInHiltContainer<HomeFragment> {
            fragment = this as HomeFragment
            testContext = this.requireContext()
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.homeFragment)
            this.viewLifecycleOwnerLiveData.observeForever { lifeCycle ->
                if (lifeCycle != null) {
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }
    }

    @Test
    fun fragmentMustBeInitializedAndShowTitle(): Unit = runBlocking {
        initAdapter()
        Espresso.onView(withId(R.id.tv_app_name))
            .check(ViewAssertions.matches(withText(R.string.first_app_name)))
        Espresso.onView(withId(R.id.tv_app_name_light))
            .check(ViewAssertions.matches(withText(R.string.second_app_name)))
    }

    @Test
    fun recyclerViewMustBeInitializedAndShowListOfRepositories(): Unit = runBlocking {
        initAdapter()
        Espresso.onView(withId(R.id.rc_repositories))
            .perform()
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        assert(fragment.repositoriesAdapter.itemCount > 0)
    }

    @Test
    fun recyclerViewMustBeInitializedAndPossibleScroll(): Unit = runBlocking {
        initAdapter()
        Espresso.onView(withId(R.id.rc_repositories))
            .perform(
                RecyclerViewActions.scrollToPosition<RemoteRepositoriesAdapter.ViewHolder>(
                    50
                )
            )
    }

    private suspend fun initAdapter() {
        fragment.repositoriesAdapter
            .submitData(PagingData.from(getEntityMockList(50)
                .fromEntitiesToModels()))
    }
}