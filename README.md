#MonkeyBrains
MonkeyBrains is a sophisticated AI Engine for jMonkeyEngine. It uses an agent framework to wrap human and AI controlled characters in plugin-based AI algorithms so that different each game can pick out whichever AI techniques fits best.

**Note:** This is MeFisto94's Fork of the official MonkeyBrains Project. See the chapter "Fork" for more information about the goals of this fork.

##Fork:
The goals for this fork are relatively simple, I want to decouple the Agents from the "Framework" Parts.  
What I call "Framework" Parts is something like the `Inventory`, the `Weapon` System and the `Hitpoints`.  
In addition to that I want to redesign the `MonkeyBrainsAppState` or maybe even dropping it partially (since it essentially only updates the `Agent`).  
For this I will base the fork on the Version `1.0.0` so before the changes made by the User `Pesegato`, just because I think it would disturb to base your fork on something like another fork.
  
**Why??**  
This is really simple: For a FPS project I essentially have my own definitions of `Weapons`, `Inventory` and more important:  
I don't want to have some AppState changing the Translations of my objects (which wouldn't work anyway because of the Physics Engine fighting this).  
What I noticed as I wrote this, essentially, I want to extract the Steering Behaviors [jmesteer](http://jmesteer.bdevel.org) from MonkeyBrains again.
On the other hand I saw, there is no pre-merge version available it seems.

## Fork Status:
I've done pretty much already and for now I am mostly done with it. I might change stuff I find or users report but in general we now reached a level of good useability (see Getting Started for that).  
This fork fixed a few bugs (Framerate dependant movements, incorrect rotations, useful rotation speed) and also implemented some features (multitasking, automatic updating using the scenegraph, manual use of agents, controlling rigidbodies and BetterCharacterControls).  

## Getting Started
Because we did some major changes, the Demos are only partially valid. Luckily we simplified the design in our fork so it's easy for you to use:  
The following snippet is somplete enough for an easy AI behavior. Note that it would actually be a three-liner if there weren't so many parameters to set (since the default values differ much per usecase)  

```
Agent agent = new Agent(0.1f); // Radius 0.1m
agent.setMass(1f); // Actually the default, 1kg
agent.setMaxMoveSpeed(0.5f);
agent.setMainBehavior(new ArriveBehavior(Vector3f.ZERO.clone()));
agent.setRotationSpeed(FastMath.TWO_PI);

Spatial agentSpatial = new Node("AgentSpatial");
agentSpatial.addControl(agent);
agentSpatial.setLocalTranslation(((float)Math.random() * 2f) - 1f, 0f, ((float)Math.random() * 2f) - 1f);
```

For more advanced topics see the `demos` folder. It actually consists of a "DemoSingleThread" Application which is Single Threaded and a Multi Threaded Application.  
Actually having Monkey Brains Multithreaded is really easy, but stuff like the input Listener and all make it a bit more verbose.

##Documentation for MonkeyBrains:
User guides, documentation for MonkeyBrains have been made and you can see it at wiki pages

##Working games:
For more examples of working games built with MonkeyBrains see:
https://github.com/QuietOne/MonkeyBrainsDemoGames

##Note:
If you have suggestion or any questions, please see forum:
http://hub.jmonkeyengine.org/forum/board/projects/monkeybrains/

##Licence:
Distributed under the BSD licence.
