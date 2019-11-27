package hnn;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import model.Job;
import model.JobsList;
import model.Problem;
import model.Solution;

public class HNN {
	private int size;
	private Network network;
	private Problem problem;
	private DistanceMatrix distances;
	private Job[] jobs;
	private int A, B, C, D;
	private double T;
	private double delta;
	private int m; // number of machines
	
	//default
	public HNN() {
		this.size = 0;
		this.distances = new DistanceMatrix();
		this.setProblem(new Problem());
		this.jobs = new Job[0];
		this.setA(0);
		this.setB(0);
		this.setC(0);
		this.setD(0);
		this.setT(0f);
		this.setDelta(0f);
		this.m = 0;
		this.setNetwork(new Neuron[0][0]);
	}
	
	public HNN(int size) {
		this.size = size;
		this.distances = new DistanceMatrix();
		this.setProblem(new Problem());
		this.jobs = new Job[size];
		this.setA(0);
		this.setB(0);
		this.setC(0);
		this.setD(0);
		this.setT(0f);
		this.setDelta(0f);
		this.m = 0;
		this.setNetwork(new Neuron[size][size]);
	}
	
	public HNN(Problem pb, int a, int b, int c, int d, double t, double delta, double[][] v_init) {
		this.size = pb.getNbJobs();
		this.distances = new DistanceMatrix(pb.getJobs());
		this.setProblem(pb);
		this.jobs = pb.getJobs();
		this.setA(a);
		this.setB(b);
		this.setC(c);
		this.setD(d);
		this.setT(t);
		this.setDelta(delta);
		this.m = pb.getNbMachines();
		Neuron[][] n = new Neuron[this.size][this.size];
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				n[i][j] = new Neuron(i, j, v_init[i][j]);
			}
		}
		this.setNetwork(n);
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Neuron[][] network) {
		this.network = new Network(network);
		this.computeEnergy();
	}

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public int getC() {
		return C;
	}

	public void setC(int c) {
		C = c;
	}

	public int getD() {
		return D;
	}

	public void setD(int d) {
		D = d;
	}

	public double getT() {
		return T;
	}

	public void setT(double t) {
		T = t;
	}
	
	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}
	
	// TODO
	public double[] run() {
		int iter = 0;
		Network best = this.network.copy();
		while(!best.isSolution() || iter < 10) {
			best = this.network.copy();
			iter += 1;
//			System.out.println("Iteration: "+iter);
			ShannonsEntropy criterion = new ShannonsEntropy();
			while(!criterion.hasConverged(best)) {
				updateNetwork();
				this.computeEnergy();
				if(this.network.getEnergy() < best.getEnergy()) {
					best = this.network.copy();
				}
			}
			if (iter >300) {
				throw new Error("The number of iteration exceeded the limit of 300.");
			}
		}
//		System.out.println("Energy :" + best.getEnergy());
		this.network = best;
//		display();
		return new double[] {best.getEnergy(), this.getMakespan(), iter};
	}
	
	private void updateNetwork() {
		//	continuous model
//		for(int i = 0; i < this.size; i++) {
//			for(int j = 0; j < this.size; j++) {
//				this.network.get(i,j).update(A, B, C, D, T, delta, network, distances);
//			}
//		}
		
		//	discrete model
		for(int k = 0; k < 5 * this.size*this.size; k++ ) {
			int randomI = ThreadLocalRandom.current().nextInt(0, this.size);
			int randomJ = ThreadLocalRandom.current().nextInt(0, this.size);
			this.network.get(randomI, randomJ).update(A, B, C, D, T, delta, network, distances);
		}
	}

	public void display() {
		System.out.println();
		if(this.network.isSolution()) {
			JobsList jl = new JobsList();
			for(int j = 0; j < this.size; j++) {
				for(int i = 0; i < this.size; i++) {
					if(this.network.get(i, j).getV() == 1) {
						jl.addJob(jobs[i]);
						break;
					}
				}
			}
			
			Solution sol = new Solution(jl, this.m);
			sol.display();
		}
		else {
			System.out.println("The HNN does not represent a feasible solution.");
			return;
		}
		
		String sep = " ";
		for(int k = 0; k < 6*this.size; k++) {
			sep += "-";
		}
		System.out.println(sep);
		for(int i = 0; i < this.size; i++) {
			String row = "| ";
			for(int j = 0; j < this.size; j++) {
				row +=  network.get(i, j).getV() + " | ";
			}
			System.out.println(row);
			System.out.println(sep);
		}
	}
	
	public void computeEnergy() {
		double sum_a = 0;
		for(int j = 0; j < this.size; j++) {
			for(int i = 0; i < this.size; i++) {
				for(int k = 0; k < this.size; k++) {
					if(k != i) {
						sum_a += network.get(i,j).getV() * network.get(k,j).getV();
					}
				}
			}
		}
		
		double sum_b = 0;
		for(int j = 0; j < this.size; j++) {
			for(int i = 0; i < this.size; i++) {
				for(int l = 0; l < this.size; l++) {
					if(l != j) {
						sum_b += network.get(i,j).getV() * network.get(i,l).getV();
					}
				}
			}
		}
		
		double sum_c = -this.size;
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				sum_c += network.get(i,j).getV();
			}
		}
		
		double sum_d = 0;
		for(int i = 0; i < this.size; i++) {
			for(int k = 0; k < this.size; k++) {
				if( k!= i) {
					for(int j = 0; j < this.size; j++) {
						if(i == 0) {
							sum_d += distances.get(i, k) * network.get(i,j).getV() * (network.get(k, i+1).getV() + network.get(k, this.size-1).getV());
						}
						else if(i == this.size - 1) {
							sum_d += distances.get(i, k) * network.get(i,j).getV() * (network.get(k,0).getV() + network.get(k,i-1).getV());
						}
						else{
							sum_d += distances.get(i, k) * network.get(i,j).getV() * (network.get(k,i+1).getV() + network.get(k,i-1).getV());
						}
					}
				}
			}
		}
		
		this.network.setEnergy(A/2 * sum_a + B/2 * sum_b + C/2 * sum_c*sum_c + D/2 * sum_d);
	}
	
	public int getMakespan() {
		if(this.network.isSolution()) {
			JobsList jl = new JobsList();
			for(int j = 0; j < this.size; j++) {
				for(int i = 0; i < this.size; i++) {
					if(this.network.get(i, j).getV() == 1) {
						jl.addJob(jobs[i]);
						break;
					}
				}
			}
			
			Solution sol = new Solution(jl, this.m);
			return sol.getMakespan();
		}
		else throw new Error("The HNN does not represent a feasible solution, cannot get Makespan");
	}
	
}
