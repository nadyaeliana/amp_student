package com.example.student.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student.R
import com.example.student.databinding.FragmentStudentListBinding
import com.example.student.viewmodel.ListViewModel


class StudentListFragment : Fragment() {
    private lateinit var viewModel: ListViewModel
    private val studentListAdapter  = StudentListAdapter(arrayListOf())
    private lateinit var binding: FragmentStudentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragmentZ
        binding = FragmentStudentListBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init viewModel
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        //init recycle view
        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.adapter = studentListAdapter

        //implement swiperefresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeRefresh.isRefreshing = false
        }


        observeViewModel()

    }

    fun observeViewModel() {
        viewModel.studentsLD.observe(viewLifecycleOwner, Observer {
            studentListAdapter.updateStudentList(it)
        })
        viewModel.studentLoadErrorLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.txtError?.visibility = View.VISIBLE
            } else {
                binding.txtError?.visibility = View.GONE
            }
        })
        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.recView.visibility = View.GONE
                binding.progressLoad.visibility = View.VISIBLE
            } else {
                binding.recView.visibility = View.VISIBLE
                binding.progressLoad.visibility = View.GONE
            }
        })


    }


}