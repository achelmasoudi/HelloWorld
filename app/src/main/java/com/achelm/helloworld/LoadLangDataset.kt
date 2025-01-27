package com.achelm.helloworld.fragments

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Language(val language: String, val code: String, val flag: String)

fun loadLanguages(context: Context): List<Language> {
    val json = context.assets.open("languages.json").bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<Language>>() {}.type
    return Gson().fromJson(json, listType)
}

