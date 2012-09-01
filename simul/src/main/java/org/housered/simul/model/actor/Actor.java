package org.housered.simul.model.actor;

import org.housered.simul.model.assets.Occupant;
import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.work.Employee;
import org.housered.simul.model.world.Identifiable;

public interface Actor extends Identifiable, Locatable, Occupant, Employee
{
}
