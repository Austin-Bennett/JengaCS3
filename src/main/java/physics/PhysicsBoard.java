package physics;

import org.ode4j.ode.*;
import static org.ode4j.ode.OdeHelper.*;

public class PhysicsBoard {

    private DWorld world;
    private DSpace space;
    private DJointGroup contactGroup;

    // Contact surface parameters — tune these
    private static final double FRICTION    = 2;
    private static final double BOUNCE      = 0.0;
    private static final double ERP         = 0.2; // error reduction (like baumgarte)
    private static final double CFM         = 1e-5; // constraint force mixing (softness)

    public void initPhysics() {
        OdeHelper.initODE2(0);

        world        = OdeHelper.createWorld();
        space        = OdeHelper.createHashSpace(null);
        contactGroup = OdeHelper.createJointGroup();

        // Z-up gravity
        world.setGravity(0, 0, -9.8);

        // These two are key for stable stacking
        world.setERP(ERP);
        world.setCFM(CFM);
    }

    // Called every frame
    public void stepPhysics(double deltaTime) {
        // Detect collisions and generate contact joints
        OdeHelper.spaceCollide(space, null, nearCallback);

        // Step the simulation
        world.quickStep(deltaTime); // quickStep is faster and stable enough for stacking

        // Clear contact joints — they're recreated fresh each frame
        contactGroup.empty();
    }

    // Called for every potentially colliding pair
    private final DGeom.DNearCallback nearCallback = (data, geomA, geomB) -> {
        DBody bodyA = geomA.getBody();
        DBody bodyB = geomB.getBody();

        // Max 4 contact points per pair — enough for box-box
        DContactBuffer contacts = new DContactBuffer(4);
        int numContacts = OdeHelper.collide(geomA, geomB, 4, contacts.getGeomBuffer());

        for (int i = 0; i < numContacts; i++) {
            DContact contact = contacts.get(i);
            contact.surface.mode   = OdeConstants.dContactBounce | OdeConstants.dContactSoftCFM;
            contact.surface.mu     = FRICTION;
            contact.surface.bounce = BOUNCE;
            contact.surface.soft_cfm = CFM;

            DJoint joint = OdeHelper.createContactJoint(world, contactGroup, contact);
            joint.attach(bodyA, bodyB);
        }
    };

    public DWorld getWorld()  { return world; }
    public DSpace getSpace()  { return space; }

    public void cleanup() {
        contactGroup.destroy();
        space.destroy();
        world.destroy();
        OdeHelper.closeODE();
    }
}