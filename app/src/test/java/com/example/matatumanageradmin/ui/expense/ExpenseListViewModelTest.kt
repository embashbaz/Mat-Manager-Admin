package com.example.matatumanageradmin.ui.expense

import androidx.test.filters.SmallTest
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
class ExpenseListViewModelTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var viewModelTest: ExpenseListViewModel

    @Inject
    @Named("test_repo")
    lateinit var fakeRepository: FakeRepository

    @Inject
    @Named("dispatchers_test")
    lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setup(){
        hiltRule.inject()
        viewModelTest = ExpenseListViewModel(fakeRepository, dispatcherProvider)

    }



}