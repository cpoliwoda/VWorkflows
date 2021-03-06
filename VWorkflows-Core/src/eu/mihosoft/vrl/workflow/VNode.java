/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow;

import java.util.Collection;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public interface VNode extends Model {

    public StringProperty titleProperty();

    public void setTitle(String title);

    public String getTitle();

    public StringProperty idProperty();

    /**
     * Defines the local id of this node.
     *
     * @param id id to set
     */
    public void setId(String id);

    /**
     * Returns the local id of this node.
     *
     * @return
     */
    public String getId();

    /**
     * Returns the global id of this node
     *
     * @return global id of this node
     */
//    public String getGlobalId();
    public DoubleProperty xProperty();

    public DoubleProperty yProperty();

    public void setX(double x);

    public void setY(double x);

    public double getX();

    public double getY();

    public DoubleProperty widthProperty();

    public DoubleProperty heightProperty();

    public void setWidth(double w);

    public void setHeight(double h);

    public double getWidth();

    public double getHeight();

//    public ObservableList<VNode> getChildren();
//    public ObservableList<Connector<FlowNode>> getInputs();
//    public ObservableList<Connector<FlowNode>> getOutputs();
    public void setValueObject(ValueObject obj);

    public ValueObject getValueObject();

    public ObjectProperty<ValueObject> valueObjectProperty();

    public VFlowModel getFlow();

//    boolean isInputOfType(String type);
//
//    boolean isOutputOfType(String type);
//    boolean isInput();
//
//    boolean isOutput();
//    void setInput(boolean state, String type);
//    void setOutput(boolean state, String type);
    public Connector addInput(String type);

    public Connector addOutput(String type);

    public Connector addConnector(Connector c);

//    ObservableList<String> getInputTypes();
//
//    ObservableList<String> getOutputTypes();
    public Collection<String> getMainInputTypes();

    public Collection<String> getMainOutputTypes();

    public Connector getMainInput(String type);

    public Connector getMainOutput(String type);

    public void setMainInput(Connector connector);

    public void setMainOutput(Connector connector);

    public Connector getConnector(String localId);

    public ObservableList<Connector> getConnectors();

    public ObservableList<Connector> getInputs();

    public ObservableList<Connector> getOutputs();
}
