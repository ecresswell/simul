package org.housered.simul.actor;

import org.housered.simul.interfaces.Locatable;
import org.housered.simul.location.Position;

public class Person implements Locatable
{
    private Mood mood;

    @Override
    public Position getCurrentPosition()
    {
        return null;
    }
}
