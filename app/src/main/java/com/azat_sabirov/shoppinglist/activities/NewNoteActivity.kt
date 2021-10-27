package com.azat_sabirov.shoppinglist.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.azat_sabirov.shoppinglist.R
import com.azat_sabirov.shoppinglist.databinding.ActivityNewNoteBinding
import com.azat_sabirov.shoppinglist.fragments.NoteFragment

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                setMainResult()
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setMainResult() {
        val i = Intent().apply {
            putExtra(NoteFragment.TITLE_KEY, binding.titleEt.text.toString())
            putExtra(NoteFragment.DESC_KEY, binding.descriptionEt.text.toString())
        }
        setResult(RESULT_OK, i)
        finish()
    }
}