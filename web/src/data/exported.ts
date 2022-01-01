interface Invite {
	inviteId: string;
	acceptedBy: User | undefined;
	isExpired: Boolean;
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

interface CreateSurveyRequest {
	name: string;
	questions: Question[];
	commonQuestions: CommonQuestion[];
}

interface DeleteSurveyRequest {
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

interface AllSurveysResponse {
	surveys: Survey[];
}

interface CreateSurveyResponse {
	type: string;
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

interface User {
	id: number;
	login: string;
	name: string;
	surname: string;
	email: string;
	roles: Role[];
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
	AllSurveysResponse,
	CommonQuestion,
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
	LoginError,
	LoginRequest,
	LoginResponse,
	LoginSuccessful,
	Question,
	RatingQuestion,
	RegisterError,
	RegisterRequest,
	RegisterResponse,
	RegisterSuccessful,
	Role,
	ServerError,
	StarsQuestion,
	Survey,
	TextQuestion,
	User,
}
