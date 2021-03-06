### Released on Steam
Link to the game on Steam: https://store.steampowered.com/app/1385690/Battletronics/

* The game has been seen by: 1.7 million people
* The game's demo page has been visited by: 40 thousand people
* The game has been bought by: 600 people

### Requirements

- Java JDK downloaded on computer.
- Terminal able to run commands: 'javac' and 'java'.

### How to start the game

0. Make sure you are in the directory/folder where the game files are located.
1. Run the command: javac \*.java
2. Then run the command: java Menu

#### Controls

- Player1: arrow keys, delete, end.
- Player2: w, a, s, d, 1, q.
- Player3: u, h, j, k, ".", "-".
- Player4: numkey8, numkey4, numkey5, numkey6, numkey_divide, numkey_multiply.

#### Preview images of the game

![Image of playing board](readmeImages/img7.png) ![Image of playing board](readmeImages/img6.png) ![Image of playing board](readmeImages/img5.png) ![Image of playing board](readmeImages/img1.png) ![Image of playing board](readmeImages/img2.png) ![Image of playing board](readmeImages/img3.png)

#### Class Hierarchy

- Menu / MenuInstructions / SoundEffects
- App / Battletronics
- SetKeyBindings / PlayerMovement / PlayerInventory / CreateBattlefield / Players
- CollisionDetection / HealthPackSpawn / PowerUpSpawn / Environment
- FireRing / LightningStorm / WaterFlood
- Sprite is parent to -> All visable objects
- Blocks / HealthPacks / Powerups

#### Preview video of the game
![](readmeImages/previewVid.gif)
