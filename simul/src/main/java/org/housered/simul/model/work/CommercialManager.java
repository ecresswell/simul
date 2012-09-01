package org.housered.simul.model.work;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommercialManager
{
    private static final Set<CommercialBuilding> NO_JOB = Collections
            .unmodifiableSet(new HashSet<CommercialBuilding>());
    private Map<CommercialBuilding, Set<Employee>> contracts = new HashMap<CommercialBuilding, Set<Employee>>();
    private Map<Employee, Set<CommercialBuilding>> inverseContracts = new HashMap<Employee, Set<CommercialBuilding>>();

    public void createContract(Employee employee, CommercialBuilding placeOfWork)
    {
        if (contracts.get(placeOfWork) == null)
            contracts.put(placeOfWork, new HashSet<Employee>());
        contracts.get(placeOfWork).add(employee);

        if (inverseContracts.get(employee) == null)
            inverseContracts.put(employee, new HashSet<CommercialBuilding>());
        inverseContracts.get(employee).add(placeOfWork);
    }

    public Set<CommercialBuilding> getPlacesOfWork(Employee employee)
    {
        Set<CommercialBuilding> result = inverseContracts.get(employee);
        if (result == null)
            return NO_JOB;
        return result;
    }
}
