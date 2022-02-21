package com.nevmem.survey.survey

class AlreadyPublishedAnswerException : Exception("Answer was already published")

class SurveyNotFoundException : Exception("Survey not found")

class SurveyAnswerInconsistencyException : Exception("Survey answer is not consistent to actual survey")

class UnknownCommonQuestionException(commonQuestionId: String) : Exception("Unknown common question type $commonQuestionId")
