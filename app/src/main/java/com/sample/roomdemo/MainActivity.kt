package com.sample.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.roomdemo.databinding.ActivityMainBinding
import com.sample.roomdemo.db.Subscriber
import com.sample.roomdemo.db.SubscriberDatabase
import com.sample.roomdemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var subscriberViewModel: SubscriberViewModel
    lateinit var adapter: MyRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO()
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)

        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = MyRecyclerViewAdapter({selectedItem:Subscriber -> listItemClicked(selectedItem)})
        binding.subscriberRecyclerview.adapter = adapter

        displaySubscribersList()
    }
    private fun displaySubscribersList(){
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("MyTag", "${it.toString()}")
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(subscriber: Subscriber){
        //Toast.makeText(this,"Selected Name : ${subscriber.name}",Toast.LENGTH_SHORT).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}