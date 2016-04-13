package wingman.modifiers.weapons;

/*Weapons are fired by motion controllers on behalf of players or ships
 * They observe motions and are observed by the Game World
 */
public abstract class AbstractRotatableWeapon extends AbstractWeapon {
    protected double angle;
    
    public AbstractRotatableWeapon(){
        super();
        this.angle = 0;
    }
    
    public void setAngle(double radians) {
        this.angle = radians;
    }
}
