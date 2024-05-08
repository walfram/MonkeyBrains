package demo.game;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.behaviors.npc.SimpleMainBehavior;
import com.jme3.ai.agents.behaviors.npc.steering.WanderAreaBehavior;
import com.jme3.ai.agents.util.control.MonkeyBrainsAppState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import demo.ai.AIAttackBehavior;
import demo.ai.AILookBehavior;
import demo.ai.AISeekBehavior;
import demo.model.Model;
import demo.model.Model.Team;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentContextState extends BaseAppState {

  private static final Logger logger = LoggerFactory.getLogger(AgentContextState.class);

  private final float maxMoveSpeed = 5f;
  private final float rotationSpeed = FastMath.DEG_TO_RAD * 90f;

  private final Set<Agent<?>> agents = new HashSet<>(128);

  @Override
  protected void initialize(Application app) {
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

  public void createNpcAgent(Spatial spatial) {
    Agent<Model> agent = new Agent<>(1f);
    Model model = new Model();
    agent.setModel(model);
    agent.setMaxMoveSpeed(maxMoveSpeed);
    agent.setRotationSpeed(rotationSpeed);

    SimpleMainBehavior main = new SimpleMainBehavior();

    WanderAreaBehavior wander = new WanderAreaBehavior();
    wander.setArea(spatial.getWorldTranslation(), new Vector3f(2, 0, 2));
    main.addBehavior(wander);

    AILookBehavior look = new AILookBehavior();
    look.setVisibilityRange(10f);
    main.addBehavior(look);
    
    AISeekBehavior seek = new AISeekBehavior();
    look.addListener(seek);
    main.addBehavior(seek);

    AIAttackBehavior attack = new AIAttackBehavior(agent.getModel().weapons());
    attack.useAttackCallback(target -> {
      getState(ProjectilesState.class).spawn(agent, target);
    });
    look.addListener(attack);
    main.addBehavior(attack);
    
    agent.setMainBehavior(main);
    spatial.addControl(agent);

    logger.debug("npc agent = {}", agent);

    agents.add(agent);

    // so that look behavior can now work
    MonkeyBrainsAppState.getInstance().addAgent(agent);
  }

  public void createPlayerAgent(Spatial player) {
    Agent<Model> agent = new Agent<>(1f);
    Model model = new Model();
    model.assignTeam(Team.PLAYER);
    agent.setModel(model);
    agent.setMaxMoveSpeed(maxMoveSpeed);
    agent.setRotationSpeed(rotationSpeed);

    // TODO add some behavior to player agent

    player.addControl(agent);
    agents.add(agent);
    
    logger.debug("player agent = {}", agent);

//    for (Agent<?> a : agents) {
//      SimpleMainBehavior main = (SimpleMainBehavior) a.getMainBehavior();
//      for (Behavior behavior : main.getBehaviors()) {
//        if (behavior instanceof SeekBehavior seek) {
//          seek.setTarget(agent);
//        }
//      }
//    }

    MonkeyBrainsAppState.getInstance().addAgent(agent);
  }
}
