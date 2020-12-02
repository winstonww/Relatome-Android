package com.example.relatome.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.relatome.databinding.AsnameItemRecyclerBinding
import com.example.relatome.databinding.FragmentAs1NameSuggestionBinding
import com.example.relatome.domain.AsNameDomainAsNameSuggestion
import com.example.relatome.viewmodel.As1NameViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.selects.select
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [As1NameSuggestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class As1NameSuggestionFragment : Fragment() {

    private lateinit var viewModel : As1NameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val binding = FragmentAs1NameSuggestionBinding.inflate(inflater, container, false)
        val args = As1NameSuggestionFragmentArgs.fromBundle(requireArguments())


        viewModel = ViewModelProvider(this,
            As1NameViewModel.Factory(requireActivity().application, args.as1nameInput))
            .get(As1NameViewModel::class.java)

        val adapter = As1NameSuggestionAdapter(viewModel)
        binding.as1NameRecycler.adapter = adapter

        viewModel.asNameList.observe(viewLifecycleOwner, Observer {
            Timber.i("submit list ${it.toString()}")
            adapter.data = it
        })

        binding.nextButtonAs1NameSuggestion.setOnClickListener {
            val item = adapter.getSelectedItem()
            if(item == null) {
                Snackbar.make(binding.root, "Please select one anatomical structure from the above list.", Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.saveAs1Id(item!!.id, requireActivity().getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE))
                findNavController().navigate(As1NameSuggestionFragmentDirections.actionAs1NameSuggestionFragmentToAs2NameInputFragment())
            }

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        activity?.run {
//            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        } ?: throw Throwable("invalid activity")
//        mainViewModel.updateToolbarTitle("Home")
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

}

class As1NameSuggestionAdapter(val viewModel: As1NameViewModel) : RecyclerView.Adapter<As1NameSuggestionAdapter.ViewHolder>() {
    var data = listOf<AsNameDomainAsNameSuggestion>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: AsnameItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(AsnameItemRecyclerBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        Timber.i("Here in bindviewholder1")
        holder.binding.asName.text = item.asName
        if (viewModel.selectedPosition == position) {
            holder.binding.divider.visibility = View.VISIBLE
        } else {
            holder.binding.divider.visibility = View.GONE
        }
        holder.binding.root.setOnClickListener {
            if (viewModel.selectedPosition == position) {
                viewModel.selectedPosition = -1
                notifyDataSetChanged()
            } else {
                viewModel.selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getSelectedItem(): AsNameDomainAsNameSuggestion? {
        if (viewModel.selectedPosition == -1) {
            return null
        }
        return data[viewModel.selectedPosition]
    }

}