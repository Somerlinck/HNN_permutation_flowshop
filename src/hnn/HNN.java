package hnn;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import model.Job;
import model.Problem;

public class HNN {
	private int size;
	private Neuron[][] network;
	private Problem problem;
	private DistanceMatrix distances;
	private Job[] jobs;
	private int A, B, C, D;
	private double T;
	private double delta;
	
	//default
	public HNN() {
		this.size = 0;
		this.setNetwork(new Neuron[0][0]);
		this.setProblem(new Problem());
		this.distances = new DistanceMatrix();
		this.jobs = new Job[0];
		this.setA(0);
		this.setB(0);
		this.setC(0);
		this.setD(0);
		this.setT(0f);
		this.setDelta(0f);
	}
	
	public HNN(int size) {
		this.size = size;
		this.setNetwork(new Neuron[size][size]);
		this.setProblem(new Problem());
		this.distances = new DistanceMatrix();
		this.jobs = new Job[size];
		this.setA(0);
		this.setB(0);
		this.setC(0);
		this.setD(0);
		this.setT(0f);
		this.setDelta(0f);
	}
	
	public HNN(Problem pb, int a, int b, int c, int d, double t, double delta, double[][] u_init) {
		this.size = pb.getNbJobs();
		this.network = new Neuron[this.size][this.size];
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				this.network[i][j] = new Neuron(i, j, u_init[i][j]);
			}
		}
		this.setProblem(pb);
		this.distances = new DistanceMatrix(pb.getJobs());
		this.jobs = pb.getJobs();
		this.setA(a);
		this.setB(b);
		this.setC(c);
		this.setD(d);
		this.setT(t);
		this.setDelta(delta);
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Neuron[][] getNetwork() {
		return network;
	}

	public void setNetwork(Neuron[][] network) {
		this.network = network;
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
	public void run() {
		while(!isSolution()) {
			double energy = this.getEnergy()-1;
			while(energy != this.getEnergy()) {
				energy = this.getEnergy();
				updateNetwork();
			}
		}
	}
	
	private void updateNetwork() {
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				this.network[i][j].update(A, B, C, D, T, delta, network, distances);
			}
		}
	}

	public void display() {
		if(this.isSolution()) {
			String row = "(";
			for(int j = 0; j < this.size; j++) {
				for(int i = 0; i < this.size; i++) {
					if(this.network[i][j].getV() == 1) {
						row += " " + i + " ";
						break;
					}
				}
			}
			row += ")";
			System.out.println(row);
		}
		else {
			System.out.println("The HNN does not represent a feasible solution.");
		}
		
		String sep = "";
		for(int k = 0; k < 4*this.size; k++) {
			sep += "-";
		}
		System.out.println(sep);
		for(int i = 0; i < this.size; i++) {
			String row = "| ";
			for(int j = 0; j < this.size; j++) {
				row +=  network[i][j].getV() + " | ";
			}
			System.out.println(row);
			System.out.println(sep);
		}
	}
	
	public boolean isSolution() {
		// rows
		for(int i = 0; i < this.size; i++) {
			int sum = 0;
			for(int j = 0; j < this.size; j++) {
				sum += this.network[i][j].getV();
				if (sum > 1) {
					return false;
				}
			}
			if (sum == 0) {
				return false;
			}
		}
		
		// columns
		for(int i = 0; i < this.size; i++) {
			int sum = 0;
			for(int j = 0; j < this.size; j++) {
				sum += this.network[j][i].getV();
				if (sum > 1) {
					return false;
				}
			}
			if (sum == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public double getEnergy() {
		double sum_a = 0;
		for(int j = 0; j < this.size; j++) {
			for(int i = 0; i < this.size; i++) {
				for(int k = 0; k < this.size; k++) {
					if(k != i) {
						sum_a += network[i][j].getV() * network[k][j].getV();
					}
				}
			}
		}
		
		double sum_b = 0;
		for(int j = 0; j < this.size; j++) {
			for(int i = 0; i < this.size; i++) {
				for(int l = 0; l < this.size; l++) {
					if(l != j) {
						sum_b += network[i][j].getV() * network[i][l].getV();
					}
				}
			}
		}
		
		double sum_c = -this.size;
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				sum_c += network[i][j].getV();
			}
		}
		
		double sum_d = 0;
		for(int i = 0; i < this.size; i++) {
			for(int k = 0; k < this.size; k++) {
				if( k!= i) {
					for(int j = 0; j < this.size; j++) {
						if(i == 0) {
							sum_d += distances.get(i, k) * network[i][j].getV() * (network[k][i + 1].getV() + network[k][this.size - 1].getV());
						}
						if(i == this.size - 1) {
							sum_d += distances.get(i, k) * network[i][j].getV() * (network[k][0].getV() + network[k][i - 1].getV());
						}
						else{
							sum_d += distances.get(i, k) * network[i][j].getV() * (network[k][i + 1].getV() + network[k][i - 1].getV());
						}
					}
				}
			}
		}
		
		return A/2 * sum_a + B/2 * sum_b + C/2 * sum_c*sum_c + D/2 * sum_d;
	}
	
}
