package com.magang.projectmaganglatihan.model
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class AttendanceListResponse(
    @SerializedName("Data")
    var `data`: Data,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    @Keep
    data class Data(
        @SerializedName("detail")
        var detail: List<Detail>,
        @SerializedName("total_attendance_absence")
        var totalAttendanceAbsence: Int,
        @SerializedName("total_attendance_permit")
        var totalAttendancePermit: Int,
        @SerializedName("total_attendance_sick")
        var totalAttendanceSick: Int,
        @SerializedName("total_attendance_success")
        var totalAttendanceSuccess: Int,
        @SerializedName("total_employee")
        var totalEmployee: Int
    ) {
        @Keep
        data class Detail(
            @SerializedName("absence_request_id")
            var absenceRequestId: Int,
            @SerializedName("address")
            var address: String,
            @SerializedName("attendance_absence_type")
            var attendanceAbsenceType: Int,
            @SerializedName("attendance_created_by")
            var attendanceCreatedBy: Int,
            @SerializedName("attendance_employee_id")
            var attendanceEmployeeId: Int,
            @SerializedName("attendance_id")
            var attendanceId: Int,
            @SerializedName("attendance_in_date")
            var attendanceInDate: String,
            @SerializedName("attendance_in_time")
            var attendanceInTime: String,
            @SerializedName("attendance_info")
            var attendanceInfo: Any,
            @SerializedName("attendance_is_late")
            var attendanceIsLate: Int,
            @SerializedName("attendance_out_date")
            var attendanceOutDate: Any,
            @SerializedName("attendance_out_time")
            var attendanceOutTime: Any,
            @SerializedName("attendance_type")
            var attendanceType: Int,
            @SerializedName("attendance_updated_by")
            var attendanceUpdatedBy: Int,
            @SerializedName("company_id")
            var companyId: Int,
            @SerializedName("created_at")
            var createdAt: String,
            @SerializedName("deleted_at")
            var deletedAt: Any,
            @SerializedName("employee")
            var employee: Employee,
            @SerializedName("image")
            var image: String,
            @SerializedName("lat")
            var lat: String,
            @SerializedName("lng")
            var lng: String,
            @SerializedName("type")
            var type: Type,
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
                @SerializedName("updated_at")
                var updatedAt: String
            )

            @Keep
            data class Type(
                @SerializedName("at_icon")
                var atIcon: String,
                @SerializedName("at_id")
                var atId: Int,
                @SerializedName("at_text")
                var atText: String,
                @SerializedName("at_title")
                var atTitle: String,
                @SerializedName("created_at")
                var createdAt: String,
                @SerializedName("deleted_at")
                var deletedAt: Any,
                @SerializedName("updated_at")
                var updatedAt: String
            )
        }
    }
}