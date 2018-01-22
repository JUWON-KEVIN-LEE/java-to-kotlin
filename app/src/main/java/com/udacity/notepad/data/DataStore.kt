package com.udacity.notepad.data

import android.content.Context
import org.jetbrains.anko.doAsync

object DataStore {

    // TODO in kotlin compiler doesn't like static... ( ? )
    @JvmStatic
    lateinit var notes: NoteDatabase // late init >>> safer and cleaner
        private set

    fun init(context: Context) {
        notes = NoteDatabase(context)
    }

    fun execute(runnable: Runnable) {
        execute { runnable.run() }
    }

    fun execute(fn : () -> Unit) {
        doAsync { fn() }
    }
}
