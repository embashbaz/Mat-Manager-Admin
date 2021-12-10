package com.example.matatumanageradmin.ui.fleetList

import com.example.matatumanageradmin.data.FakeRepository
import com.example.matatumanageradmin.utils.DispatcherProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named


@HiltAndroidTest
class FleetListViewModelTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_repo")
    lateinit var fakeRepository: FakeRepository

    @Inject
    @Named("dispatchers_test")
    lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setup(){
        hiltRule.inject()

    }



}