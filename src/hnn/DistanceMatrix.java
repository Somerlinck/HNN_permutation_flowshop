package hnn;

import model.Job;

public class DistanceMatrix {
	private Job[] jobs;
	private int[][] distances;
	private int size;
	
	//default
	public DistanceMatrix() {
		this.jobs = new Job[0];
		this.setDistances(new int[0][0]);
		this.size = 0;
	}
	
	// TODO
	public DistanceMatrix(Job[] jl) {
		this.jobs = jl;
		this.size = jl.length;
		this.setDistances(new int[this.size][this.size]);
		
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				if(i != j) {
					Job job_start = this.jobs[i];
					Job job_end = this.jobs[j];
					int res = 0;
					// TO DO
					this.distances[i][j] = res;
				}
			}
		}
	}

	public int[][] getDistances() {
		return distances;
	}

	public void setDistances(int[][] distances) {
		this.distances = distances;
	}

}
