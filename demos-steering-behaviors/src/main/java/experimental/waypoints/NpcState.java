package experimental.waypoints;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

public class NpcState extends BaseAppState {

  private final Node scene = new Node("npc-scene");
  
  public NpcState(Node rootNode) {
    rootNode.attachChild(scene);
  }

  @Override
  protected void initialize(Application app) {
    Spatial spatial = app.getAssetManager().loadModel("Models/Jaime/Jaime.j3o");
    AnimMigrationUtils.migrate(spatial);
    
    scene.attachChild(spatial);
    
    Vector3f initialWaypoint = getState(SimpleWaypointsState.class).initialWaypoint();
    spatial.setLocalTranslation(initialWaypoint);

    // Walk, Wave, Taunt, JumpEnd, Idle, Punches, SideKick, Run, JumpStart, Jumping
    
    spatial.getControl(AnimComposer.class).setCurrentAction("Idle");
    
    List<Vector3f> waypoints = getState(SimpleWaypointsState.class).waypoints();

    NpcMoveContext context = new NpcMoveContext(waypoints, initialWaypoint);
    Action<NpcMoveContext> initialAction = new IdleAction(context);
    
    spatial.addControl(new NpcActionControl<>(initialAction, context));
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
