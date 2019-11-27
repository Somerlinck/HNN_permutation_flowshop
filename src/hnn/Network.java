package hnn;

public class Network {
	private Neuron[][] network;
	private double energy;
	private int length;
	
	public Network(Neuron[][] n) {
		this.setNetwork(n);
		this.energy = 0;
		this.length = n.length;
	}
	
	public void setEnergy(double e) {
		this.energy = e;
	}
	
	public double getEnergy() {
		return this.energy;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
        if (!(o instanceof Network)) return false;

        Network other = (Network) o;
        
        return other.getEnergy() == this.getEnergy();
	}

	public Neuron[][] getNetwork() {
		return network;
	}

	public void setNetwork(Neuron[][] network) {
		this.network = network;
	}
	
	public Neuron get(int i, int j) {
		return this.network[i][j];
	}
	
	public int getLength() {
		return this.network.length;
	}
	
	public Network copy() {
		Neuron[][] copy = new Neuron[this.length][this.length];
		for(int i = 0; i < this.length; i++) {
			for(int j = 0; j < this.length; j++) {
				copy[i][j] = this.network[i][j].copy();
			}
		}
		Network res = new Network(copy);
		res.setEnergy(this.getEnergy());
		return res;
	}
	
	public boolean isSolution() {
		// rows
		for(int i = 0; i < this.length; i++) {
			int sum = 0;
			for(int j = 0; j < this.length; j++) {
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
		for(int i = 0; i < this.length; i++) {
			int sum = 0;
			for(int j = 0; j < this.length; j++) {
				sum += this.network[i][j].getV();
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
}
