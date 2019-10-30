package model;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Problem {

    private int nbJobs;
    private int nbMachines;
    private Job[] jobs;

    // default
    public Problem() {
        nbJobs = 0;
        nbMachines = 0;
        jobs = null;
    }

    // create a problem with an array of jobs and m machines
    public Problem(Job[] t, int m) {
        nbMachines = m;
        nbJobs = t.length;
        jobs = new Job[nbJobs];
        for (int i = 0; i < nbJobs; i++) {
            jobs[i] = t[i];
        }
    }

    // create a problem with a text file
    public Problem(String s) {
        try {
            Scanner scanner = new Scanner(new FileReader(s));

            // read the number pf jobs
            if (scanner.hasNextInt()) {
                nbJobs = scanner.nextInt();
            }

            // read the number of machines
            if (scanner.hasNextInt()) {
                nbMachines = scanner.nextInt();
            }

            jobs = new Job[nbJobs];
            int[] d = new int[nbMachines];
            int i = 0; // job index
            int j = 0; // task index
            while (scanner.hasNextInt()) {
                d[j] = scanner.nextInt();
//                System.out.println(j + " " + d[j]);
                if (j < nbMachines - 1) {
                    j++; // next task
                } else { // else we create the job and go to the following job
                    jobs[i] = new Job(i, d);
                    i++;
                    j = 0;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
            System.exit(2);
        }
    }

    public int getNbJobs() {
        return nbJobs;
    }

    public int getNbMachines() {
        return nbMachines;
    }

    public Job getJob(int i) {
        return jobs[i];
    }

    // create a list corresponding to the array of jobs
    public JobsList createJobsList() {
        JobsList l = new JobsList();
        for (int i = 0; i < nbJobs; i++) {
            l.addJob(jobs[i].clone());
        }
        return l;
    }


    public JobsList createNEHList(int m) { // get the NEH list
        JobsList res = new JobsList();
        for (Job job : jobs) res.addJob(job);
        res.NEH();

        JobsList partial = new JobsList();
        partial.addJob(res.pop(0));

        int size = res.nbJobs();
        for (int i = 0; i < size; i++) {
            Job job = res.pop(0);
            JobsList best = partial.clone();
            best.addJob(job, 0);
            for (int j = 0; j < i + 1; j++) {
                JobsList test = partial.clone();
                test.addJob(job, j + 1);
                if (new Solution(test, m).getMakespan() < new Solution(best, m).getMakespan()) best = test;
            }
            partial = best;
        }

        return partial;
    }

    // compute r_kj
    public int computeAvailableTime(int k, int j) {
        if (k == 0) return 0;
        int r = 0;
        for (int i = 0; i < k; i++) r += this.getJob(j).getProcessingTime(i);
        return r;
    }

    // compute q_kj
    public int computeDelay(int k, int j) {
        if (k == nbMachines - 1) return 0;
        int q = 0;
        for (int i = k + 1; i < getNbMachines(); i++) q += getJob(j).getProcessingTime(i);
        return q;
    }

    // compute the sum of processing times executed on the machine k
    public int computeTotalProcessingTime(int k) {
        int sum = 0;
        for (int j = 0; j < getNbJobs(); j++) sum += getJob(j).getProcessingTime(k);
        return sum;
    }

    public int LowerBound(JobsList lJobs) {
        int max = LowerBound(0, lJobs);
        for (int k = 1; k < nbMachines; k++) max = Math.max(LowerBound(k, lJobs), max);
        return max;
    }

    public int LowerBound(int k, JobsList lJobs) {
        int res = 0;

        int min = Integer.MAX_VALUE;
        for (Job job : lJobs) min = Math.min(computeAvailableTime(k, job.getIndex()), min);

        res += min;

        min = Integer.MAX_VALUE;
        for (Job job : lJobs) min = Math.min(computeDelay(k, job.getIndex()), min);

        return res + min + computeTotalProcessingTime(k);
    }

    // compute r_kj considering a schedule in progresss
    public int computeAvailableTime(Solution o, int k, int j) {
        if (k == 0) return o.getCompletionTime(0);
        int pred = computeAvailableTime(o, k - 1, j) + getJob(j).getProcessingTime(k - 1);
        return Math.max(o.getCompletionTime(k), pred);
    }

    // compute the sum of the processing times of the tasks from a list
    // processed on the machine k
    public int computeTotalProcessingTime(int k, JobsList l) {
        int sum = 0;
        for (Job job : l) sum += job.getProcessingTime(k);
        return sum;
    }

    public int LowerBound(Solution o, JobsList lJobs) {
        int max = LowerBound(o, 0, lJobs);
        for (int k = 1; k < nbMachines; k++) max = Math.max(LowerBound(o, k, lJobs), max);
        return max;
    }

    public int LowerBound(Solution o, int k, JobsList lJobs) {
        int res = 0;

        int min = Integer.MAX_VALUE;
        for (Job job : lJobs) min = Math.min(computeAvailableTime(o, k, job.getIndex()), min);
        res += min;

        min = Integer.MAX_VALUE;
        for (Job job : lJobs) min = Math.min(computeDelay(k, job.getIndex()), min);

        return res + min + computeTotalProcessingTime(k, lJobs);
    }

    // branch & bound
    public void branchAndBound() {
        int compteur = 0;
        int cmax = Integer.MAX_VALUE;
        Solution bestSchedule = null;

        NodesPriorityQueue fp = new NodesPriorityQueue();
        int bInf = LowerBound(createJobsList());
        System.out.println("LB : " + bInf);
        fp.addNode(new Node(new Solution(nbMachines), createJobsList(), bInf, compteur++));

        while (!fp.isEmpty()) {
            Node s = fp.poll();
            for (Job nonPlace : s.getUnscheduled()) {
                Solution o = s.getSchedule().clone();
                o.addJob(nonPlace);

                JobsList nonPlaces = new JobsList();
                for (Job nonPlace2 : s.getUnscheduled()) {
                    if (nonPlace2.equals(nonPlace)) continue;
                    nonPlaces.addJob(nonPlace2);
                }

                if (nonPlaces.nbJobs() == 0) {
                    if (cmax > o.getMakespan()) {
                        cmax = o.getMakespan();
                        bestSchedule = o;
                        System.out.println("New cmax : " + cmax);
                    }
                } else {
                    bInf = LowerBound(o, nonPlaces);
                    if (bInf < cmax) fp.addNode(new Node(o, nonPlaces, bInf, compteur++));
                }
            }
        }

        bestSchedule.display();
    }

    public Solution generateRandomSolution() {
        Solution random = new Solution(nbMachines);
        ArrayList<Job> shuffle = new ArrayList<>(Arrays.asList(jobs));
        Collections.shuffle(shuffle);
        shuffle.forEach(random::addJob);
        random.getMakespan();
        return random;
    }
    
    public Job[] getJobs() {
    	Job[] res = new Job[this.nbJobs];
    	for(int i = 0; i < this.nbJobs; i++) {
    		res[this.jobs[i].getIndex()] = this.jobs[i];
    	}
    	return res;
    }
}