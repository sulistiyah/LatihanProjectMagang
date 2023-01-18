package com.magang.projectmaganglatihan.model


import com.google.gson.annotations.SerializedName

class TugasKerjaResponse(
    @SerializedName("Data")
    var `data`: List<Data>,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    class Data(
        @SerializedName("comment")
        var comment: String,
        @SerializedName("company_id")
        var companyId: Int,
        @SerializedName("company_project_id")
        var companyProjectId: Int,
        @SerializedName("employee_id")
        var employeeId: Int,
        @SerializedName("report_status")
        var reportStatus: ReportStatus,
        @SerializedName("report_status_id")
        var reportStatusId: Int,
        @SerializedName("sprint_log")
        var sprintLog: SprintLog,
        @SerializedName("sprint_log_project_id")
        var sprintLogProjectId: Int,
        @SerializedName("work_report_id")
        var workReportId: Int,
        @SerializedName("work_report_time")
        var workReportTime: String
    ) {
        class ReportStatus(
            @SerializedName("color")
            var color: String,
            @SerializedName("company_id")
            var companyId: Int,
            @SerializedName("report_status_description")
            var reportStatusDescription: String,
            @SerializedName("report_status_id")
            var reportStatusId: Int,
            @SerializedName("report_status_name")
            var reportStatusName: String
        )

        class SprintLog(
            @SerializedName("company_id")
            var companyId: Int,
            @SerializedName("company_project")
            var companyProject: CompanyProject,
            @SerializedName("company_project_id")
            var companyProjectId: Int,
            @SerializedName("issue_detail")
            var issueDetail: String,
            @SerializedName("priority")
            var priority: Priority,
            @SerializedName("priority_id")
            var priorityId: String,
            @SerializedName("related_page")
            var relatedPage: String,
            @SerializedName("sprint_log_project_id")
            var sprintLogProjectId: Int,
            @SerializedName("tc_code")
            var tcCode: String
        ) {
            class CompanyProject(
                @SerializedName("company_id")
                var companyId: Int,
                @SerializedName("company_project_description")
                var companyProjectDescription: String,
                @SerializedName("company_project_id")
                var companyProjectId: Int,
                @SerializedName("company_project_logo")
                var companyProjectLogo: Any,
                @SerializedName("company_project_name")
                var companyProjectName: String,
                @SerializedName("company_project_status")
                var companyProjectStatus: String,
                @SerializedName("status")
                var status: Int,
                @SerializedName("status_text")
                var statusText: String
            )

            class Priority(
                @SerializedName("company_id")
                var companyId: Int,
                @SerializedName("priority_description")
                var priorityDescription: String,
                @SerializedName("priority_id")
                var priorityId: Int,
                @SerializedName("priority_name")
                var priorityName: String
            )
        }
    }
}