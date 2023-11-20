package com.android.project.tukang.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.project.tukang.R
import com.android.project.tukang.data.LogModel
import com.android.project.tukang.data.response.NewLogResponse
import com.android.project.tukang.databinding.ActivityHistoryBinding
import com.android.project.tukang.ui.adapter.MainAdapter
import com.android.project.tukang.ui.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var mainAdapter: MainAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectViewModel()
        getAllData()
        getHistoryData()
        setAdapter()
    }

    private fun updateListData(list: List<LogModel>) {
        mainAdapter.listLog.clear()
        mainAdapter.listLog.addAll(list)
        mainAdapter.setData(list as MutableList<LogModel>)
    }

    private fun getHistoryData(){
        showLoading(true)
        listLog()
    }

    private fun getAllData(){
        mainViewModel.getListLog().observe(this) {
            if (it != null) {
                updateListData(it)
                showLoading(false)
            }
        }
    }

    private fun listLog() {
        val call = mainViewModel.request.getAllLog()

        call.enqueue(object : Callback<NewLogResponse> {
            override fun onResponse(
                call: Call<NewLogResponse>,
                response: Response<NewLogResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        mainViewModel.listLog.postValue(it.list_log)
                        binding.toolbar.visibility = View.GONE
                    }
                }
                else {
                    Log.d("Error", "Tidak Ada Data")
                    binding.rvLog.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    showLoading(false)
                    Toast.makeText(this@HistoryActivity, "Tidak Ada Data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NewLogResponse>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ktp, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_repeat -> {
                startActivity(Intent(this, MainActivity::class.java) )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            spinDetail.visibility = if (state) View.VISIBLE else View.GONE
            rvLog.visibility = if (state) View.GONE else View.VISIBLE
        }
    }

    private fun connectViewModel(){
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]
    }

    private fun setAdapter(){
        mainAdapter = MainAdapter(this, mutableListOf())
        binding.rvLog.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL)
            )
            adapter = mainAdapter
        }
    }

    companion object {
        fun start(context: Context) {
            Intent(context, HistoryActivity::class.java).apply {
                context.startActivity(this)
            }
        }
    }
}