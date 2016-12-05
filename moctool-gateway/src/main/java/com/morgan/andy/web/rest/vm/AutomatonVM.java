package com.morgan.andy.web.rest.vm;

import java.util.ArrayList;

public class AutomatonVM {

    private ArrayList<CytoscapeElement> elements;

    public ArrayList<CytoscapeElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<CytoscapeElement> elements) {
        this.elements = elements;
    }

    public void addElement(CytoscapeElement element) {
        if(elements == null) {
            elements = new ArrayList<>();
        }
        elements.add(element);
    }

}
