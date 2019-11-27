import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.opencsv.CSVWriter;

import hnn.HNN;
import model.JobsList;
import model.Problem;
import model.Solution;

public class Main {
	
	public static void main(String[] args) throws IOException {
		//	NEH
//		Problem problem = new Problem("res/tai01.txt");
//        JobsList res = problem.createNEHList(problem.getNbMachines());
//        Solution sol = new Solution(res, problem.getNbMachines());
//        sol.display();
		
		//	HNN Display
//		Problem problem = new Problem("res/tai11.txt");
//		int n = problem.getNbJobs();
//		
//		int A = 100;
//		int B = 100;
//		int C = 100;
//		int D = 70;
//		double T = 0.02;
//		double delta = 0.0001;
//		double[][] v_init = new double[n][n];
//		for(int i = 0; i < n; i++) {
//			for(int j = 0; j < n; j++) {
////				v_init[i][j] = ThreadLocalRandom.current().nextInt(0, 2);
//				v_init[i][j] = i == j ? 1 : 0;
//			}
//		}
//		
//		HNN network = new HNN(problem, A, B, C, D, T, delta, v_init);
//		double[] result = network.run();
//		System.out.println("The neural network has converged");
		
		String name = "res/tai11.txt";
		Problem problem = new Problem(name);
		int n = problem.getNbJobs();
		
		int[] A_B = new int[] {100};
		int[] C = new int[] {90, 100, 110};
		int[] D = new int[] {70, 80, 90, 100, 110, 120};
		double T = 0.02; double delta = 0.0001;
		double[][] v_init = new double[n][n];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
//				v_init[i][j] = ThreadLocalRandom.current().nextInt(0, 2);
				v_init[i][j] = i == j ? 1 : 0;
			}
		}
		
		int rep = 100;
		
		CSVWriter writer = new CSVWriter(new FileWriter("C:/Users/leovu/Desktop/training_HNN.csv"), ',', ' ',' ', "\n");
		String[] headers = "name_instance,nb_jobs,nb_machines,config,param_A,param_B,param_C,param_D, Energy, HNN_score, iter".split(",");
		writer.writeNext(headers);
		
		HNN network;
		double[] result;
		int config = 0;
		for(int a_b: A_B) {
			for(int c: C) {
				for(int d: D) {
					System.out.println("A = " + a_b + " ; B = " + a_b + " ; C = " + c + " ; D = " + d);
					for(int i = 0; i < rep; i++) {
						network = new HNN(problem, a_b, a_b, c, d, T, delta, v_init);
						try {
							result = network.run(); // [energy, makespan]
							String[] row = new String[] {name, ""+n, ""+problem.getNbMachines(), ""+config, ""+a_b, ""+a_b, ""+c, ""+d, ""+result[0]/d, ""+result[1], ""+result[2]};
							writer.writeNext(row);
						}
						catch (Error e) {
							//iteration exceeded
							String[] row = new String[] {name, ""+n, ""+problem.getNbMachines(), ""+config, ""+a_b, ""+a_b, ""+c, ""+d, "undefined", "undefined", "undefined"};
							writer.writeNext(row);
						}
					}
					config++;
				}
			}
		}
		writer.close();
		System.out.println("END");
	}

}
