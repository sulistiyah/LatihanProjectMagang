package com.magang.projectmaganglatihan.storage

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(private val mCtx: Context) {


    //Username di HomeActivity
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



    //Employee ID untuk Param ProfilActivity
    val employeeId: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("employeeId", null).toString()
        }

    fun saveEmployeeId(id: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("employeeId", id)
        editor.apply()
    }


    val departementId: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("departementId", null).toString()
        }

    fun saveDepartementId(id: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("departementId", id)
        editor.apply()
    }

    val departementTitle: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("departementTitle", null).toString()
        }

    fun saveDepartementTitle(departementTitle: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("departementTitle", departementTitle)
        editor.apply()
    }



    val companyId: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("companyId", null).toString()
        }

    fun saveCompanyId(id: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("companyId", id)
        editor.apply()
    }

    val infoId: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("infoId", null).toString()
        }

    fun saveInfoId(id: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("infoId", id)
        editor.apply()
    }

    val noTelp : String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("noTelepon", null).toString()
        }

    fun saveNoTelepon (noTelepon : String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("noTelepon", noTelepon)
        editor.apply()
    }

    val employeeEmail : String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("employeeEmail", null).toString()
        }

    fun saveEmployeeEmail (employeeEmail : String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("employeeEmail", employeeEmail)
        editor.apply()
    }

    val employeeNik : String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("employeeNik", null).toString()
        }

    fun saveEmployeeNik (employeeNik : String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("employeeNik", employeeNik)
        editor.apply()
    }



    val tokenSplash: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", null).toString()
        }

    fun saveTokenSplash(token: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("token", token)
        editor.apply()
    }



    val tokenLogin: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", null).toString()
        }

    fun saveTokenLogin(token: String) {
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
        editor.apply()
    }


    val changePass: Boolean
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(CHANGE_PASSWORD, false)

        }

    fun saveChangePass(changePass: Boolean) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean(CHANGE_PASSWORD, changePass)
        editor.apply()
    }

    val avatar: Boolean
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(SAVE_AVATAR, false)

        }

    fun saveAvatar(avatar: Boolean) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean(SAVE_AVATAR, avatar)
        editor.apply()
    }

    val checkData: Boolean
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(CHECK_DATA, false)

        }

    fun saveCheckData(checkData: Boolean) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean(CHECK_DATA, checkData)
        editor.apply()
    }


    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_preff"
        private var IS_LOGIN = "isLogin"
        private var CHECK_DATA = "checkData"
        private var SAVE_AVATAR = "saveAvatar"
        private var CHANGE_PASSWORD = "changePassword"
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