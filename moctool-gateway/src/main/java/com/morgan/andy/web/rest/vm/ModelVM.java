package com.morgan.andy.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class ModelVM {

    private StateVM[] stateVMs;
    private TransitionVM[] transitionVMs;
    private String[] alphabet;

    public ModelVM(StateVM[] stateVMs, TransitionVM[] transitionVMs, String[] alphabet) {
        this.stateVMs = stateVMs;
        this.transitionVMs = transitionVMs;
        this.alphabet = alphabet;
    }

    public ModelVM() {

    }

    public StateVM[] getStateVMs() {
        return stateVMs;
    }

    public void setStateVMs(StateVM[] stateVMs) {
        this.stateVMs = stateVMs;
    }

    public TransitionVM[] getTransitionVMs() {
        return transitionVMs;
    }

    public void setTransitionVMs(TransitionVM[] transitionVMs) {
        this.transitionVMs = transitionVMs;
    }

    @Override
    public String toString() {
        return "ModelVM{" +
            "stateVMs=" + Arrays.toString(stateVMs) +
            ", transitionVMs=" + Arrays.toString(transitionVMs) +
            '}';
    }

    public String[] getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String[] alphabet) {
        this.alphabet = alphabet;
    }


    public static class StateVM {
        private String stateName;
        private String top;
        private String left;
        private String id;
        private boolean isStart;
        private boolean isFinal;

        public boolean isStart() {
            return isStart;
        }

        public void setStart(boolean isStart) {
            this.isStart = isStart;
        }

        public boolean isFinal() {
            return isFinal;
        }

        public void setFinal(boolean isFinal) {
            this.isFinal = isFinal;
        }

        public StateVM(String id, String top, String left, String stateName, boolean isStart, boolean isFinal) {
            this.stateName = stateName;
            this.top = top;
            this.left = left;
            this.id = id;
            this.isStart = isStart;
            this.isFinal = isFinal;
        }

        public StateVM() {

        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getTop() {
            return top;
        }

        public void setTop(String top) {
            this.top = top;
        }

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "StateVM{" +
                "stateName='" + stateName + '\'' +
                ", top=" + top +
                ", left=" + left +
                ", id='" + id + '\'' +
                '}';
        }
    }

    public static class TransitionVM {
        private String id;
        private String sourceId;
        private String targetId;
        private String label;

        public TransitionVM(String id, String sourceId, String targetId, String label) {
            this.id = id;
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.label = label;
        }

        public TransitionVM() {

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "TransitionVM{" +
                "id='" + id + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", label='" + label + '\'' +
                '}';
        }
    }

}
