package com.magang.projectmaganglatihan.storage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(private val mCtx: Context) {

    val employeeFullname : String
    get() {
        val sharedPreferences =
            mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        return sharedPreferences.getString("employeeFullname",null).toString()
    }
    fun saveEmployeeFullname (employeeFullname: String){
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("employeeFullname",employeeFullname)
        editor.apply()
    }

    val token: String
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", null).toString()
        }

    fun saveToken(token: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("token", token)
        editor.apply()
    }

    val islogin: Boolean
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(IS_LOGIN, false)

        }

    fun savelogin(islogin: Boolean) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean(IS_LOGIN, islogin)
        editor.commit()
    }

    val register: String
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("register", null).toString()
        }

    fun saveDataRegister(dataRegister: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("register", dataRegister)
        editor.apply()
    }


    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_preff"
        private var IS_LOGIN = "isLogin"
        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)

            }
            return mInstance as SharedPrefManager
        }
    }
}