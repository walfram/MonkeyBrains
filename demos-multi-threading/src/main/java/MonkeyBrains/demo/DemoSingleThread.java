package MonkeyBrains.demo;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.Behavior;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.ArriveBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.CohesionBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.FleeBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.PathFollowBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.WanderAreaBehavior;
import com.jme3.ai.agents.debugtools.ShowVelocityControl;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the MainThreaded Example showing you how easy you can use
 * MonkeyBrains and that even without some AppStates, just by attaching the
 * control to the Spatial
 * 
 * @author MeFisto94
 */
public class DemoSingleThread extends SimpleApplication implements ActionListener {

    List<Agent> agentList = new LinkedList<>();
    BitmapText textActiveAgents;
    BitmapText textControls;
    BitmapText textBehaviorName;
    BehaviorEnum currentBehaviorType;
    
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1600, 800);
        
        DemoSingleThread app = new DemoSingleThread();
        app.setSettings(settings);
        app.setShowSettings(false);
        
        app.start();
    }

    @Override
    public void simpleInitApp() {
        currentBehaviorType = BehaviorEnum.ArriveBehavior;
        
        for (int i = 0; i < 1; i++)
            rootNode.attachChild(addAgent());
        
        setupCam();
        setupKeys();
        setupText();       
    }
    
    public void setupKeys() {
        inputManager.addListener(this, "clear", "add", "switch");
        inputManager.addMapping("clear", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("add", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addMapping("switch", new KeyTrigger(KeyInput.KEY_RETURN));
    }
    
    public void setupText()
    {
        textActiveAgents = new BitmapText(guiFont);
        textControls = new BitmapText(guiFont);
        textBehaviorName = new BitmapText(guiFont);
        
        textActiveAgents.setLocalTranslation(new Vector3f(0f, settings.getHeight(), 0f));
        
        guiNode.attachChild(textActiveAgents);
        guiNode.attachChild(textControls);
        guiNode.attachChild(textBehaviorName);
        
        textControls.setText
        ("SPACE => Clear\n + (0) => Add Agent\n ENTER => Change Behavior");
        
        textControls.setLocalTranslation(
            new Vector3f(
                0f, //settings.getWidth() - textControls.getLineWidth()
                settings.getHeight() - 2f * textActiveAgents.getLineHeight(),
                0f));
    }
    
    public void setupCam()
    {
        // We look at the XZ Plane when flying in the air above (pseudo 2d)
        cam.setLocation(Vector3f.UNIT_Y.mult(2f));
        Quaternion q = new Quaternion();
        q.lookAt(Vector3f.UNIT_Y.mult(-1f), Vector3f.UNIT_Z.mult(-1f));
        cam.setRotation(q);
    }

    public Spatial generateSpatial()
    {
        Node node = new Node("AgentNode");
        Geometry geomBox = new Geometry("Box", new Box(0.02f, 0.02f, 0.02f));
        Geometry geomArrowLook = new Geometry("Arrow", new Arrow(Vector3f.UNIT_Z.mult(0.15f)));
        
        Material matBlue = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material matRed = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        matBlue.setColor("Color", ColorRGBA.Blue);
        matRed.setColor("Color", ColorRGBA.Red);
        
        geomArrowLook.setMaterial(matBlue);
        geomBox.setMaterial(matRed);

        
        node.attachChild(geomArrowLook);
        node.attachChild(geomBox);
        
        return node;
    }
    
    public Spatial addAgent()
    {
        Agent agent = new Agent(0.1f);
        agentList.add(agent);
        
        agent.setMass(1f);
        agent.setMaxMoveSpeed(0.5f);
        SimpleMainBehavior mB = new SimpleMainBehavior();
        mB.addBehavior(newBehavior(currentBehaviorType));
        agent.setMainBehavior(mB);
        agent.setRotationSpeed(FastMath.TWO_PI);

        Spatial agentSpatial = generateSpatial();
        agentSpatial.addControl(agent);
        agentSpatial.setLocalTranslation(((float)Math.random() * 2f) - 1f, 0f, ((float)Math.random() * 2f) - 1f);
        agentSpatial.addControl(new ShowVelocityControl(this));
        agent.setEnabled(true);
        return agentSpatial;
    }
    
    public Behavior newBehavior(BehaviorEnum beh)
    {
        switch(beh)
        {
            case WanderAreaBehavior:
            {
                WanderAreaBehavior wab = new WanderAreaBehavior();
                wab.setArea(Vector3f.ZERO.clone(), Vector3f.UNIT_XYZ.mult(0.5f));
                return wab;
            }
            
            case ArriveBehavior:
                return new ArriveBehavior(new Vector3f(0f, 0f, 0f), 0.4f);
                
            case CohesionBehavior:
                return new CohesionBehavior(agentList);
                
            case PathFollowBehavior:
                List<Vector3f> list = new LinkedList<>();
                list.add(new Vector3f(0f, 0f, 0f));
                list.add(new Vector3f(0.75f, 0f, 0f));
                list.add(new Vector3f(0.75f, 0f, -0.75f));
                list.add(new Vector3f(0f, 0f, -0.75f));
                list.add(new Vector3f(-0.75f, 0f, -0.75f));
                list.add(new Vector3f(-0.75f, 0f, 0f));
                list.add(new Vector3f(-0.75f, 0f, 0.75f));
                list.add(new Vector3f(0f, 0f, 0.75f));
                list.add(new Vector3f(0f, 0f, 0f));
                final PathFollowBehavior pfb = new PathFollowBehavior(list, 0.5f, 1f);
                pfb.setPathDoneRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // setVelocity wouldn't work since when this is called,
                        // the pointer is already resolved.
                        pfb.getAgent().getVelocity().set(Vector3f.ZERO);
                    }
                });
                return pfb;
            case FleeBehavior:
                return new FleeBehavior(Vector3f.ZERO);
        }
        
        throw new RuntimeException("Don't know the Behavior Type " + beh.toString());
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        textActiveAgents.setText(agentList.size() + " Agents active");
        textBehaviorName.setText(currentBehaviorType.toString());
        textBehaviorName.setLocalTranslation(
            new Vector3f(settings.getWidth() - textBehaviorName.getLineWidth(),
            settings.getHeight(), 0f));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed)
            return;
        
        switch (name)
        {
            case "clear":
                for(Spatial s : rootNode.getChildren())
                {
                    s.removeFromParent();
                }
                agentList.clear();
                break;
                
            case "add":
                rootNode.attachChild(addAgent());
                break;
                
            case "switch":
                currentBehaviorType = currentBehaviorType.getNext();
                for (Agent a : agentList)
                {
                    ((SimpleMainBehavior)a.getMainBehavior()).clearBehaviors();
                    ((SimpleMainBehavior)a.getMainBehavior()).addBehavior(
                            newBehavior(currentBehaviorType));
                }
        }
    }
    
    public enum BehaviorEnum {
        WanderAreaBehavior,
        ArriveBehavior,
        CohesionBehavior,
        PathFollowBehavior,
        FleeBehavior;
        
        public BehaviorEnum getNext()
        {
            switch (this) 
            {
                case WanderAreaBehavior:
                    return ArriveBehavior;
                    
                case ArriveBehavior:
                    return PathFollowBehavior;
                   
                case PathFollowBehavior:
                    return CohesionBehavior;
                    
                case CohesionBehavior:
                    return FleeBehavior;
                    
                default:
                    return ArriveBehavior;
            }
        }
    };
}
