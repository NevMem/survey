package com.nevmem.survey

object TableNames {
    val invitesTableName = withVersion("invite")
    val mediaTableName = withVersion("media")
    val mediaGalleryTableName = withVersion("media_gallery")
    val surveyAnswerTableName = withVersion("survey_answer")
    val questionAnswerTableName = withVersion("question_answer")
    val projectsTableName = withVersion("project")
    val userProjectAssignTableName = withVersion("user_project_assign")
    val userProjectRoleTableName = withVersion("user_project_role")
    val surveyTableName = withVersion("survey")
    val questionTableName = withVersion("question")
    val commonQuestionTableName = withVersion("common_question")
    val exportDataTaskTableName = withVersion("export_data_task")
    val taskLogTableName = withVersion("task_log")
    val userTableName = withVersion("user")

    private fun withVersion(name: String): String {
        return "${name}_v1"
    }
}
