package hnn;

import model.Job;
import model.JobsList;
import model.Solution;

public class DistanceMatrix {
	private Job[] jobs;
	private double[][] distances;
	private int size;
	
	//default
	public DistanceMatrix() {
		this.jobs = new Job[0];
		this.setDistances(new double[0][0]);
		this.size = 0;
	}
	
	public DistanceMatrix(Job[] jl) {
		this.jobs = jl;
		this.size = jl.length;
		this.setDistances(new double[this.size][this.size]);
		int nbMachines = this.jobs[0].getNbTasks();
		
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				if(i != j) {
					Job job_start = this.jobs[i];
					Job job_end = this.jobs[j];
					int res = 0;
					
					JobsList li = new JobsList();
					li.addJob(job_start); li.addJob(job_end);
					Solution schedule = new Solution(li, nbMachines);
					
					for(int i1 = 0; i1 < nbMachines-1; i1++) {
//						res += job_end.getStartingTime(i1) - (job_start.getStartingTime(i1) + job_start.getProcessingTime(i1));
						res += job_end.getStartingTime(i1) - job_start.getStartingTime(i1+1);
					}
					
					this.distances[i][j] = res;
					if (res < min) {
						min = res;
					}
					if (res > max) {
						max = res;
					}
				}
			}
		}
		
		for(int i = 0; i < this.size; i++) {
			for(int j = 0; j < this.size; j++) {
				this.distances[i][j] = (this.distances[i][j] - min) / (max - min);
			}
		}
	}

	public double[][] getDistances() {
		return distances;
	}

	public void setDistances(double[][] distances) {
		this.distances = distances;
	}
	
	public double get(int i, int j) {
		return this.distances[i][j];
	}

}
