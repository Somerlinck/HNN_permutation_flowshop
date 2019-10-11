

public class Job implements Comparable<Job>, Cloneable {
    // "implements java.lang.Comparable" for NEH
    // "Cloneable" to do copies

    private int index;                    // job index
    private int nbTasks;				// number of tasks
    private int[] processingTimes;		// processing time of the tasks
    private int length;					// sum of the processing times
    private int[] startingTimes;       	// starting time on each machine

    // default
    public Job() {
        index = 0;
        nbTasks = 0;
        processingTimes = null;
        startingTimes = null;
    }

    // create a job of indew n with an array containing the processing times
    public Job(int n, int[] d) {
        index = n;
        nbTasks = d.length;
        processingTimes = new int[nbTasks];
        startingTimes = new int[nbTasks];

        for (int i = 0; i < nbTasks; i++) {
            processingTimes[i] = d[i];
            startingTimes[i] = -1;
        }
    }

    public int getIndex() {
        return index;
    }

    public int getNbTasks() {
        return nbTasks;
    }

    public int getProcessingTime(int i) {
        return processingTimes[i];
    }

    public void display() {
        System.out.println("Job " + index + ", total processing time: " + getlength());
        System.out.println("Prossessing time of the tasks : ");
        for (int i = 0; i < nbTasks; i++) {
            System.out.print("(task " + i + " : " + processingTimes[i] + ") ");
        }
        System.out.println();
        System.out.println("Starting time of the tasks  : ");
        for (int i = 0; i < nbTasks; i++) {
            System.out.print("(task " + i + " : " + startingTimes[i] + ") ");
        }
        System.out.println();
    }

    public int getStartingTime(int i) {
        return startingTimes[i];
    }

    public void setStartingTime(int i, int t) {
        startingTimes[i] = t;
    }

    public int getLength() {
        calculateLength();
        return length;
    }

    public Job clone() {
        Job j = null;
        try {
            j = (Job) super.clone();
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        // copie des tableaux : nécessaire !
        j.processingTimes = processingTimes.clone();
        j.startingTimes = startingTimes.clone();
        return j;
    }

    /* we need a relation of comparison between the jobs for the functions
     * "sort()" et "reverseOrder()".
     * we are using the length
     */

    public int compareTo(Job obj) {
        int number1 = obj.getlength();
        int number2 = getlength();
        if (number1 > number2) {
            return -1;
        } else if (number1 == number2) {
            return 0;
        } else {
            return 1;
        }
    }

    /*
     * calculate the total length of the job
     */
    private void calculateLength() {
        length = 0;
        for (int i = 0; i < this.nbTasks; i++) length += getProcessingTime(i);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Job)) return false;
        return ((Job) o).getindex() == getindex();
    }

}