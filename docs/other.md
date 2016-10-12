
##Random Stuff:
`com.jme3.ai.agents.Team` has been removed. For my purposes it's nothing more than `List<Agent>`, since I don't need `points` or `name`.
Also I removed `Agent`'s `team` field since for our understanding of "Agent Groups", one Agent can have multiple groups (Not sure how practical that will be when the behaviors interfer)  

`Agent` doesn't have a field `name` anymore and the matching constructors have been removed. The method `getName`, however, is still there. It returns the spatials name