/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow.fx;

import eu.mihosoft.vrl.workflow.Connection;
import eu.mihosoft.vrl.workflow.ConnectionSkin;
import eu.mihosoft.vrl.workflow.VFlow;
import eu.mihosoft.vrl.workflow.VNode;
import eu.mihosoft.vrl.workflow.VNodeSkin;
import eu.mihosoft.vrl.workflow.Skin;
import eu.mihosoft.vrl.workflow.SkinFactory;
import javafx.scene.Parent;
import jfxtras.labs.scene.control.window.Window;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FXSkinFactory implements SkinFactory<FXConnectionSkin, FXFlowNodeSkin> {

    private final Parent parent;
    private final FXSkinFactory parentFactory;
//    private Window clipboard;

    public FXSkinFactory(Parent parent) {
        this.parent = parent;
//        this.clipboard = clipboard;
        this.parentFactory = null;
    }

    private FXSkinFactory(Parent parent, FXSkinFactory parentFactory) {
        this.parent = parent;
//        this.clipboard = clipboard;
        this.parentFactory = parentFactory;
    }

    @Override
    public VNodeSkin createSkin(VNode n, VFlow flow) {
        return new FXFlowNodeSkin(this, parent, n, flow);
    }

    @Override
    public ConnectionSkin createSkin(Connection c, VFlow flow, String type) {
        System.out.println("skin for: " + c);
        return new FXConnectionSkin(this, parent, c, flow, type/*, clipboard*/);
    }

    @Override
    public SkinFactory<FXConnectionSkin, FXFlowNodeSkin> createChild(Skin parent) {
        
        System.out.println("PARENT: " + parent);

        FXSkinFactory result = new FXSkinFactory(((FXSkin) parent).getContentNode(), this);

        return result;
    }

    @Override
    public SkinFactory<FXConnectionSkin, FXFlowNodeSkin> getParent() {
        return this.parentFactory;
    }
}
