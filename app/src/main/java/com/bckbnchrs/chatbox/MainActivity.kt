package com.bckbnchrs.chatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView:RecyclerView
    lateinit var editText: EditText
    lateinit var sendBtn:ImageView
    var ref=FirebaseDatabase.getInstance().getReference()
    lateinit var list:ArrayList<String>
    lateinit var myAdapter: MyAdapter
    var pos=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView=findViewById(R.id.recyclerView)
        editText=findViewById(R.id.editText)
        sendBtn=findViewById(R.id.sendBtn)


        sendBtn.setOnClickListener {
            if(editText.text.toString()==""){
                Toast.makeText(applicationContext,"Pls Write Something!",Toast.LENGTH_SHORT).show()
            }else{
                ref.child("chatroom").push().setValue(editText.text.toString())
                editText.setText("")
            }
        }

        list= ArrayList()
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        myAdapter=MyAdapter(list,applicationContext)
        recyclerView.adapter=myAdapter

        ref.child("chatroom").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    var temp=ArrayList<String>()
                    for(snap:DataSnapshot in snapshot.children){
                        temp.add(snap.value.toString())

                        if(pos>=list.size)
                            list.add(snap.value.toString())
                        pos++
                    }
                    pos=0
//                    Handler(Looper.getMainLooper()).postDelayed(object:Runnable{
//                        override fun run() {
//                        }
//                    },2000)
                    myAdapter.notifyDataSetChanged()
                    recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount)
//                    myAdapter.notifyItemRangeInserted(myAdapter.itemCount,list.size-1)
                    Log.i("listValue",list.toString())


                }
            }

        })

    }
}