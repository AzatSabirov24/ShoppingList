package com.azat_sabirov.shoppinglist.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.azat_sabirov.shoppinglist.activities.MainApp
import com.azat_sabirov.shoppinglist.databinding.FragmentShopListNamesBinding
import com.azat_sabirov.shoppinglist.db.MainViewModel
import com.azat_sabirov.shoppinglist.db.ShopListNameAdapter
import com.azat_sabirov.shoppinglist.dialogs.NewListDialog
import com.azat_sabirov.shoppinglist.entities.ShoppingListName
import com.azat_sabirov.shoppinglist.utils.TimeManager

class ShopListNamesFragment : BaseFragment() {
    private lateinit var binding: FragmentShopListNamesBinding
    private val adapter = ShopListNameAdapter()

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String) {
                val shopListName = ShoppingListName(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter
    }

    private fun observer(){
        mainViewModel.allShoppingListNames.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }


}