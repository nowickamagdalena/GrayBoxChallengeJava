import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class Max3SatProblem {
    static final int DEFAULT_CLAUSE_LENGTH = 3;
    private ArrayList<int[][]> clauses;
    private ArrayList<Integer> variables;
    private ArrayList<Integer>[] clausesByVars;
    private int clauseLength;

    public Max3SatProblem(){
        this.clauseLength = DEFAULT_CLAUSE_LENGTH;
        this.clausesByVars = null;
        this.variables = null;
        this.clauses = new ArrayList<>();
    }

    //loading txt file with defined clauses
    public boolean load(String fileName){
        BufferedReader br = null;
        FileReader fr = null;
        TreeSet<Integer> varSet = new TreeSet<>();
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            String line;
            //reading file line by line to get one clause at a time
            while((line = br.readLine()) != null){
                //creating new clause as two dimensional array
                int[][] nextClause = new int[this.clauseLength][2];
                int i = 0;
                String[] words = line.split(" ");
                for(String s : words){
                    //checking if the word is variable, not bracket
                    if(isInteger(s)){
                        int currentVar = Math.abs(Integer.parseInt(s));
                        //adding variable to clause
                        nextClause[i][0] = currentVar;
                        //adding variable sign to clause
                        if(s.charAt(0) == '-')
                            nextClause[i][1] = -1;
                        else
                            nextClause[i][1] = 0;
                        //adding variable to the set
                        varSet.add(currentVar);
                        i++;
                    }
                }
                this.clauses.add(nextClause);
            }
            // creating arrayList from set, as set is complete
            this.variables = new ArrayList<>(varSet);
            //segregating loaded clauses to variables, to optimize searching
            segregateClausesToVariables();

//            printClauses();
//            printVariables();
//            printSegregatedClauses();
            return true;

        }catch (FileNotFoundException e){
            System.out.println("File not found");

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(fr != null)
                    fr.close();
                if(br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isInteger(String str) {
        try{
            Integer.parseInt(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private void segregateClausesToVariables() {
        //creating array for all variables to store clauses, which include the variable
        this.clausesByVars = new ArrayList[this.variables.size()];

        for( int i = 0; i < this.clausesByVars.length; i++){
            this.clausesByVars[i] = new ArrayList<>();
        }

        for (int i = 0; i < this.clauses.size(); i++) {
            for (int j = 0; j < this.clauseLength; j++) {
                int variable = this.clauses.get(i)[j][0];
                int varIndx = this.variables.indexOf(variable);
                this.clausesByVars[varIndx].add(i);
            }
        }
    }

    public int getVarsNumber() {
        return this.variables.size();
    }

    // computing number of satisfied clauses for given solution
    public int compute(boolean[] solution) {
        int satCount = 0;
        for (int i = 0; i < this.clauses.size(); i++) {
            boolean isSatisfied = false;

            for (int j = 0; j < this.clauseLength && !isSatisfied; j++) {
                int variable = this.clauses.get(i)[j][0];
                int varIndx = this.variables.indexOf(variable);

                if (this.clauses.get(i)[j][1] == -1) {
                    if (!solution[varIndx]) {
                        satCount++;
                        isSatisfied = true;
                    }
                }
                else {
                    if (solution[varIndx]) {
                        satCount++;
                        isSatisfied = true;
                    }
                }
            }
        }
        return satCount;
    }
    // computing number of satisfied clauses containing given variable
    public int computeClausesContainingVariable(boolean[] solution, int variablePosition) {
        int satCount = 0;
        for (int i = 0; i < this.clausesByVars[variablePosition].size(); i++) {
            boolean isSatisfied = false;
            int[][] currClause = this.clauses.get(this.clausesByVars[variablePosition].get(i));

            for (int j = 0; j < this.clauseLength && !isSatisfied; j++) {
                int variable = currClause[j][0];
                int varIndx = this.variables.indexOf(variable);

                if (currClause[j][1] == -1) {
                    if (!solution[varIndx]) {
                        satCount++;
                        isSatisfied = true;
                    }
                }
                else {
                    if (solution[varIndx]) {
                        satCount++;
                        isSatisfied = true;
                    }
                }
            }
        }
        return satCount;
    }

    public int getClausesNumber() {
        return this.clauses.size();
    }
    public void printClauses() {
        for (int i = 0; i < this.clauses.size(); i++) {
            System.out.print("[ ");
            for (int j = 0; j < this.clauseLength; j++) {
                if (this.clauses.get(i)[j][1] == -1)
                    System.out.print("-");
                System.out.print(this.clauses.get(i)[j][0] + " ");
            }
            System.out.print("]\n");
        }
    }

    public void printSegregatedClauses() {
        for (int i = 0; i < this.variables.size(); i++) {
            for (int j = 0; j < this.clausesByVars[i].size(); j++) {
                System.out.print(this.clausesByVars[i].get(j) + ", ");
            }
            System.out.println();
        }
    }

    public void printVariables() {
        for(Integer i : this.variables){
            System.out.println(i);
        }
    }
}
