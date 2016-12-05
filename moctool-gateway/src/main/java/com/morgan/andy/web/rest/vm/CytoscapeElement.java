package com.morgan.andy.web.rest.vm;

import java.util.HashMap;
import java.util.Map;

public class CytoscapeElement {
        private String classes;
        private Map<String, String> data;
        private boolean grabbable = true;
        private String group;
        private boolean locked;
        private Map<String, Integer> position;
        private boolean removed;
        private boolean selectable = true;
        private boolean selected;

        public String getClasses() {
            return classes;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }

        public Map<String, String> getData() {
            return data;
        }

        public void setData(Map<String, String> data) {
            this.data = data;
        }

        public void addDataElement(String key, String value) {
            if(data == null) {
                data = new HashMap<>();
            }
            data.put(key, value);
        }

        public boolean isGrabbable() {
            return grabbable;
        }

        public void setGrabbable(boolean grabbable) {
            this.grabbable = grabbable;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public boolean isLocked() {
            return locked;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }

        public Map<String, Integer> getPosition() {
            return position;
        }

        public void setPosition(Map<String, Integer> position) {
            this.position = position;
        }

        public boolean isRemoved() {
            return removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }

        public boolean isSelectable() {
            return selectable;
        }

        public void setSelectable(boolean selectable) {
            this.selectable = selectable;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
}
