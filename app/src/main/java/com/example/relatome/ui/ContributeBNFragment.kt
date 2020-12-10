package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.relatome.R
import com.example.relatome.databinding.FragmentContributeBNBinding
import com.example.relatome.databinding.PendingRelationshipItemRecyclerBinding
import com.example.relatome.domain.PendingRelationshipDomainContribute
import com.example.relatome.viewmodel.ContributeLoadingStatus
import com.example.relatome.viewmodel.ContributeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [ContributeBNFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContributeBNFragment : Fragment() {

    private lateinit var contributeViewModel: ContributeViewModel
    private lateinit var binding: FragmentContributeBNBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContributeBNBinding.inflate(inflater, container, false)

        val adapter = ContributeBNAdapter( OnClick {
            id -> findNavController().navigate(ContributeBNFragmentDirections.actionContributeBNFragmentToFillRelationshipFragment(id))
        })
        binding.contributeRecycler.adapter = adapter

        contributeViewModel = ViewModelProvider(this,
            ContributeViewModel.Factory(requireActivity().application))
            .get(ContributeViewModel::class.java)

        contributeViewModel.pendingRelationshipList.observe(viewLifecycleOwner, Observer {
            Timber.i(it.toString())
            adapter.submitList(it)

        })

        contributeViewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when(it) {
                ContributeLoadingStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                ContributeLoadingStatus.TIMEOUT -> {
                    Snackbar.make(binding.root, "Connection Timeout. Retrying...", Snackbar.LENGTH_LONG).show()
                    contributeViewModel.refreshPendingRelationship()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

        // Swipe to refresh
        binding.pullToRefreshPendingRelationships.setOnRefreshListener {
            lifecycleScope.launchWhenStarted {
                contributeViewModel.refreshPendingRelationship()
            }
            binding.pullToRefreshPendingRelationships.setRefreshing(false);
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeBNFragment -> {
                    // Respond to navigation item 1 reselection
                    findNavController().navigate(ContributeBNFragmentDirections.actionContributeBNFragmentToHomeBNFragment())
                    true
                }
                R.id.contributeBNFragment -> {
                    // Respond to navigation item 2 reselection
                    true
                }
                R.id.reviseBNFragment -> {
                    findNavController().navigate(ContributeBNFragmentDirections.actionContributeBNFragmentToReviseBNFragment())
                    true
                }
                else -> true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            contributeViewModel.refreshPendingRelationship()
            Timber.i("On start refresh pending relationship")
        }
        binding.bottomNavigation.setSelectedItemId(R.id.contributeBNFragment)
    }
}

class OnClick(val action: (String) -> Unit)

class ContributeBNAdapter(val onClick: OnClick):
    androidx.recyclerview.widget.ListAdapter<PendingRelationshipDomainContribute, ContributeBNAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: PendingRelationshipItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<PendingRelationshipDomainContribute>() {
        override fun areItemsTheSame(oldItem: PendingRelationshipDomainContribute, newItem: PendingRelationshipDomainContribute): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PendingRelationshipDomainContribute, newItem: PendingRelationshipDomainContribute): Boolean {
            return oldItem.as1Name == newItem.as1Name && oldItem.as2Name == newItem.as2Name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PendingRelationshipItemRecyclerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.as1Name.text = item.as1Name
        holder.binding.as2Name.text = item.as2Name
        holder.binding.postedAt.text = item.postedAt.split("T").first()
        holder.binding.root.setOnClickListener { onClick.action(item.id) }
//        holder.binding.postedAt.text = item.postedAt
    }
}