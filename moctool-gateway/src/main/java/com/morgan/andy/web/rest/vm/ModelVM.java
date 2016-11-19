package com.morgan.andy.web.rest.vm;

import java.util.Arrays;

public class ModelVM {

    private StateVM[] stateVMs;
    private TransitionVM[] transitionVMs;
    private StateVM startStateVM;
    private String[] alphabet;

    public ModelVM(StateVM[] stateVMs, TransitionVM[] transitionVMs, StateVM startStateVM, String[] alphabet) {
        this.stateVMs = stateVMs;
        this.transitionVMs = transitionVMs;
        this.startStateVM = startStateVM;
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

    public StateVM getStartStateVM() {
        return startStateVM;
    }

    public void setStartStateVM(StateVM startStateVM) {
        this.startStateVM = startStateVM;
    }

    public String[] getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String[] alphabet) {
        this.alphabet = alphabet;
    }


    public static class StateVM {
        private String src;
        private String top;
        private String left;
        private String id;

        public StateVM(String id, String top, String left, String src) {
            this.src = src;
            this.top = top;
            this.left = left;
            this.id = id;
        }

        public StateVM() {

        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
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
                "src='" + src + '\'' +
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
