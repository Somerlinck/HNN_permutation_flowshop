

public class Node implements Comparable<Node> {

    private Solution schedule; // schedulennancement en cours
    private JobsList unscheduled; // liste des jobs non placés
    private int bInf; // inferior boundary
    private int index; // index of the node

    // default
    public Node() {
        schedule = new Solution();
        unscheduled = new JobsList();
        bInf = Integer.MIN_VALUE;
        index = 0;
    }

    public Node(Solution o, JobsList l, int b, int n) {
        schedule = o.clone();
        unscheduled = l.clone();
        bInf = b;
        index = n;
    }

    public Solution getSchedule() {
        return schedule;
    }

    public JobsList getUnscheduled() {
        return unscheduled;
    }

    public int getBInf() {
        return bInf;
    }

    public int getindex() {
        return index;
    }

    public void display() {
        System.out.println("Sommet " + index);
        //schedule.display();
        //for (Job j : list)
        //    j.display();
        System.out.println("Borne inf = " + bInf);
    }

    public int compareTo(Node obj) {
        int nb1 = obj.getBInf();
        int nb2 = bInf;
        if (nb1 > nb2) {
            return -1;
        } else if (nb1 == nb2) {
            return 0;
        } else {
            return 1;
        }
    }
}