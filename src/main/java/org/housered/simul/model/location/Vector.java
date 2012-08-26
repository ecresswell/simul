package org.housered.simul.model.location;

public interface Vector
{
    float getX();
    
    float getY();
    
    void setX(float x);
    
    void setY(float y);
    
    Vector subtract(Vector v);
}
