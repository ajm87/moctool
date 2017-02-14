package com.morgan.andy.moc.simulation;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {
    private static final AtomicInteger ID = new AtomicInteger(0);

    private ArrayList<Step> steps;
    private final int id;
    private SimulationState finalState = SimulationState.INCOMPLETE;

    public static AtomicInteger getID() {
        return ID;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public int getId() {
        return id;
    }


    public Simulation(ArrayList<Step> steps) {
        this.steps = steps;
        id = ID.getAndIncrement();
    }

    public Simulation() {
        id = ID.getAndIncrement();
    }

    public SimulationState getFinalState() {
        return finalState;
    }

    void setFinalState(SimulationState finalState) {
        this.finalState = finalState;
    }

    public enum SimulationState {
        ACCEPT, REJECT, INCOMPLETE
    }

}
