package com.achelm.helloworld.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.achelm.helloworld.R

class LanguagePickerBottomSheetFragment(private val btmSheetTitle: String): BottomSheetDialogFragment() {

    var onLanguageSelected: ((language: Language) -> Unit)? = null
    private lateinit var languages: List<Language>
    private lateinit var titleOfBottomSheet: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_language_picker_bottom_sheet, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewLanguages)
        titleOfBottomSheet = view.findViewById(R.id.titleOfBottomSheetId)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Load languages
        languages = loadLanguages(requireContext())

        recyclerView.adapter = LanguageAdapter(languages) { language ->
            onLanguageSelected?.invoke(language)
            dismiss()
        }

        // Set the title of the bottom sheet
        titleOfBottomSheet.text = btmSheetTitle

        return view
    }

    class LanguageAdapter(private val languages: List<Language>, private val onLanguageClicked: (Language) -> Unit) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
            return LanguageViewHolder(view)
        }

        override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
            val language = languages[position]
            holder.bind(language, onLanguageClicked)
        }

        override fun getItemCount(): Int = languages.size

        class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(language: Language, onLanguageClicked: (Language) -> Unit) {
                val flagTxtV: TextView = itemView.findViewById(R.id.flagTextView)
                val languageTxtV: TextView = itemView.findViewById(R.id.languageTextView)
                val selectionBtn: LinearLayout = itemView.findViewById(R.id.language_selectionBtnId)
                flagTxtV.text = language.flag
                languageTxtV.text = language.language

                selectionBtn.setOnClickListener {
                    onLanguageClicked(language)
                }
            }
        }
    }

}