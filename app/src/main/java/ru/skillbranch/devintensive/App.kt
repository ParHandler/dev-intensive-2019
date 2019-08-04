package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context

class App:Application() {
    companion object{
        private var insance:App? = null

        fun applicationContext() : Context{
            return insance!!.applicationContext
        }
    }

    init {
        insance = this
    }

    override fun onCreate() {
        super.onCreate()
        //TODO call once
    }
}