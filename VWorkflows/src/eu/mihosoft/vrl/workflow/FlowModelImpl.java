/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow;

import com.sun.javafx.collections.UnmodifiableObservableMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FlowModelImpl implements FlowModel {

    private final ObservableMap<String, Connections> connections =
            FXCollections.observableHashMap();
    private final UnmodifiableObservableMap<String, Connections> readOnlyObservableConnections =
            (UnmodifiableObservableMap<String, Connections>) FXCollections.unmodifiableObservableMap(connections);
    private final ObservableList<FlowNode> observableNodes =
            FXCollections.observableArrayList();
    private final ObservableList<FlowNode> readOnlyObservableNodes =
            FXCollections.unmodifiableObservableList(observableNodes);
    private final Map<String, FlowNode> nodes = new HashMap<>();
    private Class<? extends FlowNode> flowNodeClass = FlowNodeBase.class;
    private final BooleanProperty visibleProperty = new SimpleBooleanProperty();

    @Override
    public BooleanProperty visibleProperty() {
        return visibleProperty;
    }

    @Override
    public void setVisible(boolean b) {
        visibleProperty.set(b);
    }

    @Override
    public boolean isVisible() {
        return visibleProperty.get();
    }

    // TODO duplicated code
    private static String connectionId(String id, String s, String r) {
        return "id=" + id + ";[" + s + "]->[" + r + "]";
    }

    // TODO duplicated code
    private static String connectionId(Connection c) {
        return connectionId(c.getId(), c.getSenderId(), c.getReceiverId());
    }

    @Override
    public ConnectionResult tryConnect(FlowNode s, FlowNode r, String type) {
        
        CompatibilityResult result = r.getValueObject().
                compatible(s.getValueObject(), type);

        return new ConnectionResultImpl(result, null);
    }

    @Override
    public ConnectionResult connect(FlowNode s, FlowNode r, String type) {

        ConnectionResult result = tryConnect(s, r, type);

        if (!result.getStatus().isCompatible()) {
            return result;
        }

//        nodes.put(s.getId(), s);
//        nodes.put(r.getId(), r);

//        observableNodes.add(s);
//        observableNodes.add(r);

        Connection connection = getConnections(type).add(s.getId(), r.getId());

//        if (connection != null) {
//            createConnectionSkin(connection, type);
//        }

        return new ConnectionResultImpl(result.getStatus(), connection);
    }

    @Override
    public ObservableList<FlowNode> getNodes() {
        return readOnlyObservableNodes;
    }

    @Override
    public void clear() {
        List<FlowNode> delList = new ArrayList<>(observableNodes);

        for (FlowNode n : delList) {
            remove(n);
        }
    }

    @Override
    public FlowNode remove(FlowNode n) {

//        if (n instanceof FlowModel) {
//            ((FlowModel)n).clear();
//        }

        FlowNode result = nodes.remove(n.getId());
        observableNodes.remove(n);

//        removeNodeSkin(n);

        for (Connections cns : getAllConnections().values()) {

            Collection<Connection> connectionsToRemove =
                    cns.getAllWith(n.getId());

            for (Connection c : connectionsToRemove) {
                cns.remove(c);
//                removeConnectionSkin(c);
            }
        }

        return result;
    }

    @Override
    public UnmodifiableObservableMap<String, Connections> getAllConnections() {
        return readOnlyObservableConnections;
    }

    //TODO unmodifiable connection object?
    @Override
    public Connections getConnections(String type) {
        return connections.get(type);
    }

    @Override
    public FlowNode getSender(Connection c) {
        return nodes.get(c.getSenderId());
    }

    @Override
    public FlowNode getReceiver(Connection c) {
        return nodes.get(c.getReceiverId());
    }

    @Override
    public void setFlowNodeClass(Class<? extends FlowNode> cls) {
        try {
            Constructor constructor = cls.getConstructor(FlowModel.class);
            throw new IllegalArgumentException("constructor missing: (String, String)");
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ConnectionsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.flowNodeClass = cls;
    }

    @Override
    public Class<? extends FlowNode> getFlowNodeClass() {
        return flowNodeClass;
    }

    

     FlowNode newNode(FlowNode result, ValueObject obj) {

        result.setValueObject(obj);

        // search id:
        String id = "0";
        int count = 0;

        while (nodes.containsKey(id)) {
            count++;
            id = "" + count;
        }

        result.setId(id);

        nodes.put(id, result);
        observableNodes.add(result);

//                createNodeSkin(result);


        return result;
    }

    

    @Override
    public void addConnections(Connections connections, String flowType) {
        this.connections.put(flowType, connections);
    }

    @Override
    public VisualizationRequest getVisualizationRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVisualizationRequest(VisualizationRequest vReq) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}