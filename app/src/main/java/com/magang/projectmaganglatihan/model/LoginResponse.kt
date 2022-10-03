package com.magang.projectmaganglatihan.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val Data: Data,
    val Message: String,
    val StatusCode: Int
)

data class Data(
    val token: String,
    val user: User
)

data class User(
    val company_id: Int,
    val created_at: String,
    val deleted_at: Any,
    val employee_base_salary: String,
    val employee_branch: Any,
    val employee_branch_id: Int,
    val employee_code: String,
    val employee_contract_end_date: Any,
    val employee_contract_start_date: Any,
    val employee_contract_type: Int,
    val employee_created_by: Int,
    val employee_department_id: Int,
    val employee_email: String,
    val employee_fullname: String,
    val employee_id: Int,
    val employee_image: String,
    val employee_info: Any,
    val employee_is_active: Int,
    val employee_is_suspended: Int,
    val employee_join_date: Any,
    val employee_nik: String,
    val employee_position_id: Int,
    val employee_schedule_id: Int,
    val employee_sex: String,
    val employee_updated_by: Int,
    val fcm_id: Any,
    val has_priviledge: Int,
    val last_logged_in: String,
    val profile: Profile,
    val updated_at: String
)

data class Profile(
    val address: Any,
    val address_alt: Any,
    val color: String,
    val date_of_birth: Any,
    val employee_avatar: String,
    val firstname: String,
    val fullname: String,
    val identity_filename: Any,
    val identity_no: Any,
    val identity_type: Int,
    val lastname: String,
    val middlename: String,
    val mobile_no: Any,
    val mobile_no_alt: Any,
    val npwp_no: Any,
    val phone_no: Any,
    val phone_no_alt: Any,
    val place_of_birth: Any,
    val profile_employee_id: Int,
    val profile_id: Int,
    val salutation: String,
    val shortname: String
)