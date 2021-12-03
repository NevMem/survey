interface NotificationAction {
    message: string;
    action: (notificationId: string) => void
};

export type {
    NotificationAction,
};
