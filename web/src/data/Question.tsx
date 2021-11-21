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

export default Question;
export type {
    RatingQuestion,
    StarsQuestion,
    TextQuestion,
}
