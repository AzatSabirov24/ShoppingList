package com.azat_sabirov.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azat_sabirov.shoppinglist.activities.MainApp
import com.azat_sabirov.shoppinglist.activities.NewNoteActivity
import com.azat_sabirov.shoppinglist.databinding.FragmentNoteBinding
import com.azat_sabirov.shoppinglist.db.MainViewModel
import com.azat_sabirov.shoppinglist.db.NoteAdapter
import com.azat_sabirov.shoppinglist.entities.NoteItem

class NoteFragment : BaseFragment() {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: NoteAdapter
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    private fun initRcView() = with(binding) {
        noteRcView.layoutManager = LinearLayoutManager(activity)
        adapter = NoteAdapter()
        noteRcView.adapter = adapter
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                Log.d("MyLog", "${it.data?.getStringExtra(DESC_KEY)}")
            }
        }
    }

    private fun observer() {
        mainViewModel.allNotes.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    companion object {
        const val NEW_NOTE_KEY = "new_note_key"
        const val DESC_KEY = "desk_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}