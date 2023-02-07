/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class AlienEntity extends Entity {

  private double moveSpeed = 600; // horizontal speed

  private Game game; // the game in which the alien exists

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  public AlienEntity(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g;
    dx = moveSpeed;  // start off moving right
  } // constructor

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move alien
   */
  public void move (long delta){
    // if we reach left side of screen and are moving left change direction
    if ((dx < 0) && (x < 5)) {
    	dx *= -1;
    } // if

    // if we reach right side of screen and are moving right change direction
    if ((dx > 0) && (x > 895)) {
    dx *= -1;
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
	   
	   // if the alien runs into a tree turn around
	   if (other instanceof TreeEntity) { 
	       dx *= -1;
	     } // if
   } // collidedWith
  
} // AlienEntity class
