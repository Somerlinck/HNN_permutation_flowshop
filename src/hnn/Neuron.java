package hnn;

public class Neuron {
	private double u; // internal state
	private double v; // external state
	int x; // row
	int i; // column
	
	// default
	public Neuron(int i, int j, double v_init) {
		this.u = 0;
		this.v = v_init;
		this.x = i;
		this.i = j;
	}
	
	public Neuron(Neuron neuron) {
		// TODO Auto-generated constructor stub
		this.u = neuron.getU();
		this.v = neuron.getV();
		this.x = neuron.getRow();
		this.i = neuron.getCol();
	}

	public double getU() {
		return this.u;
	}
	
	public double getV() {
		return this.v;
	}
	
	public int getRow() {
		return this.x;
	}
	
	public int getCol() {
		return this.i;
	}
	
	public void update(int A, int B, int C, int D, double T, double delta, Network network, DistanceMatrix d) {
		int n = network.getLength();
		
		//	number of 1s per row
		int sum_a = 0;
		for(int j=0; j < n; j++) {
			if(j != this.i) {
				sum_a += network.get(this.x,j).getV();
			}
		}
		
		// number of 1s per column
		int sum_b = 0;
		for(int y = 0; y < n; y++) {
			sum_b += network.get(y, this.i).getV();
		}
		
		// number of 1s
		int sum_c = -n;
		for(int k = 0; k < n; k++) {
			for(int l = 0; l < n; l++) {
				sum_c += network.get(k, l).getV();
			}
		}
		
		// distance from neighbors
		double sum_d = 0;
		for(int y = 0; y < n; y++) {
			if(this.i == 0) {
				sum_d += d.get(this.x, y) * (network.get(y, this.i+1).getV() + network.get(y, n-1).getV());
			}
			else if(this.i == n-1) {
				sum_d += d.get(this.x, y) * (network.get(y, 0).getV() + network.get(y, this.i-1).getV());
			}
			else{
				sum_d += d.get(this.x, y) * (network.get(y, this.i+1).getV() + network.get(y, this.i-1).getV());
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
	
	public Neuron copy() {
		return new Neuron(this);
	}

}
