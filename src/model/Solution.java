

public class Solution implements Cloneable, Comparable {
    private JobsList schedule;
    private int nbMachines;            	  // number of machines
    private int makespan;                // makespan totale
    private int[] completionTimes;		// completion time on each machine

    public static void main(String[] args) {
        JobsList l = new JobsList();
        l.addJob(new Job(0, new int[]{1, 2, 3}));
        l.addJob(new Job(1, new int[]{1, 2, 3}));
        l.addJob(new Job(2, new int[]{1, 3, 3}));
        new Solution(l, 3).display();
    }

    // default
    public Solution() {
        schedule = new JobsList();
        nbMachines = 0;
        completionTimes = new int[0];
    }

    // create an empty schedule on m machines
    public Solution(int m) {
        schedule = new JobsList();
        nbMachines = m;
        completionTimes = new int[nbMachines];
        for (int i = 0; i < nbMachines; i++) {
            completionTimes[i] = 0; // machines available at t=0
        }
    }

    // creat a schedule with a list of jobs on m machines
    // the jobs are processed following the order of the list
    public Solution(JobsList l, int m) {
        schedule = new JobsList();
        nbMachines = m;
        completionTimes = new int[nbMachines];
        schedule(l);
    }

    public int getMakespan() {
        calculateMakespan();
        return makespan;
    }

    public JobsList getSchedule() {
        return schedule;
    }

    public int getCompletionTime(int i) {
        return completionTimes[i];
    }

    public void addJob(Job j) {
        schedule.addJob(j);
    }

    public void initialize() { // initialize the schedule
        for (Job j : schedule) for (int i = 0; i < nbMachines; i++) j.setStartingTime(i, -1); // non processed tasks
        for (int i = 0; i < nbMachines; i++) completionTimes[i] = 0; // machines available at t=0
    }

    public void reset() {
        initialize();
        JobsList copy = getSchedule().clone();
        schedule.clear();
        schedule(copy);
        getScore();
    }

    public void display() { // display the schedule
        schedule.display();
        for (Job j : schedule) {
            System.out.print("Job " + j.getIndex() + " : ");
            for (int i = 0; i < nbMachines; i++) {
                System.out.print("(op " + i + " à t = "
                        + j.getStartingTime(i) + ") ");
            }
            System.out.println();
        }
        System.out.println("Cmax = " + getMakespan());
    }

    public Solution clone() {
        Solution o = null;
        try {
            o = (Solution) super.clone();
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        // copy of the Jobs list
        o.schedule = schedule.clone();
        // copy of the completion time array
        o.completionTimes = completionTimes.clone();
        return o;
    }


    public void scheduleJob(Job j) { // schedule a job depending on what has already been schedule
        addJob(j);
        j.setStartingTime(0, completionTimes[0]);
        completionTimes[0] += j.getProcessingTime(0);
        for (int i = 1; i < j.getNbTasks(); i++) {
            int timeMin = Math.max(getCompletionTime(i - 1), getCompletionTime(i));
            j.setStartingTime(i, timeMin);
            completionTimes[i] = timeMin + j.getProcessingTime(i);
        }
    }

    private void calculateMakespan() {
        makespan = 0;
        for (int i = 0; i < completionTimes.length; i++)
            if (completionTimes[i] > makespan) makespan = completionTimes[i];
    }

    public void schedule(JobsList l) { // ordonnance les jobs de la liste
        l.forEach(this::scheduleJob);
    }

    public float getScore() {
        return getMakespan();
    }

    public int getNbMachines() {
        return nbMachines;
    }

    @Override
    public int compareTo(Object o) {
        Solution other = (Solution) o;
        if (other.getScore() == getScore()) return 0;
        return other.getScore() < getScore() ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Solution)) return false;

        Solution other = (Solution) o;

        return other.getSchedule().equals(getSchedule());
    }

    @Override
    public int hashCode() {
        int res = 0;
        int index = 1;
        for (Job job : schedule) res += job.getIndex() * index++;
        return res;
    }

}