package com.magang.projectmaganglatihan.model

import com.google.gson.annotations.SerializedName

class RegisterParam(
    @SerializedName("company_id")
    var companyId: Int,
    @SerializedName("employee_department_id")
    var employeeDepartmentId: String,
    @SerializedName("employee_email")
    var employeeEmail: String,
    @SerializedName("employee_fullname")
    var employeeFullname: String,
    @SerializedName("employee_nik")
    var employeeNik: String,
    @SerializedName("employee_password")
    var employeePassword: String,
    @SerializedName("employee_phone_no")
    var employeePhoneNo: String
)