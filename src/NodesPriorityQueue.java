

import java.util.*;

public class NodesPriorityQueue implements Iterable<Node> {
    private PriorityQueue<Node> file;
    
    // default
    public NodesPriorityQueue() {
    	file = new PriorityQueue<Node>();
    }
    
    public Node peek() {
    	return file.peek();
    }
    
    public Node poll() {
    	return file.poll();
    }
    
    public int size() {
    	return file.size();
    }
    
    public void addNode(Node s) {
    	file.add(s);
    }
    
    public boolean isEmpty() {
    	if (file.size() == 0) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    // pour utiliser le "foreach"
    public Iterator<Node> iterator() {
        Iterator<Node> iSommet = file.iterator();
        return iSommet;
    }
}