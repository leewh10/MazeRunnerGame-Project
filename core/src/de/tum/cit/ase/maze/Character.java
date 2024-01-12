package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.swing.*;

public class Character {
    private float x, y;
    private boolean hasKey;
    private int lives;
    private Texture texture;


    //Constructor
    public Character(float x, float y, boolean hasKey, int lives) {
        this.x = x;
        this.y = y;
        this.hasKey = hasKey;
        this.lives = lives;
        this.texture = new Texture("assets/character.png");
    }

    //getters and setters
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isHasKey() {
        return hasKey;
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void move() {
        float speed = 5; // Adjust the speed as needed

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }
}