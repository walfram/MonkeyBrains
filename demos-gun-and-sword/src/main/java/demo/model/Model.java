package demo.model;

import com.jme3.ai.agents.Agent;
import com.jme3.ai.agents.Updatable;

// TODO better name 
public class Model implements Updatable {

  private final Weapons weapons = new Weapons();

  @Override
  public void update(float tpf) {
    weapons.update(tpf);
  }

  public enum Team {NPC, PLAYER}

  private Team team = Team.NPC;

  public void assignTeam(Team team) {
    this.team = team;
  }

  public boolean isSameTeam(Agent<?> targetAgent) {
    Model other = (Model) targetAgent.getModel();
    return other.team == team;
  }

  public Weapons weapons() {
    return weapons;
  }
}
