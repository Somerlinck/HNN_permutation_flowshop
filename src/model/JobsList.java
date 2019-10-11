import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class JobsList implements Cloneable, Iterable<Job> {
    private List<Job> list;

    // default
    public JobsList() {
        list = new ArrayList<Job>();        // empty list
    }

    public Job getJob(int i) {
        return list.get(i);
    }

    public Job pop(int i) {
        Job res = getJob(i);
        removeJob(i);
        return res;
    }

    public void addJob(Job j) {
//        if(list.contains(j)) throw new Error("Job already in job list!");
        list.add(j);
    }

    public void addJob(Job j, int i) {        // ajoute à la position i
//        if(list.contains(j)) throw new Error("Job already in job list!");
        list.add(i, j);
    }

    public void removeJob(Job j) {
        list.remove(j);
    }

    public void removeJob(int i) {
        list.remove(i);
    }

    public void clear() {
        list.clear();
    }

    public int nbJobs() {
        return list.size();
    }

    public int position(Job j) {
        return list.indexOf(j);
    }

    public void display() {            // print the list of jobs
        System.out.print("( ");
        for (Job j : list) {
            System.out.print(j.getIndex() + " ");
        }
        System.out.println(")");
    }

    public void NEH() { // return a list sorted acording to the NEH criteria
        Collections.sort(list, Collections.reverseOrder()); // we sort by decreasing length
    }

    // to use "foreach"
    public Iterator<Job> iterator() {
        Iterator<Job> iJob = list.iterator();
        return iJob;
    }

    // create a copy
    public JobsList clone() {
        JobsList l = null;
        try {
            l = (JobsList) super.clone();
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        l.list = new ArrayList<Job>();
        for (Job j : list) {
            l.list.add(j.clone());
        }
        return l;
    }

    public Boolean contains(Job j) {
        return this.list.contains(j);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof JobsList)) return false;
        JobsList other = (JobsList) o;
        for (int i = 0; i < nbJobs(); i++) if (!getJob(i).equals(other.getJob(i))) return false;
        return true;
    }

}