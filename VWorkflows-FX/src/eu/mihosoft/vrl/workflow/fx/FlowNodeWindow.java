/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow.fx;

import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VFlowModel;
import eu.mihosoft.vrl.workflow.VNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;
import jfxtras.labs.scene.control.window.WindowIcon;
import jfxtras.labs.util.event.MouseControlUtil;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FlowNodeWindow extends Window {

    private ObjectProperty<FXFlowNodeSkin> nodeSkinProperty = new SimpleObjectProperty<>();
    private ScalableContentPane content;

    public FlowNodeWindow(FXFlowNodeSkin skin) {

        nodeSkinProperty().set(skin);

        getLeftIcons().add(new CloseIcon(this));
        getLeftIcons().add(new MinimizeIcon(this));

        setStyle("-fx-background-color: rgba(120,140,255,0.2);-fx-border-color: rgba(120,140,255,0.42);-fx-border-width: 2;");

//        setStyle("-fx-background-color: rgb(120,140,255);-fx-border-color: rgb(255,255,255);-fx-border-width: 2;");

        OptimizableContentPane parentContent = new OptimizableContentPane();

        content = new ScalableContentPane();

        parentContent.getChildren().add(content);

        Pane root = new Pane();
        content.setContentPane(root);

        super.setContentPane(parentContent);
        addCollapseIcon(skin);

//        addSelectionRectangle(skin, root);


    }

    private void showFlowInWindow(VFlow flow, Stage stage, String title) {

        // create scalable root pane
        jfxtras.labs.scene.layout.ScalableContentPane canvas = new jfxtras.labs.scene.layout.ScalableContentPane();

        // define background style
        canvas.setStyle("-fx-background-color: linear-gradient(to bottom, rgb(10,32,60), rgb(42,52,120));");

        // create skin factory for flow visualization
        FXSkinFactory fXSkinFactory = new FXSkinFactory(canvas.getContentPane());

        // generate the ui for the flow
        flow.addSkinFactories(fXSkinFactory);

        // the usual application setup
        Scene scene = new Scene(canvas, 800, 800);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public Pane getWorkflowContentPane() {
        return content.getContentPane();
    }

    /**
     * @return the nodeSkinProperty
     */
    public final ObjectProperty<FXFlowNodeSkin> nodeSkinProperty() {
        return nodeSkinProperty;
    }

    private void addCollapseIcon(FXFlowNodeSkin skin) {

        if (skin == null) {
            return;
        }

        if (!(skin.getModel() instanceof VFlowModel)) {
            return;
        }

        final WindowIcon collapseIcon = new WindowIcon();

        collapseIcon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FXFlowNodeSkin skin = nodeSkinProperty.get();

                if (skin != null) {
                    VFlowModel model = (VFlowModel) skin.getModel();
                    model.setVisible(!model.isVisible());
                }
            }
        });

        getRightIcons().add(collapseIcon);

        if (skin.modelProperty() != null) {
            skin.modelProperty().addListener(new ChangeListener<VNode>() {
                @Override
                public void changed(ObservableValue<? extends VNode> ov,
                        VNode t, VNode t1) {
                    if (t1 instanceof VFlowModel) {
                        getRightIcons().add(collapseIcon);
                    } else {
                        getRightIcons().remove(collapseIcon);
                    }
                }
            });
        }


        // adds an icon that opens a new view in a separate window

        final WindowIcon newViewIcon = new WindowIcon();

        newViewIcon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FXFlowNodeSkin skin = nodeSkinProperty.get();

                if (skin != null) {

                    Stage stage = new Stage();
                    stage.setWidth(400);
                    stage.setHeight(300);

                    String nodeId = skin.getModel().getId();

                    for (VFlow vf : skin.getController().getSubControllers()) {
                        if (vf.getModel().getId().equals(nodeId)) {
                            showFlowInWindow(vf, stage, getTitle());
                            break;
                        }
                    }
                }
            }
        });

        getLeftIcons().add(newViewIcon);

    }

    private void addSelectionRectangle(FXFlowNodeSkin skin, Pane root) {
        if (skin == null) {
            return;
        }
        if (!(skin.getModel() instanceof VFlowModel)) {
            return;
        }
        Rectangle rect = new Rectangle();
        rect.setStroke(new Color(1, 1, 1, 1));
        rect.setFill(new Color(0, 0, 0, 0.5));
        MouseControlUtil.
                addSelectionRectangleGesture(root, rect);
    }
}
