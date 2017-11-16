package com.melonhead.sanitychecker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.kbiakov.codeview.adapters.Format
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.classifier.CodeProcessor
import io.github.kbiakov.codeview.highlight.ColorTheme
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

abstract class JSONDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        CodeProcessor.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        code_view.setOptions(Options.Default.get(this)
                .withFormat(Format.ExtraCompact)
                .withTheme(ColorTheme.MONOKAI)
        )

        fab.setOnClickListener { _ ->
            async(UI) {
                runTests()
            }
        }
    }

    abstract suspend fun runTests()

    abstract fun getTests(): List<APICall<*>>

}
