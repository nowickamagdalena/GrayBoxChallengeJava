public class Main {
    public static void main(String[] args){
        Max3SatProblem problem = new Max3SatProblem();
        if(problem.load("m3s_350_41.txt")){
            System.out.println("Number of clauses: " + problem.getClausesNumber());
            System.out.println(iteration(problem));
        }

    }

    public static String iteration(Max3SatProblem problem) {

        Optimizer opt = new Optimizer(problem);

        opt.initialize();

        for (int i = 0; i < 30; i++) {
            System.out.println("iteration: " + i);
            opt.runIteration();
            opt.getBestFound();
            System.out.println(opt.getBestResult());
            System.out.println(opt.getCurrentSatClauses());
        }
        return opt.getBestFound().getResultAsString();
    }
}
