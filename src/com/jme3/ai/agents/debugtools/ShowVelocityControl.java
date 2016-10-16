package com.jme3.ai.agents.debugtools;

import com.jme3.ai.agents.Agent;
import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.debug.Arrow;

/**
 * Debug Helper to show the velocity of a steering behavior and it's rotation).
 * <br>Simply add this Control and see what happens.<br>
 * <br>
 * Note: The arrow is in the x-z Plane so you see it when viewing from the top.
 * <br>
 * Note: You may not add this Control to a Spatial <b>before</b> you've also
 * added the Agent to it.
 * @author MeFisto94
 */
public class ShowVelocityControl extends AbstractControl
{
    Node node;
    Geometry arrowVelocity;
    Geometry arrowDirection;
    Application app;
    Agent agent;
    Quaternion velocityRotation;
    
    public ShowVelocityControl(Application app)
    {
        super();
        this.app = app;
        velocityRotation = new Quaternion();
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (arrowVelocity == null || agent == null)
        {
            return;
        }
        
        velocityRotation.lookAt(agent.getVelocity(), Vector3f.UNIT_Y.clone());
        arrowVelocity.setLocalRotation(
                velocityRotation.multLocal(node.getWorldRotation().inverse()));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        if (this.spatial != null) {
           if (arrowDirection != null)
               arrowDirection.removeFromParent();
           if (arrowVelocity != null)
               arrowVelocity.removeFromParent();
        }
        
        super.setSpatial(spatial);
        
        if (!(spatial instanceof Node)) {
            throw new RuntimeException
                ("Cannot add this control to non-node spatials");
        }
        
        if (spatial.getControl(Agent.class) == null) {
            throw new RuntimeException("Cannot add this control to a spatial "
                    + "which doesn't contain an Agent (The Agent has to be "
                    + "added before the ShowVelocityControl");
        }
        
        agent = spatial.getControl(Agent.class);
        
        node = (Node)spatial;
        
        if (node.getChild("ArrowVelocity") == null) {
            arrowVelocity = generateVelocityArrow();
            node.attachChild(arrowVelocity);
        } else {
            arrowVelocity = (Geometry)node.getChild("ArrowVelocity");
        }
        
        if (node.getChild("ArrowDirection") == null) {
            arrowDirection = generateDirectionalArrow();
            node.attachChild(arrowDirection);
        } else {
            arrowDirection = (Geometry)node.getChild("ArrowDirection");
        }
    }
    
    public Geometry generateVelocityArrow()
    {
        Geometry geomArrowVelocity = new Geometry("ArrowVelocity",
                new Arrow(Vector3f.UNIT_Z.mult(0.15f)));
        Material matMagenta = new Material(app.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        matMagenta.setColor("Color", ColorRGBA.Magenta);
        geomArrowVelocity.setMaterial(matMagenta);
        
        return geomArrowVelocity;
    }
    
    public Geometry generateDirectionalArrow()
    {
        Geometry geomArrowDirection = new Geometry("ArrowDirection", 
                new Arrow(Vector3f.UNIT_Z.mult(0.15f)));
        Material matBlue = new Material(app.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        matBlue.setColor("Color", ColorRGBA.Blue);
        geomArrowDirection.setMaterial(matBlue);
        
        return geomArrowDirection;
    }
}
