package hnn;

import java.util.ArrayList;

import model.Problem;

public class HNN {
	private int size;
	private ArrayList<ArrayList<Neuron>> network;
	private Problem problem;
	
	//default
	public HNN() {
		this.size = 0;
		this.network = new ArrayList<ArrayList<Neuron>>();
		this.problem = new Problem();
	}
	
	public HNN(int size) {
		this.size = size;
		this.network = new ArrayList<ArrayList<Neuron>>();
		ArrayList<Neuron> list;
		for(int i = 0; i < size; i++) {
			list = new ArrayList<Neuron>();
			for(int j = 0; j < size; j++) {
				list.add(new Neuron());
			}
		}
		this.problem = new Problem();
	}
	
	public HNN(Problem pb) {
		this.size = pb.getNbJobs();
		ArrayList<Neuron> list;
		for(int i = 0; i < this.size; i++) {
			list = new ArrayList<Neuron>();
			for(int j = 0; j < this.size; j++) {
				list.add(new Neuron());
			}
		}
		this.problem = pb;
	}
	
}
