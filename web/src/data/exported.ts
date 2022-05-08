interface AcceptInviteRequest {
	id: number;
}

interface AcceptInviteResponse {
	status: AcceptInviteStatus;
}

interface Administrator {
	id: number;
	login: string;
	name: string;
	surname: string;
	email: string;
}

interface AllRolesResponse {
	roles: Role[];
}

interface AllSurveysResponse {
	surveys: Survey[];
}

interface BroadcastAllRequest {
	title: string;
	message: string;
}

interface BroadcastAllResponse {
	sentMessages: number;
}

interface CommonQuestion {
	id: string;
}

interface CreateExportDataTaskRequest {
	surveyId: number;
}

interface CreateInviteRequest {
	projectId: number;
	userLogin: string;
	expirationTimeSeconds: number;
}

interface CreateInviteResponse {
	invite: Invite;
}

interface CreateProjectRequest {
	name: string;
}

interface CreateProjectResponse {
	project: Project;
}

interface CreateSurveyError extends CreateSurveyResponse {
	message: string;
}

interface CreateSurveyRequest {
	projectId: number;
	name: string;
	questions: Question[];
	commonQuestions: CommonQuestion[];
	answerCoolDown: number;
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

interface GetGalleryRequest {
	id: number;
}

interface GetGalleryResponse {
	gallery: MediaGallery;
}

interface GetInvitesResponse {
	invites: Invite[];
}

interface GetProjectInfoRequest {
	projectId: number;
}

interface GetProjectInfoResponse {
	projectInfo: ProjectInfo;
}

interface GetProjectRequest {
	id: number;
}

interface GetProjectResponse {
	project: Project;
}

interface GetProjectsResponse {
	projects: Project[];
}

interface GetSurveyRequest {
	id: number;
}

interface GetSurveyResponse {
	survey: Survey;
}

interface GetSurveysRequest {
	projectId: number;
}

interface GetSurveysResponse {
	projectId: number;
	surveys: Survey[];
}

interface IncomingInvitesResponse {
	invites: Invite[];
}

interface Invite {
	id: number;
	project: Project;
	toUser: Administrator;
	fromUser: Administrator;
	status: InviteStatus;
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
	administrators: Administrator[];
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

interface OutgoingInvitesResponse {
	invites: Invite[];
}

interface Project {
	id: number;
	name: string;
	owner: Administrator;
}

interface ProjectAdministratorInfo {
	administrator: Administrator;
	roles: Role[];
}

interface ProjectInfo {
	administratorsInfo: ProjectAdministratorInfo[];
}

interface Question {
	type: string;
}

interface QuestionVariant {
	id: string;
	variant: string;
}

interface RadioQuestion extends Question {
	title: string;
	variants: QuestionVariant[];
}

interface RatingQuestion extends Question {
	title: string;
	min: number;
	max: number;
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

interface Survey {
	id: number;
	projectId: number;
	surveyId: string;
	name: string;
	questions: Question[];
	commonQuestions: CommonQuestion[];
	surveyCoolDown: number;
}

interface SurveyMetadata {
	answersCount: number;
	filesCount: number;
}

interface Task {
	id: number;
	projectId: number;
	state: TaskState;
	log: TaskLog[];
	outputs: Media[];
}

interface TaskLog {
	message: string;
	timestamp: number;
}

interface TasksInSurvey {
	surveyId: number;
}

interface TextQuestion extends Question {
	title: string;
	maxLength: number;
}

interface UpdateRolesRequest {
	administrator: Administrator;
	roles: Role[];
}

interface UpdateRolesResponse {
	administrator: Administrator;
	roles: Role[];
}

interface UserId {
	uuid: string;
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

export function instanceOfRadioQuestion(object: Question): object is RadioQuestion {
	return object.type === "radio";
}

export function instanceOfRatingQuestion(object: Question): object is RatingQuestion {
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

export function instanceOfTextQuestion(object: Question): object is TextQuestion {
	return object.type === "text";
}

enum AcceptInviteStatus {
    Ok = "Ok",
    Expired = "Expired"
}

enum InviteStatus {
    Accepted = "Accepted",
    Expired = "Expired",
    Waiting = "Waiting"
}

enum TaskState {
    Waiting = "Waiting",
    Executing = "Executing",
    Success = "Success",
    Error = "Error"
}

export {
	AcceptInviteStatus,
	InviteStatus,
	TaskState,
}

export type {
	AcceptInviteRequest,
	AcceptInviteResponse,
	Administrator,
	AllRolesResponse,
	AllSurveysResponse,
	BroadcastAllRequest,
	BroadcastAllResponse,
	CommonQuestion,
	CreateExportDataTaskRequest,
	CreateInviteRequest,
	CreateInviteResponse,
	CreateProjectRequest,
	CreateProjectResponse,
	CreateSurveyError,
	CreateSurveyRequest,
	CreateSurveyResponse,
	CreateSurveySuccess,
	DeleteSurveyRequest,
	GetGalleryRequest,
	GetGalleryResponse,
	GetInvitesResponse,
	GetProjectInfoRequest,
	GetProjectInfoResponse,
	GetProjectRequest,
	GetProjectResponse,
	GetProjectsResponse,
	GetSurveyRequest,
	GetSurveyResponse,
	GetSurveysRequest,
	GetSurveysResponse,
	IncomingInvitesResponse,
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
	OutgoingInvitesResponse,
	Project,
	ProjectAdministratorInfo,
	ProjectInfo,
	Question,
	QuestionVariant,
	RadioQuestion,
	RatingQuestion,
	RegisterError,
	RegisterRequest,
	RegisterResponse,
	RegisterSuccessful,
	Role,
	ServerError,
	StarsQuestion,
	Survey,
	SurveyMetadata,
	Task,
	TaskLog,
	TasksInSurvey,
	TextQuestion,
	UpdateRolesRequest,
	UpdateRolesResponse,
	UserId,
}
