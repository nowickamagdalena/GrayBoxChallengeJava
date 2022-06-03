import java.util.ArrayList;
import java.util.Random;

public class Optimizer {
    static final int DEFAULT_POPULATION_SIZE = 100;
    static final double DEFAULT_CROSSOVER_THRESHOLD = 0.4;
    static final double DEFAULT_MUTATION_THRESHOLD = 0.3;

    private ArrayList<Individual> solutionsPopulation;
    private int populationSize;
    private double crossoverThreshold;
    private double mutationThreshold;
    private Max3SatProblem problemInstance;
    private Individual currentBest;
    private double currentBestFitness;
    private Random generator;

    public Optimizer(Max3SatProblem problem){
        this.populationSize = DEFAULT_POPULATION_SIZE;
        this.crossoverThreshold = DEFAULT_CROSSOVER_THRESHOLD;
        this.mutationThreshold = DEFAULT_MUTATION_THRESHOLD;
        this.problemInstance = problem;
        this.currentBest = null;
        this.currentBestFitness = 0;
        this.solutionsPopulation = new ArrayList<>();
        this.generator = new Random();
    }

    public void initialize(){
        for (int i = 0; i < this.populationSize; i++) {
            Individual indiv = new Individual(problemInstance.getVarsNumber(), this.problemInstance);
            indiv.randomizeGenotype();
            this.solutionsPopulation.add(indiv);
        }
    }

    public void runIteration(){
        ArrayList<Individual> newSolPopulation = new ArrayList<>();

        while (newSolPopulation.size() < this.solutionsPopulation.size()) {
            Individual parent1 = chooseParent();
            Individual parent2 = chooseParent();
            Individual child1;
            Individual child2;
            if (randomProbability() <= this.crossoverThreshold) {
                ArrayList<Individual> children = Individual.crossover(parent1, parent2);
                child1 = children.get(0);
                child2 = children.get(1);
            }
            else {
                child1 = new Individual(parent1);
                child2 = new Individual(parent2);
            }
            child1.mutation(this.mutationThreshold);
            child2.mutation(this.mutationThreshold);

            newSolPopulation.add(child1);
            newSolPopulation.add(child2);
        }

        this.solutionsPopulation = newSolPopulation;
        findBestResult();
    }


    public Individual chooseParent() {
        int parent1Index = randomPosInt() % this.populationSize;
        int parent2Index = randomPosInt() % this.populationSize;

        if (this.solutionsPopulation.get(parent1Index).fitness() < this.solutionsPopulation.get(parent2Index).fitness())
            return this.solutionsPopulation.get(parent2Index);
        else
            return this.solutionsPopulation.get(parent1Index);
    }

    private double randomProbability() {
        return this.generator.nextDouble();
    }

    private int randomPosInt() {
        return generator.nextInt(Integer.MAX_VALUE);
    }

    public void findBestResult() {
        for (int i = 0; i < this.solutionsPopulation.size(); i++) {
            if (this.solutionsPopulation.get(i).fitness() > this.currentBestFitness) {
                this.currentBest = new Individual(this.solutionsPopulation.get(i));
                this.currentBestFitness = this.solutionsPopulation.get(i).fitness();
            }
        }
    }

    public double getBestResult() {
        return this.currentBestFitness;
    }

    public int getCurrentSatClauses() {
        return this.problemInstance.compute(this.currentBest.getGenotype());
    }

    public Individual getBestFound() {
        return this.currentBest;
    }
}
