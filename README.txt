=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: andytong
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections
I use collections as the way to model the state of my game objects. Specifically, I wanted to use
a linkedlist of points for each of my game objects, both snakes and snake friends 
(owl, mouse, bullfrog). For the snake, a LinkedList is great to both add to the beginning (when 
moving) and also add the end (when eating). Each point for the snake is a segment of its body
and therefore order of the data matters. 
For the snake friends, each point in the linkedlist was a separate object present in the coordinate
field. A linkedlist is useful here because it is easy to add and to remove specific points within
the list.

  2. File I/O
I used File I/O to store nicknames and the high score associated with that nickname. I created a new
class IOProcessing that is instantiated in GameCourt with methods readFile and writeFile
At the beginning of each game, I ask the user for a nickname and also read in a text file with the
specified format of "[name] [score]" for each line. Exceptions are thrown accordingly. Then, because
I only want each user to be associated with one score (I implemented it to be their highest score),
I used a TreeMap that has the name as the key and the score as the value. Then the value set
was converted to an ArrayList in order to be sorted, and I also created another ArrayList to store
the usernames in the correct order. Then, in GameCourt, I call readFile() in order to create these
data structures in the backend. In my makeLeaderBoard method, I accessed the two arrayLists in order
to display the top 3 leaders. The leaderboard persisted through the game along with the current
score. 

Then, when the user dies, I call the writeFile method which adds a new line of the name and score
in the correct format. I call readFile once again to update and display the updated leaderboard 
in the bottom status bar, if the user's current score is their lifetime high and is in the top 3.

  3. Inheritance
I used inheritance by creating a separate abstract class Snake Friends that extends off of GameObj
abstract class and represents the game objects the snake interacts with. I used an abstract class
because I wanted to implement the spawn function for a generic Snake Friend and also because the 
class extends off another abstract class GameObj and the methods in there need to be implemented as
well. For example, Snake extends off GameObj (and needs to use functions such as clip).

My owl, superfood (bullfrog) and food (mouse) classes are all subclasses of snakeFriends. They
override some of the GameObj methods like intersect, move, and draw. But in particular, they 
override the spawn function that is created in my SnakeFriends abstract class. Each of my animals
has a distinct spawn pattern. For instance, the top left quarter of the game plane is known as the
"Owl Zone", so owls can only spawn there. The same is true for the bullfrogs. The mice, on the other
hand, can only spawn in the other 3 quarters of the plane. The spawn location variations cannot be
represented in an instance field. Dynamic dispatch occurs in GameCourt, when specific methods like
spawn and intersect are correctly overidden.

  4. JUnit Testing
I used JUnit testing to test the model of my game, such as my methods for each of my animal classes
and whether the interactions between the animals work as specified. Each test only tests for one
case with some including edge cases. My model for the game makes it easy such that the GUI is not
involved in my methods.

I will not be testing the File IO given that I have thrown the exceptions that could occur, 
with a JOption popup that notifies the user. Additionally the File IO processing uses the scores
from the model that I have tested before.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

The game class creates the GUI of the game as well as some of the controllers such as buttons.
The game class runs GameCourt.
GameCourt does some of the drawing of the game plane, and instantiates IOProcessor for File I/O,
adds key listeners for key input, implements the new game button, and also implements the tick 
which allows the snake to move based on key input. GameCourt also instantiates specific classes
such as Snake, Owl, Superfood, and Food, which interact with each other. The IOProcessor is also 
used to create the leaderboard from a text file and add the current user's data to a separate text 
file. Finally, the GameCourt, paints all of these interactions on to the game plane.

The GameObj abstract class provides some default implementation of game objects. The Snake class
extends off of this class by using some of the GameObj methods such as clip and also overriding 
some of them, such as move. 

The SnakeFriends abstract class extends off GameObj and provides default implementation of spawn
which is specific only too the animals the snake interacts with. Then, each of the Owl, Food, and
Superfood classes extend off of SnakeFriends, overriding spawn based on their spawn patterns along
with draw for each specific image.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

I originally tried using a 2D array to model the game state, but I found that it was hard to connect
some of the points together. I found that using a LinkedList instead to, for instance, represent
the snake is much easier to keep it connected and implement the movement aspect.

Additionally, I had trouble sorting the map because the scores could only come from the values, 
which the map can't sort by itself (it can only sort the keys). Since keys cannot have duplicates
and I wanted there to have a possibility of duplicate scores, I had to think of a different way
that involved making 2 arrayLists to represent scores and usernames and sorting them instead.

Traversing across these two arrayLists while traversing across the map also posed some problems
like incorrect matching between name and score which I fixed by implementing break within the 
double for loop.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I feel that my design overall has good separation of functionality. For example, each animal object
has their own class, and I also created my own IOProcessing class. This class keeps most of IO, and
the corresponding handling of exceptions separately. Additionally, I also created a displaytable
method that can be repeatedly called.

Something that I would refactor is the way I sorted the scores in my map. I could try to use a 
comparator which would be more efficient than two extra ArrayLists.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  Google images for pictures of owl, bullfrog, and mouse.
