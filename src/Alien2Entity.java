/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class Alien2Entity extends Entity {

  private double moveSpeed = 300; // horizontal speed

  private Game game; // the game in which the alien exists

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  public Alien2Entity(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g;
    dx = -moveSpeed;  // start off moving right
  } // constructor

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move alien
   */
  public void move (long delta){
    // if we reach left side of screen and are moving left
    // request logic update
    if ((dx < 0) && (x < 5)) {
    	 dx *= -1;
      //game.updateLogic();   // logic deals with moving entities
                            // in other direction and down screen
    } // if

    // if we reach right side of screen and are moving right
    // request logic update
    if ((dx > 0) && (x > 895)) {
    	 dx *= -1;
      //game.updateLogic();
    } // if
    
    // proceed with normal move
    super.move(delta);
  } // move


  /* collidedWith
   * input: other - the entity with which the alien has collided
   * purpose: notification that the alien has collided
   *          with something
   */
   public void collidedWith(Entity other) {
	   if (other instanceof TreeEntity) {
		      
	       dx *= -1;
	      /* // notify the game that the alien is dead
	       game.notifyAlienKilled();*/
	      // used = true;
	     } 
   } // collidedWith
  
} // AlienEntity class
