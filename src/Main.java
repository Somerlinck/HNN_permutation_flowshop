import hnn.HNN;
import model.JobsList;
import model.Problem;
import model.Solution;

public class Main {
	
	public static void main(String[] args) {
		//	NEH
//		Problem problem = new Problem("res/tai01.txt");
//        JobsList res = problem.createNEHList(problem.getNbMachines());
//        Solution sol = new Solution(res, problem.getNbMachines());
//        sol.display();
		
		//	HNN Display
		Problem problem = new Problem("res/tai01.txt");
		int n = problem.getNbJobs();
		
		int A = 500;
		int B = 500;
		int C = 1000;
		int D = 500;
		double T = 0.02;
		double delta = 0.0001;
		double[][] u_init = new double[n][n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				u_init[i][j] = - T * Math.log((n-1)/2);
			}
		}
		
		HNN network = new HNN(problem, A, B, C, D, T, delta, u_init);
		network.run();
	}

}
