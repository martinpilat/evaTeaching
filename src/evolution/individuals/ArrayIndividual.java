package org.pikater.core.utilities.evolution.individuals;

import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;

/**
 * Represents all individual which acts as arrays of values. 
 *
 * @author Martin Pilat
 */
public abstract class ArrayIndividual extends Individual {

    /**
     * Gets the value at index n.
     * 
     * @param n the index of the value.
     * @return The value at index n.
     */
    public abstract Object get(int n);

    /**
     * Sets the value on the index n
     * @param n the index on which the value shall be set
     * @param o the value which shall be set
     */
    public abstract void set(int n, IValueData o);

    /**
     * Returns the length of the individual.
     * @return The length of the individual.
     */
    public abstract int length();
    
    @Override
    public ArrayIndividual clone()
    {
    	return (ArrayIndividual) super.clone();
    }
}