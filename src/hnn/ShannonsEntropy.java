package hnn;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShannonsEntropy {

    private float threshold;

    private HashMap<Network, Float> observations = new HashMap<>();
    private int size;
    private float lastEntropy = -1;

    public ShannonsEntropy() {
        this(0.01f);
    }

    public ShannonsEntropy(float threshold) {
        this.threshold = threshold;
    }

    public boolean hasConverged(Network network) {
        boolean test = true;
        Iterator<Map.Entry<Network,Float>> iterator = observations.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Network, Float> entry = iterator.next();
            Network s2 = entry.getKey();
            if (network.equals(s2)) {
                test = false;
                float p = entry.getValue();
                observations.put(s2, p++);
                break;
            }
        }
        if (test) observations.put(network, 1.0f);
        size++;
        
        float entropy = 0;
        for (float value : observations.values()) {
            entropy -= 10 * value/size * Math.log(value/size);
        }
        if (lastEntropy == -1) {
            lastEntropy = entropy;
            return false;
        }
        
        test = lastEntropy / entropy - 1 > 0 && lastEntropy / entropy - 1 < threshold;
        lastEntropy = entropy;
        
        return test;
    }
    
    public int getSize() {
    	return this.size;
    }

}
