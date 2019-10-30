import model.JobsList;
import model.Problem;
import model.Solution;

public class Main {
	
	public static void main(String[] args) {
		Problem problem = new Problem("res/tai01.txt");
        JobsList res = problem.createNEHList(problem.getNbMachines());
        Solution sol = new Solution(res, problem.getNbMachines());
        sol.display();
	}

}
