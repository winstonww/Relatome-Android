package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.relatome.R
import com.example.relatome.databinding.FragmentReviseBNBinding
import com.example.relatome.databinding.ReviseRelationshipItemRecyclerBinding
import com.example.relatome.domain.RelationshipDomainHome
import com.example.relatome.domain.RelationshipResponseDomainRevise
import com.example.relatome.viewmodel.HomeViewModel
import com.example.relatome.viewmodel.ReviseViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 * Use the [ReviseBNFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReviseBNFragment : Fragment() {

    lateinit var binding: FragmentReviseBNBinding
    lateinit var viewModel: ReviseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReviseBNBinding.inflate(layoutInflater, container, false)

        val adapter = ReviseRelationshipAdapter(OnClick{ relationshipId ->
            findNavController().navigate(
                ReviseBNFragmentDirections.actionReviseBNFragmentToFillReviseRelationshipFragment(
                    relationshipId
                ))
        })
        binding.reviseRecycler.adapter = adapter

        viewModel = ViewModelProvider(this,
            ReviseViewModel.Factory(requireActivity().application))
            .get(ReviseViewModel::class.java)

        viewModel.relationshipResponseList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        ViewCompat.setNestedScrollingEnabled(binding.reviseRecycler, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.contributeBNFragment -> {
                    findNavController().navigate(ReviseBNFragmentDirections.actionReviseBNFragmentToContributeBNFragment())
                    true
                }
                R.id.homeBNFragment -> {
                    findNavController().navigate(ReviseBNFragmentDirections.actionReviseBNFragmentToHomeBNFragment())
                    true
                }
                R.id.reviseBNFragment -> {
                    true
                }
                else -> true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavigation.selectedItemId = R.id.reviseBNFragment

    }
}

class ReviseRelationshipAdapter(val onClick: OnClick) :
    ListAdapter<RelationshipResponseDomainRevise, ReviseRelationshipAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<RelationshipResponseDomainRevise>() {
        override fun areItemsTheSame(oldItem: RelationshipResponseDomainRevise, newItem: RelationshipResponseDomainRevise): Boolean {
            return oldItem.relationshipId == newItem.relationshipId
        }

        override fun areContentsTheSame(oldItem: RelationshipResponseDomainRevise, newItem: RelationshipResponseDomainRevise): Boolean {
            return oldItem.as1Name == newItem.as1Name && oldItem.as2Name == newItem.as2Name && oldItem.relationship == newItem.relationship
        }
    }

    class ViewHolder(val binding: ReviseRelationshipItemRecyclerBinding ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ReviseRelationshipItemRecyclerBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.as1Name.text = item.as1Name
        holder.binding.as2Name.text = item.as2Name
        holder.binding.relatonship.text = item.relationship
        holder.binding.root.setOnClickListener {
            onClick.action(item.relationshipId)
        }
    }

}