package hnn;

import java.util.ArrayList;

import model.Job;
import model.Problem;

public class HNN {
	private int size;
	private ArrayList<ArrayList<Neuron>> network;
	private Problem problem;
	private DistanceMatrix distances;
	private Job[] jobs;
	private int A, B, C, D;
	private float T;
	
	//default
	public HNN() {
		this.size = 0;
		this.setNetwork(new ArrayList<ArrayList<Neuron>>());
		this.setProblem(new Problem());
		this.distances = new DistanceMatrix();
		this.jobs = new Job[0];
		this.setA(0);
		this.setB(0);
		this.setC(0);
		this.setD(0);
		this.setT(0f);
	}
	
	public HNN(int size) {
		this.size = size;
		this.setNetwork(new ArrayList<ArrayList<Neuron>>());
		ArrayList<Neuron> list;
		for(int i = 0; i < size; i++) {
			list = new ArrayList<Neuron>();
			for(int j = 0; j < size; j++) {
				list.add(new Neuron());
			}
		}
		this.setProblem(new Problem());
		this.distances = new DistanceMatrix();
		this.jobs = new Job[size];
		this.setA(0);
		this.setB(0);
		this.setC(0);
		this.setD(0);
		this.setT(0f);
	}
	
	public HNN(Problem pb, int a, int b, int c, int d, float t) {
		this.size = pb.getNbJobs();
		ArrayList<Neuron> list;
		for(int i = 0; i < this.size; i++) {
			list = new ArrayList<Neuron>();
			for(int j = 0; j < this.size; j++) {
				list.add(new Neuron());
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
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public ArrayList<ArrayList<Neuron>> getNetwork() {
		return network;
	}

	public void setNetwork(ArrayList<ArrayList<Neuron>> network) {
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

	public float getT() {
		return T;
	}

	public void setT(float t) {
		T = t;
	}
	
	//TO DO
	public void run() {
		
	}
	
	//TO DO
	public void display() {
		
	}
}
