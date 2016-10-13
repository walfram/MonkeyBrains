/**
 * Copyright (c) 2014, jMonkeyEngine All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of 'jMonkeyEngine' nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.ai.agents.util.control;

import com.jme3.ai.agents.AIControl;
import com.jme3.ai.agents.Agent;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import java.util.LinkedList;
import java.util.List;

/**
 * Class with information about agents and consequences of their behaviors in
 * game. It is not necessary to use it but it enables easier game status
 * updates. Contains agents and game entities and provides generic ai control.
 *
 * @author Tihomir Radosavljević
 * @version 2.1.1
 */
public class MonkeyBrainsAppState extends AbstractAppState {

    /**
     * Status of game with agents.
     */
    private boolean inProgress = false;
    /**
     * Indicator if the agent in same team can damage each other.
     */
    protected boolean friendlyFire = true;
    /**
     * Application to which this game is attached.
     */
    protected Application app;
    /**
     * Controls for game.
     */
    protected GameControl gameControl;
    /**
     * List of all agents that are active in game.
     */
    protected List<Agent> agents;
    /**
     * List of all GameEntities in game except for agents.
     */
    protected List<AIControl> aiControls;

    /**
     * Maximum number of agents supported by framework.
     */
    public static final int MAX_NUMBER_OF_AGENTS = 1000;

    protected MonkeyBrainsAppState() {
        agents = new LinkedList<Agent>();
        aiControls = new LinkedList<AIControl>();
    }

    /**
     * Adding agent to game. It will be automatically updated when game is
     * updated, and agent's position will be one set into Spatial.
     *
     * @param agent agent which is added to game
     */
    public void addAgent(Agent agent) {
        agents.add(agent);
        //agent.setId(setIdCounterToAgent());
        if (inProgress) {
            agent.start();
        }
        //rootNode.attachChild(agent.getSpatial());
    }

    /**
     * Adding agent to game. It will be automatically updated when game is
     * updated.
     *
     * @param agent agent which is added to game
     * @param position position where spatial should be added
     */
    public void addAgent(Agent agent, Vector3f position) {
        agent.setWorldTranslation(position);
        agents.add(agent);
        //agent.setId(setIdCounterToAgent());
        if (inProgress) {
            agent.start();
        }
//        rootNode.attachChild(agent.getSpatial());
    }

    /**
     * Adding agent to game. It will be automatically updated when game is
     * updated.
     *
     * @param agent agent which is added to game
     * @param x X coordinate where spatial should be added
     * @param y Y coordinate where spatial should be added
     * @param z Z coordinate where spatial should be added
     */
    public void addAgent(Agent agent, float x, float y, float z) {
        agent.setWorldTranslation(new Vector3f(x, y, z));
        agents.add(agent);
        //agent.setId(setIdCounterToAgent());
        if (inProgress) {
            agent.start();
        }
//        rootNode.attachChild(agent.getSpatial());
    }

    /**
     * Removing agent from list of agents to be updated and its spatial from
     * game.
     *
     * @param agent agent who should be removed
     */
    public void removeAgent(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).stop();
                agents.get(i).getSpatial().removeFromParent();
                agents.remove(i);
                break;
            }
        }
    }

    /**
     * Disabling agent. It means from agent will be dead and won't updated.
     *
     * @param agent
     */
    public void disableAgent(Agent agent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).equals(agent)) {
                agents.get(i).stop();
                break;
            }
        }
    }

    /**
     * Method that will update all alive agents and fired bullets while active.
     */
    @Override
    public void update(float tpf) {
        if (!inProgress) {
            return;
        }
        for (int i = 0; i < agents.size(); i++) {
            agents.get(i).update(tpf);
        }
        for (int i = 0; i < aiControls.size(); i++) {
            aiControls.get(i).update(tpf);
        }
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<AIControl> getAIControls() {
        return aiControls;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }


    public void addAIControl(AIControl aiControl) {
        aiControls.add(aiControl);
    }

    public void removeAIControl(AIControl aiControl) {
        aiControl.getSpatial().removeFromParent();
        aiControls.remove(aiControl);
    }

    public static MonkeyBrainsAppState getInstance() {
        return GameHolder.INSTANCE;
    }

    public GameControl getGameControl() {
        return gameControl;
    }

    public void setGameControl(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    private static class GameHolder {

        private static final MonkeyBrainsAppState INSTANCE = new MonkeyBrainsAppState();
    }

    public void start() {
        inProgress = true;
        for (Agent agent : agents) {
            agent.start();
        }
    }

    public void stop() {
        inProgress = false;
        for (Agent agent : agents) {
            agent.stop();
        }
    }

    public Application getApp() {
        return app;
    }

    public void setApp(Application app) {
        this.app = app;
    }

}
