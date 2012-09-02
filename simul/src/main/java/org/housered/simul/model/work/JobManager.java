package org.housered.simul.model.work;

import static java.util.Collections.unmodifiableSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JobManager
{
    private static final Set<Job> NO_JOB = unmodifiableSet(new HashSet<Job>());
    private Map<Employee, Set<Job>> jobs = new HashMap<Employee, Set<Job>>();

    public void createContract(Employee employee, JobDefinition jobDefinition)
    {
        if (jobs.get(employee) == null)
            jobs.put(employee, new HashSet<Job>());

        jobs.get(employee).add(jobDefinition.createJob());
    }

    public Set<Job> getJobs(Employee employee)
    {
        Set<Job> result = jobs.get(employee);
        if (result == null)
            return NO_JOB;
        return result;
    }
}
