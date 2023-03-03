package com.onandoff.onandoff_android.data.ext

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.onandoff.onandoff_android.R

@BindingAdapter("entries")
fun Spinner.setEntries(entries: List<String>?) {
    entries?.run {
        val arrayAdapter = ArrayAdapter(context, R.layout.item_spinner, entries)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter = arrayAdapter
    }
}