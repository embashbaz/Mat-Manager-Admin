package com.example.matatumanageradmin.ui.expense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageradmin.R
import com.example.matatumanageradmin.databinding.FragmentExpenseListBinding
import com.example.matatumanageradmin.ui.other.DefaultRecyclerAdapter
import com.example.matatumanageradmin.utils.Constant


class ExpenseListFragment : Fragment() {

    private lateinit var expenseListBinding: FragmentExpenseListBinding
    private val expenseListViewModel : ExpenseListViewModel by viewModels()
    private lateinit var  defaultRecyclerAdapter: DefaultRecyclerAdapter
    private var type = ""
    private var recordId = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        expenseListBinding = FragmentExpenseListBinding.inflate(inflater,container, false)
        val view = expenseListBinding.root
        defaultRecyclerAdapter = DefaultRecyclerAdapter { expense -> onExpenseClicked(expense) }
        type = arguments?.getString("expense_type")!!
        recordId = arguments?.getString("record_id")!!

        getExpenses()

        return view
    }

    private fun onExpenseClicked(expense: Any) {
        TODO("Not yet implemented")
    }

    private fun getExpenses() {
        expenseListViewModel.getExpenseList(recordId, type)
        expenseListViewModel.expenseList.observe(viewLifecycleOwner, {
            when(it){
                is ExpenseListViewModel.ExpenseStatus.Success -> {
                    if(!it.expenses.isEmpty()) {
                        defaultRecyclerAdapter.setData(it.expenses as ArrayList<Any>)
                        defaultRecyclerAdapter.notifyDataSetChanged()
                        setRecyclerView()
                        hideNoDataText()
                        hideProgressBar()
                    }else{
                        showNoDataText()
                    }
                }

                is ExpenseListViewModel.ExpenseStatus.Failed -> {
                    showNoDataText()
                    hideProgressBar()
                }

                is ExpenseListViewModel.ExpenseStatus.Loading -> {
                    showProgressBar()
                    hideNoDataText()
                }


            }

        })
    }

    fun setRecyclerView(){
        expenseListBinding.expenseListRecycler.layoutManager = LinearLayoutManager(activity)
        expenseListBinding.expenseListRecycler.adapter = defaultRecyclerAdapter
    }

    private fun hideNoDataText(){
        expenseListBinding.noDataTxtExpenseList.visibility = View.INVISIBLE
    }

    private fun hideProgressBar(){
        expenseListBinding.progressBarExpenseList.visibility = View.INVISIBLE
    }

    private fun showNoDataText(){
        expenseListBinding.noDataTxtExpenseList.visibility = View.VISIBLE
    }

    private fun showProgressBar(){
        expenseListBinding.progressBarExpenseList.visibility = View.VISIBLE
    }


}