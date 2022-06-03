import java.util.ArrayList;
import java.util.Random;

public class Individual {
    private boolean[] genotype;
    private int genotypeLength;
    private Max3SatProblem problemInstance;
    private Random generator;


    public Individual(int genLen, Max3SatProblem problem) {
        this.genotypeLength = genLen;
        this.genotype = new boolean[genLen];
        this.problemInstance = problem;
        this.generator = new Random();
    }

    public Individual(Individual other) {
        this.genotypeLength = other.getGenotypeLength();
        this.genotype = new boolean[other.getGenotypeLength()];
        this.problemInstance = other.getProblemInstance();
        this.generator = new Random();

        for (int i = 0; i < other.getGenotypeLength(); i++) {
            this.genotype[i] = other.getGenotype()[i];
        }
    }


    public static ArrayList<Individual> crossover(Individual parent1, Individual parent2) {
        Individual child1 = new Individual(parent1.getGenotypeLength(), parent1.getProblemInstance());
        Individual child2 = new Individual(parent1.getGenotypeLength(), parent1.getProblemInstance());
        Random rand = new Random();
        for (int i = 0; i < parent1.getGenotypeLength(); i++) {
            if (rand.nextInt() % 2 == 0) {
                child1.getGenotype()[i] = parent1.getGenotype()[i];
                child2.getGenotype()[i] = parent2.getGenotype()[i];
            }
            else {
                child1.getGenotype()[i] = parent2.getGenotype()[i];
                child2.getGenotype()[i] = parent1.getGenotype()[i];
            }
        }
        ArrayList<Individual> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        return children;
    }

    public void mutation(double mutationThreshold) {
        for (int i = 0; i < this.genotypeLength; i++) {
            if (randomProbability() <= mutationThreshold) {
                int iSatisfied = this.problemInstance.computeClausesContainingVariable(this.genotype, i);
                this.genotype[i] = !this.genotype[i];

                if (iSatisfied > this.problemInstance.computeClausesContainingVariable(this.genotype, i))
                    this.genotype[i] = !this.genotype[i];
            }
        }
    }

    private double randomProbability() {
        return this.generator.nextDouble();
    }

    private int randomPosInt() {
        return generator.nextInt(Integer.MAX_VALUE);
    }

    public double fitness() {
        return (double)this.problemInstance.compute(this.genotype) / this.problemInstance.getClausesNumber();
    }

    public void randomizeGenotype() {
        for (int i = 0; i < this.genotypeLength; i++) {
            if(randomPosInt() % 2 == 0)
                this.genotype[i] = true;
            else
                this.genotype[i] = false;
        }
    }

    public String getResultAsString() {
        int size = this.problemInstance.getVarsNumber();
        String result = "";

        for (int i = 0; i < size; i++) {
            if(this.genotype[i])
                result += "1";
            else
                result += "0";
        }
        return result;
    }

    public boolean[] getGenotype() {
        return this.genotype;
    }
    public void setGenotype(boolean[] genotype) {
        this.genotype = genotype;
    }

    public int getGenotypeLength() {
        return genotypeLength;
    }

    public void setGenotypeLength(int genotypeLength) {
        this.genotypeLength = genotypeLength;
    }

    public Max3SatProblem getProblemInstance() {
        return problemInstance;
    }

    public void setProblemInstance(Max3SatProblem problemInstance) {
        this.problemInstance = problemInstance;
    }
}
