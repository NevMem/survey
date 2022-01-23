interface QuestionAnswer {
	type: string;
}

interface SurveyAnswer {
	surveyId: string;
	answers: QuestionAnswer[];
	gallery: MediaGallery | undefined;
}

interface Invite {
	inviteId: string;
	acceptedBy: User | undefined;
	isExpired: Boolean;
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

interface CommonQuestion {
	id: string;
}

interface Question {
	type: string;
}

interface LoginRequest {
	login: string;
	password: string;
}

interface RegisterRequest {
	name: string;
	surname: string;
	login: string;
	password: string;
	email: string;
	inviteId: string;
}

interface CreateInviteRequest {
	expirationTimeSeconds: number;
}

interface UpdateRolesRequest {
	user: User;
	roles: Role[];
}

interface CreateSurveyRequest {
	name: string;
	questions: Question[];
	commonQuestions: CommonQuestion[];
}

interface DeleteSurveyRequest {
	surveyId: number;
}

interface LoadSurveyMetadataRequest {
	surveyId: number;
}

interface CreateExportDataTaskRequest {
	surveyId: number;
}

interface LoginResponse {
	type: string;
}

interface RegisterResponse {
	type: string;
}

interface ServerError {
	message: string;
}

interface CreateInviteResponse {
	type: string;
}

interface GetInvitesResponse {
	invites: Invite[];
}

interface ManagedUsersResponse {
	users: User[];
}

interface AllRolesResponse {
	roles: Role[];
}

interface UpdateRolesResponse {
	user: User;
	roles: Role[];
}

interface AllSurveysResponse {
	surveys: Survey[];
}

interface CreateSurveyResponse {
	type: string;
}

interface LoadSurveyMetadataResponse {
	surveyMetadata: SurveyMetadata;
}

interface Role {
	id: string;
}

interface Survey {
	id: number;
	surveyId: string;
	name: string;
	questions: Question[];
	commonQuestions: CommonQuestion[];
}

interface SurveyMetadata {
	answersCount: number;
	filesCount: number;
}

interface Task {
	id: number;
	state: TaskState;
	log: TaskLog[];
}

interface TaskLog {
	message: string;
	timestamp: number;
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

interface RatingQuestionAnswer extends QuestionAnswer {
	number: number;
	commonQuestionId: string | undefined;
}

interface TextQuestionAnswer extends QuestionAnswer {
	text: string;
	commonQuestionId: string | undefined;
}

interface StarsQuestionAnswer extends QuestionAnswer {
	stars: number;
	commonQuestionId: string | undefined;
}

interface RatingQuestion extends Question {
	title: string;
	min: number;
	max: number;
}

interface StarsQuestion extends Question {
	title: string;
	stars: number;
}

interface TextQuestion extends Question {
	title: string;
	maxLength: number;
}

interface LoginSuccessful extends LoginResponse {
	token: string;
}

interface LoginError extends LoginResponse {
	error: string;
}

interface RegisterSuccessful extends RegisterResponse {
	token: string;
}

interface RegisterError extends RegisterResponse {
	message: string;
}

interface CreateInviteError extends CreateInviteResponse {
	message: string;
}

interface CreateInviteSuccess extends CreateInviteResponse {
	invite: Invite;
}

interface CreateSurveySuccess extends CreateSurveyResponse {
	survey: Survey;
}

interface CreateSurveyError extends CreateSurveyResponse {
	message: string;
}

export function instanceOfRatingQuestionAnswer(object: QuestionAnswer): object is RatingQuestionAnswer {
	return object.type === "rating";
}

export function instanceOfTextQuestionAnswer(object: QuestionAnswer): object is TextQuestionAnswer {
	return object.type === "text";
}

export function instanceOfStarsQuestionAnswer(object: QuestionAnswer): object is StarsQuestionAnswer {
	return object.type === "stars";
}

export function instanceOfRatingQuestion(object: Question): object is RatingQuestion {
	return object.type === "rating";
}

export function instanceOfStarsQuestion(object: Question): object is StarsQuestion {
	return object.type === "stars";
}

export function instanceOfTextQuestion(object: Question): object is TextQuestion {
	return object.type === "text";
}

export function instanceOfLoginSuccessful(object: LoginResponse): object is LoginSuccessful {
	return object.type === "success";
}

export function instanceOfLoginError(object: LoginResponse): object is LoginError {
	return object.type === "error";
}

export function instanceOfRegisterSuccessful(object: RegisterResponse): object is RegisterSuccessful {
	return object.type === "success";
}

export function instanceOfRegisterError(object: RegisterResponse): object is RegisterError {
	return object.type === "error";
}

export function instanceOfCreateInviteError(object: CreateInviteResponse): object is CreateInviteError {
	return object.type === "error";
}

export function instanceOfCreateInviteSuccess(object: CreateInviteResponse): object is CreateInviteSuccess {
	return object.type === "success";
}

export function instanceOfCreateSurveySuccess(object: CreateSurveyResponse): object is CreateSurveySuccess {
	return object.type === "success";
}

export function instanceOfCreateSurveyError(object: CreateSurveyResponse): object is CreateSurveyError {
	return object.type === "error";
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
	Invite,
	LoadSurveyMetadataRequest,
	LoadSurveyMetadataResponse,
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
