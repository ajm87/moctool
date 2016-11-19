package com.morgan.andy.web.rest.vm;

public class ModelVM {

    private State state;
    private Transition transition;

    public ModelVM(State state, Transition transition) {
        this.state = state;
        this.transition = transition;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    @Override
    public String toString() {
        return "ModelVM{" +
            "state=" + state +
            ", transition=" + transition +
            '}';
    }

    public class State {
        private String src;
        private int top;
        private int left;
        private String id;

        public State(String src, int top, int left, String id) {
            this.src = src;
            this.top = top;
            this.left = left;
            this.id = id;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
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
            return "State{" +
                "src='" + src + '\'' +
                ", top=" + top +
                ", left=" + left +
                ", id='" + id + '\'' +
                '}';
        }
    }

    public class Transition {
        private String id;
        private String sourceId;
        private String targetId;
        private String label;

        public Transition(String id, String sourceId, String targetId, String label) {
            this.id = id;
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.label = label;
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
            return "Transition{" +
                "id='" + id + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", label='" + label + '\'' +
                '}';
        }
    }

}
