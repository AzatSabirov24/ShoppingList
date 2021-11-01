package com.azat_sabirov.shoppinglist.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.azat_sabirov.shoppinglist.HtmlManager
import com.azat_sabirov.shoppinglist.R
import com.azat_sabirov.shoppinglist.databinding.ActivityNewNoteBinding
import com.azat_sabirov.shoppinglist.entities.NoteItem
import com.azat_sabirov.shoppinglist.fragments.NoteFragment
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
        getNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        sNote?.let {
            note = sNote as NoteItem
            fillNote()
        }
    }

    private fun fillNote() = with(binding) {
            titleEt.setText(note?.title.toString())
            descriptionEt.setText(HtmlManager.getFromHtml(note?.content!!).trim())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                setMainResult()
            }
            R.id.bold -> {
                setBoldForSelectedItem()
            }
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedItem() = with(binding){
        val startPos = descriptionEt.selectionStart
        val endPos = descriptionEt.selectionEnd

        val styles = descriptionEt.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()){
            descriptionEt.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }
        descriptionEt.text.apply {
            setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            trim()
        }
        descriptionEt.setSelection(startPos)
    }

    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setMainResult() {
        var editState = "new"
        val tempNote: NoteItem? = if (note == null) {
            createNewNote()
        } else {
            editState = "update"
            updateNote()
        }
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun createNewNote(): NoteItem  = with(binding){
        return NoteItem(
            null,
            titleEt.text.toString(),
            HtmlManager.toHtml(descriptionEt.text),
            getCurrentTime(),
            ""
        )
    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(title = titleEt.text.toString(),
            content = HtmlManager.toHtml(descriptionEt.text))
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("hh:mm:ss - dd/MM/yy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}