package com.example.matatumanageradmin.ui.issues

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentIssueBinding
import com.example.matatumanageradmin.ui.other.DefaultRecyclerAdapter


class IssueFragment : Fragment() {


    private lateinit var issueBinding: FragmentIssueBinding
    private val issueListViewModel: IssuesViewModel by viewModels()
    private lateinit var  defaultRecyclerAdapter: DefaultRecyclerAdapter
    private var recordId = ""
    private var type = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        issueBinding = FragmentIssueBinding.inflate(inflater, container, false)
        val view = issueBinding.root

        type = arguments?.getString("issue_type")!!
        recordId = arguments?.getString("record_id")!!

        defaultRecyclerAdapter = DefaultRecyclerAdapter { issue -> onIssueClicked(issue) }

        getIssues()

        return view
    }

    private fun onIssueClicked(issue: Any) {

    }

    private fun getIssues(){

        issueListViewModel.getIssues(recordId, type)
        issueListViewModel.issueList.observe(viewLifecycleOwner, {
            when(it){
                is IssuesViewModel.IssueStatus.Success -> {

                    if(!it.issues.isEmpty()){

                        defaultRecyclerAdapter.setData(it.issues as ArrayList<Any>)
                        setRecyclerView()
                        hideNoDataText()
                        hideProgressBar()
                    }else{
                        showNoDataText()
                    }
                }
                is IssuesViewModel.IssueStatus.Failed ->{
                    showNoDataText()
                    hideProgressBar()
                }

                is IssuesViewModel.IssueStatus.Loading ->{
                    showProgressBar()
                    hideNoDataText()
                }
            }
        })
    }

    fun setRecyclerView(){
        issueBinding.issueListRecycler.layoutManager = LinearLayoutManager(activity)
        issueBinding.issueListRecycler.adapter = defaultRecyclerAdapter
    }

    private fun hideNoDataText(){
        issueBinding.noDataTxtIssueList.visibility = View.INVISIBLE
    }

    private fun hideProgressBar(){
        issueBinding.progressBarIssueList.visibility = View.INVISIBLE
    }

    private fun showNoDataText(){
        issueBinding.noDataTxtIssueList.visibility = View.VISIBLE
    }

    private fun showProgressBar(){
        issueBinding.progressBarIssueList.visibility = View.VISIBLE
    }

}