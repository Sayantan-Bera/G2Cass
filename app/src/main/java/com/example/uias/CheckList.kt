package com.example.uias

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.uias.databinding.ActivityCheckListBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class CheckList : AppCompatActivity(),itemClicked {
    private val db=FirebaseDatabase.getInstance().getReference(NODE_CHECKLIST)
    private var adapter = ChecklistAdaptor(this)
    private var binding: ActivityCheckListBinding? = null
    private val _chkList=MutableLiveData<CheckListItem>()
    val chkList:LiveData<CheckListItem> get()=_chkList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckListBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.topToolbar)
        binding?.recyclerCheck?.adapter = adapter
        binding?.addButton?.setOnClickListener {
            customDialogFunction()
        }
        chkList.observe(this) {
            adapter.addCheck(it)
        }
        getRealTimeUpdate()

    }

    private fun customDialogFunction() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom)

        customDialog.findViewById<TextView>(R.id.add_check_btn).setOnClickListener {
            val ch:EditText=customDialog.findViewById(R.id.Check)
            var edtstr=ch.text.toString().trim()
            if(!edtstr.isEmpty()){
                val checklistitem=CheckListItem()
                checklistitem.checkIt=edtstr
                addCheck(checklistitem)
            }else{
                Toast.makeText(this,"Please add check",Toast.LENGTH_SHORT).show()
            }
            customDialog.dismiss()
        }
            customDialog.show()
    }
    fun addCheck(checkItm:CheckListItem){
      checkItm.id=db.push().key
        db.child(checkItm.id!!).setValue(checkItm)
    }
    fun deleteCheck(checkItm: CheckListItem){
        db.child(checkItm.id!!).setValue(null)

    }
    private val childEventListener=object :ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val cl=snapshot.getValue(CheckListItem::class.java)
            cl?.id=snapshot.key
            _chkList.value=cl!!
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val cl=snapshot.getValue(CheckListItem::class.java)
            cl?.id=snapshot.key
            cl?.isDeleted=true
            _chkList.value=cl!!
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }
    fun getRealTimeUpdate(){
        db.addChildEventListener(childEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        db.removeEventListener(childEventListener)
    }

    override fun onItemLongClicked(position: Int) {
        customDeleteDialogFunction(position)
    }

    private fun customDeleteDialogFunction(position: Int) {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.custom_delete_dialog)

        customDialog.findViewById<TextView>(R.id.deleteBtn).setOnClickListener {
            val current=adapter.checklist[position]
            deleteCheck(current)
            adapter.notifyItemRemoved(position)
            customDialog.dismiss()
        }
        customDialog.show()
    }
}