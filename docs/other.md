
##Random Stuff:
`com.jme3.ai.agents.Team` has been removed. For my purposes it's nothing more than `List<Agent>`, since I don't need `points` or `name`.  
Also I removed `Agent`'s `team` field since for our understanding of "Agent Groups", one Agent can have multiple groups (Not sure how practical that will be when the behaviors interfer)  
  
`Agent` doesn't have a field `name` anymore and the matching constructors have been removed. The method `getName`, however, is still there. It returns the spatials name  
  
We now use the world rotation and translation everywhere. Is that good? I'm not sure yet. The World Rotation might be problematic when you fix your model by an intermediary 90° Node.  
On the other hand, when you have an agent standing on a car, you might want his rotation to be influenced by the car (and thus adding him as subnode of the car).  

Renamed the GameEntityException to AIControlException. This is part of a larger refactoring.  
Also renamed the GameEntitySeenEvent/Listener to AIControlSeenEvent/Listener.  