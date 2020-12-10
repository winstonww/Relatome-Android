package com.example.relatome.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.relatome.R
import com.example.relatome.databinding.FragmentHomeBNBinding
import com.example.relatome.databinding.RelationshipItemRecyclerBinding
import com.example.relatome.domain.RelationshipDomainHome
import com.example.relatome.utils.slideUp
import com.example.relatome.viewmodel.HomeStatus
import com.example.relatome.viewmodel.HomeViewModel
import com.example.relatome.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Use the [HomeBNFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeBNFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentHomeBNBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBNBinding.inflate(inflater, container, false)

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
                HomeStatus.LOADING -> {
                    binding.homeProgressBar.visibility = View.VISIBLE
                    binding.homeOuterLayout.visibility = View.INVISIBLE
                }
                HomeStatus.TIMEOUT -> {
                    Snackbar.make(binding.root, "Connection timeout, Retrying...", Snackbar.LENGTH_LONG).show()
                    homeViewModel.refreshRelationships()
                }
                HomeStatus.NO_CONNECTION -> {
                    Snackbar.make(binding.root, "No Connection. Please try again.", Snackbar.LENGTH_LONG).show()

                }
                else -> {
                    binding.homeProgressBar.visibility = View.GONE
                    binding.homeOuterLayout.visibility = View.VISIBLE
                    binding.homeOuterLayout.slideUp(1000L, 0)
                }
            }
        })

        // Swipe to refresh
        binding.pullToRefreshRelationships.setOnRefreshListener {
            homeViewModel.refreshRelationships()
            binding.pullToRefreshRelationships.setRefreshing(false);
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(HomeBNFragmentDirections.actionHomeFragmentToAs1NameInputFragment())
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

        val itemTouchHelper = ItemTouchHelper(ithCallback)
        itemTouchHelper.attachToRecyclerView(binding.relationshipRecycler)

        binding.relationshipRecycler.setNestedScrollingEnabled(false)

//        ViewCompat.setNestedScrollingEnabled(binding.relationshipRecycler, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavigation.setSelectedItemId(R.id.homeBNFragment)
        homeViewModel.refreshRelationships()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.contributeBNFragment -> {
                    // Respond to navigation item 1 reselection
                    findNavController().navigate(HomeBNFragmentDirections.actionHomeBNFragmentToContributeBNFragment())
                    true
                }
                R.id.homeBNFragment -> {
                    // Respond to navigation item 2 reselection
                    true
                }
                R.id.reviseBNFragment -> {
                    findNavController().navigate(HomeBNFragmentDirections.actionHomeBNFragmentToReviseBNFragment())
                    true
                }
                else -> true
            }
        }
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