package com.example.relatome.ui

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.relatome.R
import com.example.relatome.databinding.FragmentHomeBinding
import com.example.relatome.databinding.RelationshipItemRecyclerBinding
import com.example.relatome.domain.RelationshipDomainHome
import com.example.relatome.viewmodel.HomeViewModel
import com.example.relatome.viewmodel.LoginViewModel
import com.example.relatome.viewmodel.MainViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        val adapter = RelationshipAdapter()
        binding.relationshipRecycler.adapter = adapter
        homeViewModel = ViewModelProvider(this,
            HomeViewModel.Factory(requireActivity().application))
            .get(HomeViewModel::class.java)

        homeViewModel.login.observe(viewLifecycleOwner, Observer {
            binding.helloUser.text = getString(R.string.hello_user_string, it.name)
        })

        homeViewModel.relationshipList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        // Swipe to refresh
        binding.pullToRefreshRelationships.setOnRefreshListener {
            homeViewModel.refreshRelationships()
            binding.pullToRefreshRelationships.setRefreshing(false);
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        mainViewModel.updateToolbarTitle("Home")
    }
}

class RelationshipAdapter: ListAdapter<RelationshipDomainHome, RelationshipAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: RelationshipItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<RelationshipDomainHome>() {
        override fun areItemsTheSame(oldItem: RelationshipDomainHome, newItem: RelationshipDomainHome): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RelationshipDomainHome, newItem: RelationshipDomainHome): Boolean {
            return oldItem.as1Name == newItem.as1Name && oldItem.as2Name == newItem.as2Name && oldItem.relationship == newItem.relationship
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RelationshipItemRecyclerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.as1Name.text = item.as1Name
        holder.binding.as2Name.text = item.as2Name
        holder.binding.relatonship.text = item.relationship
    }
}