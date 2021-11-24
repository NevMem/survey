interface Question {
    title: string
}

interface RatingQuestion extends Question {
    min: number
    max: number
}

interface StarsQuestion extends Question {
    stars: number
}

interface TextQuestion extends Question {
    maxLength: number
}

function instanceOfRatingQuestion(object: any): object is RatingQuestion {
    return 'title' in object && 'min' in object && 'max' in object;
}

function instanceOfStarsQuestion(object: any): object is StarsQuestion {
    return 'title' in object && 'stars' in object;
}

function instanceOfTextQuestion(object: any): object is TextQuestion {
    return 'title' in object && 'maxLength' in object;
}

export {
    instanceOfRatingQuestion,
    instanceOfStarsQuestion,
    instanceOfTextQuestion,
};

export type {
    Question,
    RatingQuestion,
    StarsQuestion,
    TextQuestion,
};
