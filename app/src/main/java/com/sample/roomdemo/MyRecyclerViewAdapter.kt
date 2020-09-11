package com.sample.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.roomdemo.databinding.ListItemBinding
import com.sample.roomdemo.db.Subscriber
import com.sample.roomdemo.generated.callback.OnClickListener

class MyRecyclerViewAdapter(private val clickListener: (Subscriber) -> Unit) : RecyclerView.Adapter<MyViewHolder>() {

    private val subscriberList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(layoutInflater,R.layout.list_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscriberList[position],clickListener)
    }
    fun setList(subscribers: List<Subscriber>){
        subscriberList.clear()
        subscriberList.addAll(subscribers)
    }
}

class MyViewHolder(var binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit){
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email

        binding.listItemLayout.setOnClickListener {
            clickListener(subscriber)
        }
    }

}