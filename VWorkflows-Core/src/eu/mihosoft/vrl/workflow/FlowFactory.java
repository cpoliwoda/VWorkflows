/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FlowFactory {

    public static VFlow newFlow() {

        VFlowModel model = FlowFactory.newFlowModel();
        
        VFlow flow = new VFlowImpl(model);

        return flow;
    }

    public static VFlow newFlow(
            SkinFactory<? extends ConnectionSkin, ? extends VNodeSkin> skinFactory) {
        

        VFlowModel model = FlowFactory.newFlowModel();

        VFlow flow = new VFlowImpl(model,skinFactory);

        return flow;
    }

    public static VFlowModel newFlowModel() {
        VFlowModel result = new VFlowModelImpl(null);
        result.setId("ROOT");
        return result;
    }
    
    public static IdGenerator newIdGenerator() {
        return new IdGeneratorImpl();
    }
}
