package experimental.compound;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.SimpleLookBehavior;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.ArriveBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.WanderAreaBehavior;
import com.jme3.ai.agents.util.control.MonkeyBrainsAppState;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;
import java.util.Set;
import jme3utilities.math.noise.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentsState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(AgentsState.class);
  
  private final Node scene = new Node("agents-scene");
  
  private final Generator random = new Generator(42);
  
  public AgentsState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    Spatial spatial = app.getAssetManager().loadModel("Models/Jaime/Jaime.j3o");
    scene.attachChild(spatial);
    
    logger.debug("spatial {} bound = {}", spatial.getName(), spatial.getWorldBound());

    AnimMigrationUtils.migrate(spatial);
    
    logger.debug("ctrl #0 = {}", spatial.getControl(0));
    logger.debug("ctrl #1 = {}", spatial.getControl(1));

    // Walk, Wave, Taunt, JumpEnd, Idle, Punches, SideKick, Run, JumpStart, Jumping
    Set<String> animClipsNames = spatial.getControl(AnimComposer.class).getAnimClipsNames();
    logger.debug("animations = {}", animClipsNames);
    
    spatial.getControl(AnimComposer.class).setCurrentAction("Walk");

    Agent<Void> agent = new Agent<>(1f);

    WanderAreaBehavior wander = new WanderAreaBehavior();
    wander.setArea(new Vector3f(), new Vector3f(32, 0, 32));

    ArriveBehavior arrive = new ArriveBehavior(new Vector3f(Vector3f.NAN));
    
    SimpleLookBehavior look = new SimpleLookBehavior();
    look.addListener(event -> {
      List<AIControl> aiControlSeen = event.getAIControlSeen();
      if (aiControlSeen.isEmpty())
        return;

      AIControl target = random.pick(aiControlSeen);
      
      if (Vector3f.NAN.equals(target.getWorldTranslation())) {
        arrive.setTarget((Agent) target);
      }
      
      if (arrive.getTarget().distanceTo(target) <= 0.5f) {
        
      }
    });
    look.setVisibilityRange(20f);

    SimpleMainBehavior main = new SimpleMainBehavior();
    agent.setMainBehavior(main);
    spatial.addControl(agent);
    
    main.addBehavior(wander);
    main.addBehavior(look);
    
    logger.debug("wander.agent = {}", wander.getAgent());
    logger.debug("look.agent = {}", look.getAgent());

    getState(MonkeyBrainsAppState.class).addAgent(agent);
  }

  @Override
  protected void cleanup(Application app) {

  }

  @Override
  protected void onEnable() {

  }

  @Override
  protected void onDisable() {

  }
}
