package game;

import game.graphics.Model;
import game.graphics.ShaderProgram;
import game.utils.BoundingBox;
import game.utils.Transform;
import org.joml.Vector3f;


//position is the center of the object
public abstract class GameObject {
    protected JengaBoard board;


    public GameObject()
    {}

    public abstract void update(double deltaTime);

    public abstract void draw(ShaderProgram shader, int mat_model_loc, int mat_normal_loc);

    public abstract GameObject clone();
}
