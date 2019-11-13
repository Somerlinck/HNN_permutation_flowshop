package hnn;

public class Neuron {
	private double u; // internal state
	private double v; // external state
	int x; // row
	int i; // column
	
	// default
	public Neuron(int i, int j, double u_init) {
		this.u = u_init;
		this.v = Math.random();
//		this.u = 0;
//		this.v = u_init;
		this.x = i;
		this.i = j;
	}
	
	public double getU() {
		return this.u;
	}
	
	public double getV() {
		return this.v;
	}
	
	public void update(int A, int B, int C, int D, double T, double delta, Neuron[][] network, DistanceMatrix d) {
		int n = network.length;
		
		//	number of 1s per row
		int sum_a = 0;
		for(int j=0; j < n; j++) {
			if(j != this.i) {
				sum_a += network[this.x][j].getV();
			}
		}
		
		// number of 1s per column
		int sum_b = 0;
		for(int y = 0; y < n; y++) {
			sum_b += network[y][this.i].getV();
		}
		
		// number of 1s
		int sum_c = -n;
		for(int k = 0; k < n; k++) {
			for(int l = 0; l < n; l++) {
				sum_c += network[k][l].getV();
			}
		}
		
		// distance from neighbors
		int sum_d = 0;
		for(int y = 0; y < n; y++) {
			if(this.i == 0) {
				sum_d += d.get(this.x, y) * (network[y][this.i + 1].getV() + network[y][n - 1].getV());
			}
			else if(this.i == n-1) {
				sum_d += d.get(this.x, y) * (network[y][0].getV() + network[y][this.i - 1].getV());
			}
			else{
				sum_d += d.get(this.x, y) * (network[y][this.i + 1].getV() + network[y][this.i - 1].getV());
			}
		}
		
//		//	update u : continuous
//		this.u = this.getU() + delta * (-this.getU() -A * sum_a -B * sum_b -C * sum_c -D * sum_d);
//		//	update v : continuous
////		this.v = Math.round(0.5 * (1 + Math.tanh(this.getU()/T)));
//		this.v = 0.5 * (1 + Math.tanh(this.getU()/T));
		
		//	update u : discrete
		this.u = -A * sum_a -B * sum_b -C * sum_c -D * sum_d;
		//	update v : discrete
		this.v = (this.u >= 0.5)? 1.0 : 0.0;
	}

}
