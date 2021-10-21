package com.azat_sabirov.shoppinglist.fragments

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun onClickNew()
}