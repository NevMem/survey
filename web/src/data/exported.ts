interface AllRolesResponse {
	roles: Role[];
}

interface AllSurveysResponse {
	surveys: Survey[];
}

interface CommonQuestion {
	id: string;
}

interface CreateExportDataTaskRequest {
	surveyId: number;
}

interface CreateInviteError extends CreateInviteResponse {
	message: string;
}

interface CreateInviteRequest {
	expirationTimeSeconds: number;
}

interface CreateInviteResponse {
	type: string;
}

interface CreateInviteSuccess extends CreateInviteResponse {
	invite: Invite;
}

interface CreateSurveyError extends CreateSurveyResponse {
	message: string;
}

interface CreateSurveyRequest {
	name: string;
	questions: Question[];
	commonQuestions: CommonQuestion[];
}

interface CreateSurveyResponse {
	type: string;
}

interface CreateSurveySuccess extends CreateSurveyResponse {
	survey: Survey;
}

interface DeleteSurveyRequest {
	surveyId: number;
}

interface GetInvitesResponse {
	invites: Invite[];
}

interface GetSurveyRequest {
	surveyId: string;
}

interface GetSurveyResponse {
	survey: Survey;
}

interface Invite {
	inviteId: string;
	acceptedBy: User | undefined;
	isExpired: Boolean;
}

interface LoadSurveyMetadataRequest {
	surveyId: number;
}

interface LoadSurveyMetadataResponse {
	surveyMetadata: SurveyMetadata;
}

interface LoadTaskRequest {
	id: number;
}

interface LoginError extends LoginResponse {
	error: string;
}

interface LoginRequest {
	login: string;
	password: string;
}

interface LoginResponse {
	type: string;
}

interface LoginSuccessful extends LoginResponse {
	token: string;
}

interface ManagedUsersResponse {
	users: User[];
}

interface Media {
	id: number;
	filename: string;
	url: string;
	bucketName: string;
}

interface MediaGallery {
	id: number;
	gallery: Media[];
}

interface Question {
	type: string;
}

interface QuestionAnswer {
	type: string;
}

interface RatingQuestion extends Question {
	title: string;
	min: number;
	max: number;
}

interface RatingQuestionAnswer extends QuestionAnswer {
	number: number;
	commonQuestionId: string | undefined;
}

interface RegisterError extends RegisterResponse {
	message: string;
}

interface RegisterRequest {
	name: string;
	surname: string;
	login: string;
	password: string;
	email: string;
	inviteId: string;
}

interface RegisterResponse {
	type: string;
}

interface RegisterSuccessful extends RegisterResponse {
	token: string;
}

interface Role {
	id: string;
}

interface ServerError {
	message: string;
}

interface StarsQuestion extends Question {
	title: string;
	stars: number;
}

interface StarsQuestionAnswer extends QuestionAnswer {
	stars: number;
	commonQuestionId: string | undefined;
}

interface Survey {
	id: number;
	surveyId: string;
	name: string;
	questions: Question[];
	commonQuestions: CommonQuestion[];
}

interface SurveyAnswer {
	publisherId: string;
	surveyId: string;
	answers: QuestionAnswer[];
	gallery: MediaGallery | undefined;
}

interface SurveyMetadata {
	answersCount: number;
	filesCount: number;
}

interface Task {
	id: number;
	state: TaskState;
	log: TaskLog[];
	outputs: Media[];
}

interface TaskLog {
	message: string;
	timestamp: number;
}

interface TextQuestion extends Question {
	title: string;
	maxLength: number;
}

interface TextQuestionAnswer extends QuestionAnswer {
	text: string;
	commonQuestionId: string | undefined;
}

interface UpdateRolesRequest {
	user: User;
	roles: Role[];
}

interface UpdateRolesResponse {
	user: User;
	roles: Role[];
}

interface User {
	id: number;
	login: string;
	name: string;
	surname: string;
	email: string;
	roles: Role[];
	allAvailableRoles: Role[];
}

export function instanceOfCreateInviteError(object: CreateInviteResponse): object is CreateInviteError {
	return object.type === "error";
}

export function instanceOfCreateInviteSuccess(object: CreateInviteResponse): object is CreateInviteSuccess {
	return object.type === "success";
}

export function instanceOfCreateSurveyError(object: CreateSurveyResponse): object is CreateSurveyError {
	return object.type === "error";
}

export function instanceOfCreateSurveySuccess(object: CreateSurveyResponse): object is CreateSurveySuccess {
	return object.type === "success";
}

export function instanceOfLoginError(object: LoginResponse): object is LoginError {
	return object.type === "error";
}

export function instanceOfLoginSuccessful(object: LoginResponse): object is LoginSuccessful {
	return object.type === "success";
}

export function instanceOfRatingQuestion(object: Question): object is RatingQuestion {
	return object.type === "rating";
}

export function instanceOfRatingQuestionAnswer(object: QuestionAnswer): object is RatingQuestionAnswer {
	return object.type === "rating";
}

export function instanceOfRegisterError(object: RegisterResponse): object is RegisterError {
	return object.type === "error";
}

export function instanceOfRegisterSuccessful(object: RegisterResponse): object is RegisterSuccessful {
	return object.type === "success";
}

export function instanceOfStarsQuestion(object: Question): object is StarsQuestion {
	return object.type === "stars";
}

export function instanceOfStarsQuestionAnswer(object: QuestionAnswer): object is StarsQuestionAnswer {
	return object.type === "stars";
}

export function instanceOfTextQuestion(object: Question): object is TextQuestion {
	return object.type === "text";
}

export function instanceOfTextQuestionAnswer(object: QuestionAnswer): object is TextQuestionAnswer {
	return object.type === "text";
}

enum TaskState {
    Waiting = "Waiting",
    Executing = "Executing",
    Success = "Success",
    Error = "Error"
}

export {
	TaskState,
}

export type {
	AllRolesResponse,
	AllSurveysResponse,
	CommonQuestion,
	CreateExportDataTaskRequest,
	CreateInviteError,
	CreateInviteRequest,
	CreateInviteResponse,
	CreateInviteSuccess,
	CreateSurveyError,
	CreateSurveyRequest,
	CreateSurveyResponse,
	CreateSurveySuccess,
	DeleteSurveyRequest,
	GetInvitesResponse,
	GetSurveyRequest,
	GetSurveyResponse,
	Invite,
	LoadSurveyMetadataRequest,
	LoadSurveyMetadataResponse,
	LoadTaskRequest,
	LoginError,
	LoginRequest,
	LoginResponse,
	LoginSuccessful,
	ManagedUsersResponse,
	Media,
	MediaGallery,
	Question,
	QuestionAnswer,
	RatingQuestion,
	RatingQuestionAnswer,
	RegisterError,
	RegisterRequest,
	RegisterResponse,
	RegisterSuccessful,
	Role,
	ServerError,
	StarsQuestion,
	StarsQuestionAnswer,
	Survey,
	SurveyAnswer,
	SurveyMetadata,
	Task,
	TaskLog,
	TextQuestion,
	TextQuestionAnswer,
	UpdateRolesRequest,
	UpdateRolesResponse,
	User,
}
