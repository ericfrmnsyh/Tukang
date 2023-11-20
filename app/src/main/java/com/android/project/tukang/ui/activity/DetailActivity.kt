package com.android.project.tukang.ui.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.project.tukang.R
import com.android.project.tukang.data.response.DataItem
import com.android.project.tukang.databinding.ActivityDetailBinding
import com.android.project.tukang.helper.Constant
import com.android.project.tukang.ui.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mySharedPreferences: SharedPreferences
    private lateinit var myEditor: SharedPreferences.Editor
    private var tag: String? = null
    private lateinit var nik: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        initSharedPreferences()

        tag = intent?.getStringExtra(Constant.KEY_TAG)
        connectViewModel()
        mainViewModel.getDetail().observe(this) {
            if (it != null) {
                updateUserData(it)
                showLoading(false)

                nik = mySharedPreferences.getString(Constant.KEY_NIK, null).toString()
                binding.card2.setOnClickListener{
                    tag?.let { it1 -> SelectActivity.start(this, nik, it1) }
                }
                binding.card3.setOnClickListener{
                    ListActivity.start(this, nik)
                }
                binding.card4.setOnClickListener {
                    HistoryActivity.start(this)
                }
            }
        }

        tag?.let {
            showLoading(true)
            getDetail(it)
        }
    }

    private fun initSharedPreferences(){
        mySharedPreferences = getSharedPreferences("my_shared_preferences", MODE_PRIVATE)
        myEditor = mySharedPreferences.edit()
    }

    private fun insertSharedPreferences(inputNIK: String){
        myEditor.apply(){
            putString(Constant.KEY_NIK,inputNIK)
            apply()
        }
        Toast.makeText(this,"Berhasil Mendapatkan Data", Toast.LENGTH_LONG).show()
    }

    private fun getDetail(tag: String){
        val call = mainViewModel.request.getDataItem(tag)

        call.enqueue(object : Callback<DataItem> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<DataItem>,
                response: Response<DataItem>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        mainViewModel.dataDetail.postValue(it)
                        binding.card5.visibility = View.GONE
                        Log.i(ContentValues.TAG, "CEK: $mainViewModel.dataDetail")
                    }
                }
                else{
                    Log.d("Error",  "Tidak Ada Data")
                    binding.card1.visibility = View.GONE
                    binding.tvNoData.text = "Tidak ada data untuk id : $tag"
                    binding.tvNoData.visibility = View.VISIBLE
                    addData()
                    showLoading(false)
                    Toast.makeText(this@DetailActivity, "Tidak Ada Data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataItem>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private fun addData(){
        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy/MM/dd HH:mm:ss")

        binding.card5.visibility = View.VISIBLE
        binding.card5.setOnClickListener{
            tag?.let { it1 -> mainViewModel.uploadReq(dateInString, it1) }
            Toast.makeText(this@DetailActivity, "$tag Pengajuan Telah Dibuat", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java) )
        }
    }
    private fun showLoading(state: Boolean) {
        binding.spinDetail.visibility = if (state) View.VISIBLE else View.GONE
        binding.card.visibility = if (state) View.GONE else View.VISIBLE
    }

    private fun connectViewModel(){
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]
    }

    private fun updateUserData(data: DataItem) {
        binding.apply {
            dataNama.text = data.nama
            dataNik.text = data.nik
            dataTempat.text = data.tempatLahir
            dataTanggal.text = data.tanggalLahir.toString()
            dataJk.text = data.jenisKelamin
            dataAlamat.text = data.alamat
            dataRt.text = resources.getString(R.string.or, data.rt, data.rw)
            dataKel.text = data.desa
            dataKec.text = data.kecamatan
            dataAgama.text = data.agama
            dataStatus.text = data.statusPerkawinan
            dataPekerjaan.text = data.pekerjaan
            dataKw.text = data.kewarganegaraan
            insertSharedPreferences(dataNik.text.toString())
            //data.nik?.let { insertSharedPreferences(it) }
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ktp, menu)
        return true
    }

    // Set action for options menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_repeat -> {
                myEditor.clear().apply()
                startActivity(Intent(this, MainActivity::class.java) )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun start(context: Context, tag: String) {
            Intent(context, DetailActivity::class.java).apply {
                this.putExtra("KEY_TAG", tag)
                context.startActivity(this)
            }
        }
    }
}