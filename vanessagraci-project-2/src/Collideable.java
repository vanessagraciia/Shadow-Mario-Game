/** This interface is for entity that 'can check' collision
 * @param <T>: is the target of collision
 */
public interface Collideable<T> {
    /** Method to check collision
     * @param target: target of the collision
     * @return true if the collision happens
     */
    public abstract boolean collision(T target);
}
