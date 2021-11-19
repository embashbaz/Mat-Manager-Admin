package com.example.matatumanageradmin.ui.issues

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matatumanageradmin.data.Issue
import com.example.matatumanageradmin.data.MainRepository
import com.example.matatumanageradmin.utils.DispatcherProvider
import com.example.matatumanageradmin.utils.OperationStatus
import kotlinx.coroutines.launch
import javax.inject.Inject

class IssuesViewModel  @Inject constructor(val repository: MainRepository,
                                           private val dispatcher: DispatcherProvider
) : ViewModel(){

    private var _issueList = MutableLiveData<IssueStatus>( IssueStatus.Empty)
    val issueList: LiveData<IssueStatus>
        get() = _issueList

    private var _newIssueAction = MutableLiveData(false)
    val newIssueAction : LiveData<Boolean>
        get() = _newIssueAction

    private var _issueObject = MutableLiveData<Issue>()
    val issueObject: LiveData<Issue>
        get() = _issueObject


    fun setClickedIssueObject(issue: Issue){
        _issueObject.value = issue
    }

    fun getIssues(id: String, type: String){
        viewModelScope.launch(dispatcher.io){
            _issueList.postValue(IssueStatus.Loading)
            when(val response = repository.getIssues(type,id,"a","a")){
                is OperationStatus.Error -> _issueList.postValue(IssueStatus.Failed(response.message!!))
                is OperationStatus.Success -> {
                    if (response.data!!.isEmpty()){
                        _issueList.postValue( IssueStatus.Failed("No data was returned"))
                    }else{
                        _issueList.postValue( IssueStatus.Success("success", response.data))
                    }

                }
            }

        }

    }


    sealed class IssueStatus{
        class Success(val resultText: String, val issues: List<Issue>): IssueStatus()
        class Failed(val errorText: String): IssueStatus()
        object Loading: IssueStatus()
        object Empty: IssueStatus()
    }

}