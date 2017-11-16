package com.melonhead.sanitychecker

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import io.github.kbiakov.codeview.adapters.Format
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.classifier.CodeProcessor
import io.github.kbiakov.codeview.highlight.ColorTheme
import kotlinx.android.synthetic.main.json_display_activity_main.*
import kotlinx.android.synthetic.main.json_display_content_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

abstract class JSONDisplayActivity : AppCompatActivity() {
    protected var currentTests: List<ApiResult>? = null

    private val successSelected: Boolean
        get() = tabs.selectedTabPosition == 0

    override fun onCreate(savedInstanceState: Bundle?) {
        CodeProcessor.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.json_display_activity_main)
        setSupportActionBar(toolbar)

        code_view.setOptions(Options.Default.get(this)
                .withFormat(Format.ExtraCompact)
                .withTheme(ColorTheme.MONOKAI)
        )

        fab.setOnClickListener { _ ->
            async(UI) {
                val tests = runTests()
                currentTests = tests
                showResults(tests)
            }
        }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tests = currentTests ?: return
                showResults(tests)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    open fun showResults(withTests: List<ApiResult>) {
        val successes = withTests.filter { it.success }
        val failures = withTests.filter { !it.success }
        val listToUse = if (successSelected) successes else failures
        code_view.setCode(listToUse.joinToString { "${it.cleanOutput()}\n" }, "json")
        Snackbar.make(content_view, "Succeeded: ${successes.size}, Failures: ${failures.size}", Snackbar.LENGTH_LONG).show()
    }

    private suspend fun runTests(): List<ApiResult> {
        return SanityChecker.runTests(getTests())
    }

    abstract fun getTests(): List<APICall<*>>
}
