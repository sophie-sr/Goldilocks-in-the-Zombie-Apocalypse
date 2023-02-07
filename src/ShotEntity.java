/* ShotEntity.java
 * March 27, 2006
 * Represents player's ship
 */
public class ShotEntity extends Entity {

  private double moveSpeed = -300; // vert speed shot moves
  private Game game; // the game in which the ship exists

  /* construct the shot
   * input: game - the game in which the shot is being created
   *        ref - a string with the name of the image associated to
   *              the sprite for the shot
   *        x, y - initial location of shot
   */
  public ShotEntity(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g;
    dx = moveSpeed;
  } // constructor

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move shot
   */
  public void move (long delta){
    super.move(delta);  // calls the move method in Entity

  } // move


  /* collidedWith
   * input: other - the entity with which the shot has collided
   * purpose: notification that the shot has collided
   *          with something
   */
   public void collidedWith(Entity other) {
     
     // if it has hit the player, notidy death
     if (other instanceof ShipEntity) {
      
       game.notifyDeath();
     } // if

   } // collidedWith

} // ShipEntity class