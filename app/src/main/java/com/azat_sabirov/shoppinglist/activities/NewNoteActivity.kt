package com.azat_sabirov.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.azat_sabirov.shoppinglist.R
import com.azat_sabirov.shoppinglist.databinding.ActivityNewNoteBinding
import com.azat_sabirov.shoppinglist.entities.NoteItem
import com.azat_sabirov.shoppinglist.fragments.NoteFragment
import com.azat_sabirov.shoppinglist.utils.HtmlManager
import com.azat_sabirov.shoppinglist.utils.MyTouchListener
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
        init()
        onClickColorPicker()
        actionMenuCallback()
    }

    private fun onClickColorPicker() = with(binding) {
        ibRed.setOnClickListener { setColorForSelectedItem(R.color.picker_red) }
        ibBlue.setOnClickListener { setColorForSelectedItem(R.color.picker_blue) }
        ibGreen.setOnClickListener { setColorForSelectedItem(R.color.picker_green) }
        ibBlack.setOnClickListener { setColorForSelectedItem(R.color.picker_black) }
        ibYellow.setOnClickListener { setColorForSelectedItem(R.color.picker_yellow) }
        ibOrange.setOnClickListener { setColorForSelectedItem(R.color.picker_orange) }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.colorPicker.setOnTouchListener(MyTouchListener())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean = with(binding) {
        when (item.itemId) {
            R.id.save -> setMainResult()
            R.id.bold -> setBoldForSelectedItem()
            R.id.color -> if (colorPicker.isShown) closeColorPicker()
            else openColorPicker()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedItem() = with(binding) {
        val startPos = descriptionEt.selectionStart
        val endPos = descriptionEt.selectionEnd

        val styles = descriptionEt.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
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

    private fun setColorForSelectedItem(colorId: Int) = with(binding) {
        val startPos = descriptionEt.selectionStart
        val endPos = descriptionEt.selectionEnd

        val styles = descriptionEt.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        if (styles.isNotEmpty()) descriptionEt.text.removeSpan(styles[0])

        descriptionEt.text.apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity, colorId)),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
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

    private fun createNewNote(): NoteItem = with(binding) {
        return NoteItem(
            null,
            titleEt.text.toString(),
            HtmlManager.toHtml(descriptionEt.text),
            getCurrentTime(),
            ""
        )
    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(
            title = titleEt.text.toString(),
            content = HtmlManager.toHtml(descriptionEt.text)
        )
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("hh:mm:ss - dd/MM/yy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private fun openColorPicker() = with(binding) {
        colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this@NewNoteActivity, R.anim.open_color_picker)
        colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() = with(binding) {
        val openAnim = AnimationUtils.loadAnimation(this@NewNoteActivity, R.anim.close_color_picker)
        openAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        colorPicker.startAnimation(openAnim)
    }

    private fun actionMenuCallback() {
        val actionCallback = object : android.view.ActionMode.Callback {
            override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(
                mode: android.view.ActionMode?,
                item: MenuItem?
            ): Boolean {
                return true
            }

            override fun onDestroyActionMode(mode: android.view.ActionMode?) {
            }
        }
        binding.descriptionEt.customSelectionActionModeCallback = actionCallback
    }
}