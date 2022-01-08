package com.android.example.messapp

data class MenuModel(val breakfast:List<String> = emptyList(),
                     val lunch:List<String> = emptyList(),
                     val dinner:List<String> = emptyList())