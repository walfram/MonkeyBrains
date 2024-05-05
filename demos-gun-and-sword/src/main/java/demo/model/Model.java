package demo.model;

import com.jme3.ai.agents.Agent;

// TODO better name 
public class Model {

  private Weapons weapons = new Weapons();

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
