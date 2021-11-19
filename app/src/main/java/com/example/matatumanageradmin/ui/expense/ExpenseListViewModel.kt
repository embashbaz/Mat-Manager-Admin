package com.example.matatumanageradmin.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.Expense
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseListViewModel  @Inject constructor(val repository: MainRepository,
                                                private val dispatcher: DispatcherProvider
) : ViewModel(){


    private var _expenseList = MutableLiveData<ExpenseStatus>(ExpenseStatus.Empty)
    val expenseList: LiveData<ExpenseStatus>
        get() = _expenseList

    private var _expenseObject = MutableLiveData<Expense>()
    val expenseObject: LiveData<Expense>
        get() = _expenseObject



    fun setClickedExpenseObject(expense: Expense){
        _expenseObject.value = expense
    }

    fun getExpenseList(id: String, type: String){

        viewModelScope.launch(dispatcher.io){
            _expenseList.postValue(ExpenseStatus.Loading)
            when(val response = repository.getExpenses(type,id,"a","a")){
                is OperationStatus.Error -> ExpenseStatus.Failed(response.message!!)
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _expenseList.postValue( ExpenseStatus.Failed("No data was returned"))
                    }else{
                        _expenseList.postValue(ExpenseStatus.Success("success", response.data))
                    }

                }
            }
        }
    }




    sealed class ExpenseStatus{
        class Success(val resultText: String, val expenses: List<Expense>): ExpenseStatus()
        class Failed(val errorText: String): ExpenseStatus()
        object Loading: ExpenseStatus()
        object Empty: ExpenseStatus()
    }
}