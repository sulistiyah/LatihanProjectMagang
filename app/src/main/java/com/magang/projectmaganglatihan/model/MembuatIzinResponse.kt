package com.magang.projectmaganglatihan.model
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class MembuatIzinResponse(
    @SerializedName("Data")
    var `data`: Data,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    @Keep
    data class Data(
        @SerializedName("ar_approved_by")
        var arApprovedBy: Int,
        @SerializedName("ar_approved_date")
        var arApprovedDate: Any,
        @SerializedName("ar_code")
        var arCode: String,
        @SerializedName("ar_created_by")
        var arCreatedBy: Int,
        @SerializedName("ar_employee_id")
        var arEmployeeId: Int,
        @SerializedName("ar_end_date")
        var arEndDate: String,
        @SerializedName("ar_end_time")
        var arEndTime: Any,
        @SerializedName("ar_id")
        var arId: Int,
        @SerializedName("ar_reason")
        var arReason: String,
        @SerializedName("ar_rejected_by")
        var arRejectedBy: Int,
        @SerializedName("ar_rejected_date")
        var arRejectedDate: Any,
        @SerializedName("ar_start_date")
        var arStartDate: String,
        @SerializedName("ar_start_time")
        var arStartTime: Any,
        @SerializedName("ar_status")
        var arStatus: Int,
        @SerializedName("ar_type")
        var arType: Int,
        @SerializedName("ar_updated_by")
        var arUpdatedBy: Int,
        @SerializedName("company_id")
        var companyId: Int,
        @SerializedName("created_at")
        var createdAt: String,
        @SerializedName("days_count")
        var daysCount: Int,
        @SerializedName("deleted_at")
        var deletedAt: Any,
        @SerializedName("employee")
        var employee: Employee,
        @SerializedName("updated_at")
        var updatedAt: String
    ) {
        @Keep
        data class Employee(
            @SerializedName("company_id")
            var companyId: Int,
            @SerializedName("created_at")
            var createdAt: String,
            @SerializedName("deleted_at")
            var deletedAt: Any,
            @SerializedName("employee_base_salary")
            var employeeBaseSalary: String,
            @SerializedName("employee_branch")
            var employeeBranch: Any,
            @SerializedName("employee_branch_id")
            var employeeBranchId: Int,
            @SerializedName("employee_code")
            var employeeCode: String,
            @SerializedName("employee_contract_end_date")
            var employeeContractEndDate: Any,
            @SerializedName("employee_contract_start_date")
            var employeeContractStartDate: Any,
            @SerializedName("employee_contract_type")
            var employeeContractType: Int,
            @SerializedName("employee_created_by")
            var employeeCreatedBy: Int,
            @SerializedName("employee_department_id")
            var employeeDepartmentId: Int,
            @SerializedName("employee_email")
            var employeeEmail: String,
            @SerializedName("employee_fullname")
            var employeeFullname: String,
            @SerializedName("employee_id")
            var employeeId: Int,
            @SerializedName("employee_image")
            var employeeImage: String,
            @SerializedName("employee_info")
            var employeeInfo: Any,
            @SerializedName("employee_is_active")
            var employeeIsActive: Int,
            @SerializedName("employee_is_suspended")
            var employeeIsSuspended: Int,
            @SerializedName("employee_join_date")
            var employeeJoinDate: Any,
            @SerializedName("employee_nik")
            var employeeNik: String,
            @SerializedName("employee_position_id")
            var employeePositionId: Int,
            @SerializedName("employee_schedule_id")
            var employeeScheduleId: Int,
            @SerializedName("employee_sex")
            var employeeSex: String,
            @SerializedName("employee_updated_by")
            var employeeUpdatedBy: Int,
            @SerializedName("fcm_id")
            var fcmId: Any,
            @SerializedName("has_priviledge")
            var hasPriviledge: Int,
            @SerializedName("last_logged_in")
            var lastLoggedIn: String,
            @SerializedName("profile")
            var profile: Profile,
            @SerializedName("updated_at")
            var updatedAt: String
        ) {
            @Keep
            data class Profile(
                @SerializedName("address")
                var address: Any,
                @SerializedName("address_alt")
                var addressAlt: Any,
                @SerializedName("color")
                var color: String,
                @SerializedName("date_of_birth")
                var dateOfBirth: Any,
                @SerializedName("employee_avatar")
                var employeeAvatar: String,
                @SerializedName("firstname")
                var firstname: String,
                @SerializedName("fullname")
                var fullname: String,
                @SerializedName("identity_filename")
                var identityFilename: Any,
                @SerializedName("identity_no")
                var identityNo: Any,
                @SerializedName("identity_type")
                var identityType: Int,
                @SerializedName("lastname")
                var lastname: String,
                @SerializedName("middlename")
                var middlename: String,
                @SerializedName("mobile_no")
                var mobileNo: Any,
                @SerializedName("mobile_no_alt")
                var mobileNoAlt: Any,
                @SerializedName("npwp_no")
                var npwpNo: Any,
                @SerializedName("phone_no")
                var phoneNo: String,
                @SerializedName("phone_no_alt")
                var phoneNoAlt: Any,
                @SerializedName("place_of_birth")
                var placeOfBirth: Any,
                @SerializedName("profile_employee_id")
                var profileEmployeeId: Int,
                @SerializedName("profile_id")
                var profileId: Int,
                @SerializedName("salutation")
                var salutation: String,
                @SerializedName("shortname")
                var shortname: String
            )
        }
    }
}