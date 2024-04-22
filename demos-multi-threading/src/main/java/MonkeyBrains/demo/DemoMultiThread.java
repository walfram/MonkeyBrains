package MonkeyBrains.demo;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.ApplyType;
import com.jme3.ai.agents.behaviors.Behavior;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.CohesionBehavior;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This is the MultiThreaded Example showing you how easy you can use
 * MonkeyBrains and that even without some AppStates, just by attaching the
 * control to the Spatial
 * 
 * @author MeFisto94
 */
public class DemoMultiThread extends DemoSingleThread {
    
    BitmapText textUnderrun;
    
    ArrayList<Agent> threadOneList, threadTwoList, threadThreeList;
    Future<Void> futureOne, futureTwo, futureThree;
    public final ExecutorService pool = Executors.newFixedThreadPool(3);
    int listIndex = 1;
    
    public static void main(String[] args) {
        DemoMultiThread app = new DemoMultiThread();
        app.start();
    }

    @Override
    public void setupText() {
        super.setupText();
        textUnderrun = new BitmapText(guiFont);
        textUnderrun.setColor(ColorRGBA.Red);
        textUnderrun.setText("!!Underrun!!");
        textUnderrun.setLocalTranslation(
                settings.getWidth() - textUnderrun.getLineWidth(),
                textUnderrun.getLineHeight(),
                0f);
        textUnderrun.setCullHint(CullHint.Always);
    }
    
    @Override
    public void simpleInitApp() {
        /* Since the parent class already generates the agents */
        threadOneList = new ArrayList<>();
        threadTwoList = new ArrayList<>();
        threadThreeList = new ArrayList<>();

        super.simpleInitApp();
        
        futureOne = generateFutureFor(getListById(1), 0f); // Init
        futureTwo = generateFutureFor(getListById(2), 0f);
        futureThree = generateFutureFor(getListById(3), 0f);
    }
    
    public void updateListIndex()
    {
        if (listIndex < 3)
            listIndex++;
        else
            listIndex = 1;
    }
    
    public List<Agent> getListById(int id)
    {
        switch(id)
        {
            case 1:
                return threadOneList;
                
            case 2:
                return threadTwoList;
                
            case 3:
                return threadThreeList;
                
            default:
                throw new RuntimeException("Invalid ListIndex! Not in [1, 3]");
        }
    }
    
    public int getNumOfAgents()
    {
        int sumAgents = 0;
        for (int i = 1; i <= 3; i++)
            sumAgents += getListById(i).size();
        
        return sumAgents;
    }
    
    @Override
    public Spatial addAgent()
    {
        Spatial s = super.addAgent();
        Agent a = s.getControl(Agent.class);
        
        /* Ignore SceneGraph's ControlUpdate, since we're triggering updateAI
         * from our own threads (and don't want the AI to update twice).
         * Note that you can also use this flag and interate over a list of
         * Agents if you want to update them even when they aren't currently
         * attached to the SceneGraph (Paging Library removed that chunk?)
         */
        a.setIgnoreScenegraph(true);
        
        a.setApplyType(ApplyType.DontApply);
        
        agentList.clear();
        getListById(listIndex).add(a);
        updateListIndex();
        
        return s;
    }
    
    public Future<Void> generateFutureFor(final List<Agent> list, final float tpf)
    {
        return pool.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception
            {
                try
                {
                    for (Agent a : list)
                    {
                       a.updateAI(tpf);
                    }
                }
                catch (Exception e)
                {
                    /* Since we don't use Future#get() in simpleUpdate */
                    System.err.println(e.getMessage());
                    System.err.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
                
                return null;
            }
        });
    }
    
    
    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        
        textActiveAgents.setText(getNumOfAgents() + " Agents active");
        
        if (!futureOne.isDone() || !futureTwo.isDone() || !futureThree.isDone())
        {
            textUnderrun.setCullHint(CullHint.Inherit);
            return;
            /* We will be out of sync. The game stays fluent but the AI
             * has lost a frame so it might be a good idea to simply cumulate
             * the tpf here and let the next iteration catch up again.
             * Or react in some other way to reduce AI load?
             */
        }
        
        textUnderrun.setCullHint(CullHint.Always);
        
        /* Apply the Behavior Calculations to the Spatials */
        for (int i = 1; i <= 3; i++)
        {
            for (Agent a : getListById(i))
            {
                a.setWorldTranslation(a.getPredictedPosition(tpf));
                a.setWorldRotation(a.getPredictedRotation());
            }
        }
        
        /* Restart the Calculations so they are done while controls update and
         * while the GPU is drawing
         */
        
        if (pool.isShutdown())
            return; // We are about to stop the application
        
        futureOne = generateFutureFor(getListById(1), tpf);
        futureTwo = generateFutureFor(getListById(2), tpf);
        futureThree = generateFutureFor(getListById(3), tpf);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed)
            return;
        
        /* To prevent concurrent access to the lists, cancel all calculations
         * they will be wrong anyway. Note: this doesn't really work, since
         * our futures don't seem to terminate instantly. Feel free to fix ;)
         */
        futureOne.cancel(true);
        futureTwo.cancel(true);
        futureThree.cancel(true);
        
        switch (name)
        {
                
            case "clear":
                for(Spatial s : rootNode.getChildren())
                {
                    s.removeFromParent();
                }

                for (int i = 1; i <= 3; i++)
                    getListById(i).clear();
                
                break;
                
            case "add":               
                rootNode.attachChild(addAgent());
                break;
                
            case "switch":
                currentBehaviorType = currentBehaviorType.getNext();
                
                for (int i = 1; i <= 3; i++)
                {
                    for (Agent a : getListById(i))
                    {
                        ((SimpleMainBehavior)a.getMainBehavior()).clearBehaviors();
                        ((SimpleMainBehavior)a.getMainBehavior()).addBehavior(
                                newBehavior(currentBehaviorType));
                    }
                }
        }
    }

    @Override
    public Behavior newBehavior(BehaviorEnum beh) {
        if (beh != BehaviorEnum.CohesionBehavior)
            return super.newBehavior(beh);
        else
        {
            ArrayList<Agent> hugeList = new ArrayList<>(getNumOfAgents());
            
            for (int i = 1; i <= 3; i++)
                hugeList.addAll(getListById(i));
            
            return new CohesionBehavior(hugeList);
        }
    }

    @Override
    public void stop() {
        super.stop();
        pool.shutdownNow();
    }
}