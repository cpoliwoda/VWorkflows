/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow;

import javafx.beans.property.ObjectProperty;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class DefaultValueObject implements ValueObject {

    private transient VNode parent;

    public DefaultValueObject() {
    }

    public DefaultValueObject(VNode parent) {
        this.parent = parent;
    }

    @Override
    public VNode getParent() {
        return parent;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ObjectProperty<Object> valueProperty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CompatibilityResult compatible(final ValueObject sender, final String flowType) {
        return new CompatibilityResult() {
            @Override
            public boolean isCompatible() {
                boolean differentObjects = sender != DefaultValueObject.this;
//                boolean compatibleType = getParent().isInputOfType(flowType)
//                        && sender.getParent().isOutputOfType(flowType);

                return differentObjects /*&& compatibleType*/;
            }

            @Override
            public String getMessage() {
                return "incompatible: " + sender.getParent().getId()  + " -> " +  getParent().getId();
            }

            @Override
            public String getStatus() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    @Override
    public VisualizationRequest getVisualizationRequest() {
        return new VisualizationRequest() {
            @Override
            public String getStyle() {
                return "default";
            }

            @Override
            public String getOptions() {
                return "";
            }
        };
    }

    /**
     * @param parent the parent to set
     */
    @Override
    public void setParent(VNode parent) {
        this.parent = parent;
    }
}