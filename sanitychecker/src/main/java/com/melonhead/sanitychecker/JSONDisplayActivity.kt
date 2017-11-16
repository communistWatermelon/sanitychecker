package com.melonhead.sanitychecker

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Format
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.classifier.CodeProcessor
import io.github.kbiakov.codeview.highlight.ColorTheme
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

abstract class JSONDisplayActivity : AppCompatActivity() {
    protected var currentTests: List<ApiResult>? = null

    protected val successSelected: Boolean
        get() = tabs.selectedTabPosition == 0

    protected val codeView: CodeView
        get() = code_view

    protected val contentView: View
        get() = content_view

    protected val tabLayout: TabLayout
        get() = tabs

    protected val view: View
        get() = view

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
                val tests = runTests()
                currentTests = tests
                updateUI(tests)
            }
        }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tests = currentTests ?: return
                updateUI(tests)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private suspend fun runTests(): List<ApiResult> {
        return SanityChecker.runTests(getTests())
    }

    abstract fun getTests(): List<APICall<*>>

    abstract fun updateUI(runTests: List<ApiResult>)
}
