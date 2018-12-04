package com.example.piyushchhabra.lifecycleawarearchitecturecomponents

import android.arch.lifecycle.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MyViewModel by lazy {
        ViewModelProviders.of(this).get(MyViewModel::class.java)
    }

    private val changeObserver =
        Observer<Int> { value -> value?.let { incrementCount(value) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.restoreState(savedInstanceState)
        viewModel.changeNotifier.observe(this, changeObserver)
        lifecycle.addObserver(viewModel)
        app_container.setOnClickListener { viewModel.increment() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    private fun incrementCount(value: Int) {
        app_text.text = (value).toString()
    }
}

class MyViewModel(private var count: Int = 0) : ViewModel(), LifecycleObserver {
    companion object { const val COUNT_KEY = "CountKey" }

    val changeNotifier = MutableLiveData<Int>()

    fun increment() { changeNotifier.value = ++count }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) fun onResume() { increment() }

    fun saveState(outState: Bundle) {
        outState.putInt(COUNT_KEY, count)
    }

    fun restoreState(inState: Bundle?) {
        inState?.let { count = inState.getInt(COUNT_KEY) }
    }
}

