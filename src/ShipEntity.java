/* ShipEntity.java
 * March 27, 2006
 * Represents player's ship
 */
public class ShipEntity extends Entity {

	private Game game; // the game in which the ship exists

	/*
	 * construct the player's ship input: game - the game in which the ship is being
	 * created ref - a string with the name of the image associated to the sprite
	 * for the ship x, y - initial location of ship
	 */
	public ShipEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
	} // constructor

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move ship
	 */
	public void move(long delta) {
		// stop at left side of screen
		if ((dx < 0) && (x < 10)) {
			return;
		} // if
			// stop at right side of screen
		if ((dx > 0) && (x > 900)) {
			return;
		} // if

		// stop at top of screen
		if ((dy < 0) && (y < 10)) {
			return;
		} // if

		// stop at bottom of screen
		if ((dy > 0) && (y > 510)) {
			return;
		} // if

		super.move(delta); // calls the move method in Entity
	} // move

	/*
	 * collidedWith input: other - the entity with which the ship has collided
	 * purpose: notification that the player's ship has collided with something
	 */
	public void collidedWith(Entity other) {
		if (other instanceof AlienEntity || other instanceof Alien2Entity || other instanceof Alien3Entity) {
			game.notifyDeath();
		} // if
	} // collidedWith

} // ShipEntity class