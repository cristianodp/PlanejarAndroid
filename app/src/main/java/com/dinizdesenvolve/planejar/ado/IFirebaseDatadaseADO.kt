package com.dinizdesenvolve.planejar.ado

import android.os.Build
import android.support.annotation.RequiresApi
import com.dinizdesenvolve.planejar.domain.Summary
import com.google.firebase.database.*
import java.util.ArrayList

/**
 * Created by crist on 25/01/2018.
 */

interface IFirebaseDatadaseADO<T>{
    var path:String

    var dataChange:IDataChange
    fun initDatabaseListener() {

        var mFirebaseDatabase = FirebaseDatabase.getInstance()

        mFirebaseDatabase.getReference(path).addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                dataChange.notifyDataChanged()
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                dataChange.notifyDataChanged()
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onChildChanged(snapshot: DataSnapshot?, p1: String?) {

                val item = processSnapshot(snapshot)
                updateList(item)
                dataChange.notifyDataChanged()


            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onChildAdded(snapshot: DataSnapshot?, p1: String?) {

                val item = processSnapshot(snapshot)
                updateList(item)
                dataChange.notifyDataChanged()

            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onChildRemoved(snapshot: DataSnapshot?) {

                val item = processSnapshot(snapshot)
                removeList(item)
                dataChange.notifyDataChanged()

            }
        })
    }

    fun removeList(item: T?)

    fun updateList(item: T?)

    fun processSnapshot(snapshot:DataSnapshot?):T?

    interface IDataChange {
        fun notifyDataChanged()
    }



}
