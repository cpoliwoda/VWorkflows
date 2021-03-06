/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow.io;

import eu.mihosoft.vrl.workflow.ValueObject;
import eu.mihosoft.vrl.workflow.VisualizationRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class PersistentNode {

    private double x;
    private double y;
    private double width;
    private double height;
    private String title;
    private ValueObject valueObject;
    private VisualizationRequest vReq;
    private String id;
    private List<PersistentConnector> connectors;
    private Map<String,String> mainInputs = new HashMap<>();
    private Map<String,String> mainOutputs = new HashMap<>();

    public PersistentNode() {
    }

    public PersistentNode(String id, String title,
            double x, double y, double width, double height,
            ValueObject valueObject, VisualizationRequest vReq,
            List<PersistentConnector> connectors) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        this.valueObject = valueObject;
        this.vReq = vReq;
        this.id = id;
        this.connectors = WorkflowIO.listToSerializableList(connectors);
    }
    
    

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the valueObject
     */
    public ValueObject getValueObject() {
        return valueObject;
    }

    /**
     * @param valueObject the valueObject to set
     */
    public void setValueObject(ValueObject valueObject) {
        this.valueObject = valueObject;
    }

    /**
     * @return the vReq
     */
    public VisualizationRequest getVReq() {
        return vReq;
    }

    /**
     * @param vReq the vReq to set
     */
    public void setVReq(VisualizationRequest vReq) {
        this.vReq = vReq;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the inputTypes
     */
    public List<PersistentConnector> getConnectors() {
        return connectors;
    }

    /**
     * @param connectors the inputTypes to set
     */
    public void setConnectors(List<PersistentConnector> connectors) {
        
        for (PersistentConnector persistentConnector : connectors) {
            addConnector(persistentConnector);
        }
    }
    
        /**
     * @param connectors the inputTypes to set
     */
    public void addConnector(PersistentConnector connector ) {

//            connector.setNode(this);
            
            connectors.add(connector);
    }

    /**
     * @return the mainInputs
     */
    public Map<String,String> getMainInputs() {
        return mainInputs;
    }

    /**
     * @param mainInputs the mainInputs to set
     */
    public void setMainInputs(Map<String,String> mainInputs) {
        this.mainInputs = mainInputs;
    }

    /**
     * @return the mainOutputs
     */
    public Map<String,String> getMainOutputs() {
        return mainOutputs;
    }

    /**
     * @param mainOutputs the mainOutputs to set
     */
    public void setMainOutputs(Map<String,String> mainOutputs) {
        this.mainOutputs = mainOutputs;
    }
}
