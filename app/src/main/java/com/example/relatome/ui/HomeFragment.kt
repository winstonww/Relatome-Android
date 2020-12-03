package com.example.relatome.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.relatome.R
import com.example.relatome.databinding.FragmentHomeBinding
import com.example.relatome.databinding.RelationshipItemRecyclerBinding
import com.example.relatome.domain.RelationshipDomainHome
import com.example.relatome.viewmodel.HomeStatus
import com.example.relatome.viewmodel.HomeViewModel
import com.example.relatome.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


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

        homeViewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                HomeStatus.LOADING -> binding.homeProgressBar.visibility = View.VISIBLE
                HomeStatus.TIMEOUT -> {
                    Snackbar.make(binding.root, "Connection timeout, Retrying...", Snackbar.LENGTH_LONG).show()
                    homeViewModel.refreshRelationships()
                }
                HomeStatus.NO_CONNECTION -> {
                    Snackbar.make(binding.root, "No Connection. Please try again.", Snackbar.LENGTH_LONG).show()

                }
                else -> binding.homeProgressBar.visibility = View.GONE
            }
        })

        // Swipe to refresh
        binding.pullToRefreshRelationships.setOnRefreshListener {
            homeViewModel.refreshRelationships()
            binding.pullToRefreshRelationships.setRefreshing(false);
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAs1NameInputFragment())
        }

        // Attach itemtouchhelper to recyclerview
        val ithCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Toast.makeText(requireContext(), "on swipe", Toast.LENGTH_SHORT).show()

                // a hack to make delete action smoother
                val position = viewHolder.adapterPosition
                val list = homeViewModel.relationshipList.value?.toMutableList()
                list?.removeAt(position)
                adapter.submitList(list)

                homeViewModel.deleteRelationship((viewHolder as RelationshipAdapter.ViewHolder).item)
            }
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> {
                    // Respond to navigation item 1 reselection
                    true
                }
                R.id.contributeHomeFragment -> {
                    // Respond to navigation item 2 reselection
                    findNavController().navigate(R.id.contributeHomeFragment)
                    true
                }
                else -> true

            }
        }

        val itemTouchHelper = ItemTouchHelper(ithCallback)
        itemTouchHelper.attachToRecyclerView(binding.relationshipRecycler)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.refreshRelationships()
    }

}


class RelationshipAdapter: ListAdapter<RelationshipDomainHome, RelationshipAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: RelationshipItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        var item : RelationshipDomainHome? = null
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RelationshipDomainHome>() {
        override fun areItemsTheSame(oldItem: RelationshipDomainHome, newItem: RelationshipDomainHome): Boolean {
            return oldItem.id == newItem.id
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
        holder.item = item
        Timber.i("Here in bindviewholder2")
    }
}