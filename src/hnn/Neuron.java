package hnn;

public class Neuron {
	private float u; // internal state
	private int v; // external state
	
	// default
	public Neuron() {
		this.u = 0;
		this.v = 0;
	}
	
	public float getU() {
		return this.u;
	}
	
	public int getV() {
		return this.v;
	}

}
