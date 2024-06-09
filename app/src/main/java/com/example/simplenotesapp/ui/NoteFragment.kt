package com.example.simplenotesapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simplenotesapp.R
import com.example.simplenotesapp.ThemeViewModel
import com.example.simplenotesapp.adapter.NoteAdapter
import com.example.simplenotesapp.data.entity.Note
import com.example.simplenotesapp.databinding.FragmentNotesBinding
import com.example.simplenotesapp.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

const val GRID_NOTES_SPAN: Int = 2
const val STARTING_X: Int = 0
const val STARTING_Y: Int = 2
@AndroidEntryPoint
class NoteFragment: Fragment(R.layout.fragment_notes), NoteAdapter.OnNoteClickListener {
    private val viewModel by viewModels<NoteViewModel>()
    private lateinit var themeViewModel: ThemeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNotesBinding.bind(requireView())
        themeViewModel = ViewModelProvider(requireActivity()).get(ThemeViewModel::class.java)


        binding.apply {
            recyclerViewNotes.layoutManager = GridLayoutManager(context, GRID_NOTES_SPAN)
            recyclerViewNotes.setHasFixedSize(true)

            addButton.setOnClickListener{
                val action = NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(null)
                findNavController().navigate(action)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notes.collect{notes ->
                    val adapter = NoteAdapter(notes, this@NoteFragment)
                    recyclerViewNotes.adapter = adapter
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notesEvent.collect{event ->
                    if (event is NoteViewModel.NotesEvent.ShowUndoSnackBar){
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).setAction("Undo"){
                            viewModel.insertNote(event.note)
                        }.show()
                    }
                }
            }

            helpPopup.setOnClickListener{
                showPopup(R.layout.popup_help)
            }

            changeTheme.setOnClickListener{
                themeViewModel.toggleTheme()
            }
        }
    }

    override fun onNoteClick(note: Note) {
        val action = NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(note)
        findNavController().navigate(action)
    }

    override fun onNoteLongClick(note: Note) {
        viewModel.deleteNote(note)
    }

    private fun showPopup(layoutId: Int){
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val popupView: View = inflater.inflate(layoutId, null)

        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, true)

        val closePopup = popupView.findViewById<FloatingActionButton>(R.id.close_popup)
        closePopup.setOnClickListener{
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(requireView(), android.view.Gravity.CENTER, STARTING_X, STARTING_Y)
    }
}