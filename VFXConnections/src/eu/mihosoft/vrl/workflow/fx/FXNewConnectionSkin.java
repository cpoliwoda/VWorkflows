/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow.fx;

import eu.mihosoft.vrl.workflow.Connection;
import eu.mihosoft.vrl.workflow.ConnectionResult;
import eu.mihosoft.vrl.workflow.ConnectionSkin;
import eu.mihosoft.vrl.workflow.Flow;
import eu.mihosoft.vrl.workflow.FlowNode;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import jfxtras.labs.util.NodeUtil;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FXNewConnectionSkin implements ConnectionSkin<Connection>, FXSkin<Connection, Path> {

    private ObjectProperty<FlowNode> senderProperty = new SimpleObjectProperty<>();
    private ObjectProperty<FlowNode> receiverProperty = new SimpleObjectProperty<>();
    private Path connectionPath;
    private LineTo lineTo;
    private MoveTo moveTo;
//    private Shape startConnector;
    private Shape receiverConnector;
    private Flow flow;
    private ObjectProperty<Connection> modelProperty = new SimpleObjectProperty<>();
    private ObjectProperty<Parent> parentProperty = new SimpleObjectProperty<>();
    private String type;
    private Node lastNode;

    public FXNewConnectionSkin(Parent parent, FlowNode sender, Flow flow, String type) {
        setParent(parent);
        setSender(sender);
        
        this.flow = flow;
        this.type = type;

//        startConnector = new Circle(20);
        receiverConnector = new Circle(20);

        moveTo = new MoveTo();
        lineTo = new LineTo();
        connectionPath = new Path(moveTo, lineTo);

        init();
    }

    

    private void init() {

        final FlowNode sender = getSender();

        DoubleBinding startXBinding = new DoubleBinding() {
            {
                super.bind(sender.xProperty(), sender.widthProperty());
            }

            @Override
            protected double computeValue() {
                return sender.getX() + sender.getWidth();
            }
        };

        DoubleBinding startYBinding = new DoubleBinding() {
            {
                super.bind(sender.yProperty(), sender.heightProperty());
            }

            @Override
            protected double computeValue() {
                return sender.getY() + sender.getHeight() / 2;
            }
        };

        moveTo.xProperty().bind(startXBinding);
        moveTo.yProperty().bind(startYBinding);

        lineTo.xProperty().bind(receiverConnector.layoutXProperty());
        lineTo.yProperty().bind(receiverConnector.layoutYProperty());

        receiverConnector.onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                makeDraggable();
            }
        });
        
        receiverConnector.setLayoutX(getSender().getX() + getSender().getWidth());
        receiverConnector.setLayoutY(getSender().getY() + getSender().getHeight()/2.0);

    }

    private void makeDraggable() {

        connectionPath.toFront();
        receiverConnector.toFront();

        DraggingUtil.makeDraggable(receiverConnector, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                final Node n = NodeUtil.getNode(
                        getParent().getScene().getRoot(),
                        t.getSceneX(), t.getSceneY(), FlowNodeWindow.class);

                if (lastNode != null) {
                    lastNode.setEffect(null);
                    lastNode = null;
                }

                if (n != null) {
                    final FlowNodeWindow w = (FlowNodeWindow) n;
                    
                    FlowNode model = w.nodeSkinProperty().get().getModel();
                    
                    // we cannot create a connection from us to us
                    if (model == getSender()) {
                        return;
                    }

//                    // only one connection between nodes
//                    if (model.getFlow().getConnections(type).contains(
//                            getSender().getId(), w.nodeSkinProperty().get().getModel().getId())) {
//                        boolean error;
//                    }
                    
                    ConnectionResult connResult =
                            flow.tryConnect(
                            getSender(), w.nodeSkinProperty().get().getModel(),
                            type);

                    if (connResult.getStatus().isCompatible()) {

                        DropShadow shadow = new DropShadow(20, Color.WHITE);
                        Glow effect = new Glow(0.5);
                        shadow.setInput(effect);
                        w.setEffect(shadow);

                        receiverConnector.setFill(Color.GREEN);
                    } else {

                        DropShadow shadow = new DropShadow(20, Color.RED);
                        Glow effect = new Glow(0.8);
                        effect.setInput(shadow);
                        w.setEffect(effect);

                        receiverConnector.setFill(Color.RED);
                    }

                    lastNode = w;
                } else {
                    receiverConnector.setFill(Color.BLACK);
                }
            }
        }, null);


        receiverConnector.onMouseReleasedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {

                receiverConnector.toBack();
                connectionPath.toBack();

                if (lastNode != null) {
                    lastNode.setEffect(null);
                    lastNode = null;
                }

                Node n = NodeUtil.getNode(
                        getParent().getScene().getRoot(),
                        t.getSceneX(), t.getSceneY(), FlowNodeWindow.class);

                if (n != null) {
                    
                    FlowNodeWindow w = (FlowNodeWindow) n;

                    receiverConnector.setFill(Color.BLACK);
                    
                    FlowNode receiver = w.nodeSkinProperty().get().getModel();
                    
                    flow.connect(getSender(), receiver, type);
                }
                
                remove();
            }
        });

    }
    
    public Node getReceiverConnector() {
        return receiverConnector;
    }

    @Override
    public FlowNode getSender() {
        return senderProperty.get();
    }

    @Override
    public final void setSender(FlowNode n) {
        senderProperty.set(n);
    }

    @Override
    public ObjectProperty<FlowNode> senderProperty() {
        return senderProperty;
    }

    @Override
    public FlowNode getReceiver() {
        return receiverProperty.get();
    }

    @Override
    public void setReceiver(FlowNode n) {
        receiverProperty.set(n);
    }

    @Override
    public ObjectProperty<FlowNode> receiverProperty() {
        return receiverProperty;
    }

    @Override
    public Path getNode() {
        return connectionPath;
    }

    @Override
    public void setModel(Connection model) {
        modelProperty.set(model);
    }

    @Override
    public Connection getModel() {
        return modelProperty.get();
    }

    @Override
    public ObjectProperty<Connection> modelProperty() {
        return modelProperty;
    }

    final void setParent(Parent parent) {
        parentProperty.set(parent);
    }

    Parent getParent() {
        return parentProperty.get();
    }

    ObjectProperty<Parent> parentProperty() {
        return parentProperty;
    }

    @Override
    public void add() {
        NodeUtil.addToParent(getParent(), connectionPath);
//        VFXNodeUtils.addToParent(getParent(), startConnector);
        NodeUtil.addToParent(getParent(), receiverConnector);

//        startConnector.toBack();
        receiverConnector.toFront();
        connectionPath.toFront();
    }

    @Override
    public void remove() {
        NodeUtil.removeFromParent(connectionPath);
//        VFXNodeUtils.removeFromParent(startConnector);
        NodeUtil.removeFromParent(receiverConnector);
    }
}
