package com.example.sleepapnea.data

import com.example.sleepapnea.model.User
import com.example.sleepapnea.model.UserApneaParams
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class FirebaseClient{

    private val database= Firebase.database.reference

    suspend fun writeUser(user: User){
        database.child("Users").child(user.uid).setValue(user)
    }

    suspend fun writeUserApneaData(apneaParams: UserApneaParams, uid:String){
        database.child("ApneaData").child(uid).setValue(apneaParams)
    }

    suspend fun readUserDetails(uid: String): User?{
        val user: User? =null
        val userEventListener= object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                TODO("Make the user object")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        database.child("User").child(uid).addValueEventListener(userEventListener)

        return user
    }

    suspend fun readApneaParams(uid: String): UserApneaParams?{
        val apneaParams: UserApneaParams?=null
        val paramsEventListener= object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        database.child("ApneaData").child(uid)
            .addValueEventListener(paramsEventListener)

        return apneaParams
    }
}