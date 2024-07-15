package net.crimson.a3dgame;


import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.cameras.Camera;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.materials.Material;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.renderer.Renderer;

import java.util.Random;

public class MyRenderer extends Renderer {
    private Plane mGround;
    private Camera mCamera;
    private Vector3 mCameraPosition;
    private final float MOVEMENT_SPEED = 0.1f;
    private float mCameraYaw = 0;
    private float mCameraPitch = 0;

    public MyRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        // Create ground
        mGround = new Plane(10, 10, 1, 1);
        Material groundMaterial = new Material();
        groundMaterial.setColor(0xff00ff00); // Green color for grass
        mGround.setMaterial(groundMaterial);
        mGround.setPosition(0, -1, 0);
        mGround.setRotation(Vector3.Axis.X, 90);
        getCurrentScene().addChild(mGround);

        // Create trees
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            float x = random.nextFloat() * 10 - 5;
            float z = random.nextFloat() * 10 - 5;
            addTree(x, z);
        }

        // Set up camera
        mCamera = getCurrentCamera();
        mCamera.setFarPlane(1000);
        mCameraPosition = new Vector3(0, 2, 0);
        mCamera.setPosition(mCameraPosition);
    }

    private void addTree(float x, float z) {
        // Tree trunk
        Cube trunk = new Cube(0.2f, true, true);
        Material trunkMaterial = new Material();
        trunkMaterial.setColor(0xff8B4513); // Brown color for trunk
        trunk.setMaterial(trunkMaterial);
        trunk.setPosition(x, -0.5, z);
        getCurrentScene().addChild(trunk);

        // Tree leaves
        Cube leaves = new Cube(0.6f, true, true);
        Material leavesMaterial = new Material();
        leavesMaterial.setColor(0xff228B22); // Green color for leaves
        leaves.setMaterial(leavesMaterial);
        leaves.setPosition(x, 0.3, z);
        getCurrentScene().addChild(leaves);
    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        mCamera.setPosition(mCameraPosition);
        mCamera.setRotation(0, mCameraYaw, mCameraPitch); // Keep roll fixed at 0

    }
    @Override
    public void onOffsetsChanged(float v, float v1, float v2, float v3, int i, int i1) {
    }

    @Override
    public void onTouchEvent(MotionEvent motionEvent) {
    }

    public void moveForward() {
        mCameraPosition.z -= MOVEMENT_SPEED;
    }

    public void moveBackward() {
        mCameraPosition.z += MOVEMENT_SPEED;
    }

    public void moveLeft() {
        mCameraPosition.x -= MOVEMENT_SPEED;
    }

    public void moveRight() {
        mCameraPosition.x += MOVEMENT_SPEED;
    }

    public void moveCamera(float xPercent, float yPercent) {
        mCameraPosition.x += xPercent * MOVEMENT_SPEED;
        mCameraPosition.z += yPercent * MOVEMENT_SPEED;
    }
    public void rotateCamera(float deltaX, float deltaY) {
        mCameraYaw += deltaX;
        mCameraPitch -= deltaY;

        // Clamp the pitch between -90 and 90 degrees
        mCameraPitch = Math.max(-90, Math.min(90, mCameraPitch));
    }


}