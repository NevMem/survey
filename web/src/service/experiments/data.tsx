interface Experiment {
    name: string;
};

interface Feature extends Experiment {};

interface IntExperiment extends Experiment {};

interface ObjectExperiment extends Experiment {};

export type {
    Feature,
    IntExperiment,
    ObjectExperiment,
};
