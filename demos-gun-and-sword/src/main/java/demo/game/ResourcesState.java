package demo.game;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class ResourcesState extends BaseAppState {

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
  
  public Spatial characterSpatial() {
    Spatial spatial = getApplication().getAssetManager().loadModel("Models/Demo_01/characters/character_01/character_01.j3o");

    Node armature = (Node) ((Node) spatial).getChild("Armature");
    Node characterMan = (Node) armature.getChild("characterMan");
    Node characterManEntity = (Node) characterMan.getChild("characterMan-entity");
    
    Node entity = (Node) characterManEntity.getChild("characterMan-ogremesh");
    AnimMigrationUtils.migrate(entity);
    AnimComposer animComposer = entity.getControl(AnimComposer.class);
    animComposer.setCurrentAction("base_stand");
    
    spatial.addControl(new AnimationControl(animComposer));
    
    return spatial;
  }
  
}
