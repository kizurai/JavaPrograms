package com.barghest.games.roan;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MainCharac {
	final int JUMPSPD = -15;
	final int MOVESPD = 5;
	private int BGSPD = 0;
	static int SCREEN_WIDTH = MainClass.getScreenWidth();
	private int centX = 0;
	private int centY = 0;

	private String spriteSheet = "CastlevaniaSoma";
	private int tilex = 45;
	private int tiley = 56;
	private int offx = 0;
	private int offy = 95;
	private BufferedImage[] walkingLeft = {Sprite.getSprite(offx, offy, tilex, tiley, 0, 4, spriteSheet), 
										   Sprite.getSprite(offx, offy, tilex, tiley, 1, 4, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 2, 4, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 3, 4, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 4, 4, spriteSheet)};
	private BufferedImage[] walkingRight = {Sprite.getSprite(offx, offy, tilex, tiley, 2, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 3, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 4, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 5, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 6, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 7, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 8, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 9, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 10, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 11, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 12, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 13, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 14, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 15, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 16, 3, spriteSheet),
										   Sprite.getSprite(offx, offy, tilex, tiley, 17, 3, spriteSheet)};
	private BufferedImage[] still = {Sprite.getSprite(offx, offy, tilex, tiley, 0, 0, spriteSheet), 
									 Sprite.getSprite(offx, offy, tilex, tiley, 1, 0, spriteSheet), 
									 Sprite.getSprite(offx, offy, tilex, tiley, 2, 0, spriteSheet),
									 Sprite.getSprite(offx, offy, tilex, tiley, 3, 0, spriteSheet)};
	private Animation walkLeft = new Animation(walkingLeft, 15);
	private Animation walkRight = new Animation(walkingRight, 10);
	private Animation standing= new Animation(still, 20);
	public Animation animation = standing;
	
	private static Background bg1 = MainClass.getBg1();
	private static Background bg2 = MainClass.getBg2();
	private int spdX = 0;
	private int spdY = 0;
	
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public static Rectangle rect = new Rectangle(0, 0, 0, 0);
	
	private String state;
	private boolean jump = false;
	private boolean ducked = false;
	private boolean moveRight = false;
	private boolean moveLeft = false;
	private boolean tapLeft = false;
	private int jumpStart = 0;
	private int jumpHeight = 50;
	private long IntervalTime = 0;
	private int dodgeLeftTime = 70;
	//private int minJumpTime = 30;
	
	
	public MainCharac() {
		animation.start();
		state = "IDLE";
	}
	
	public void checkState() {
		switch (state) {
		case "IDLE":
			animation = standing;
			break;
		case "WALKRIGHT":
			animation = walkRight;
			break;
		case "WALKLEFT":
			animation = walkLeft;
			break;
		case "JUMPLEFT":
			System.out.println((System.currentTimeMillis() - IntervalTime) + " jump left");
			animation = walkLeft;
			break;
		}
		animation.start();
	}
	
	public void update() {
		checkState();
		animation.update();
		
		if (spdX<0) {
			centX += spdX;
		}
		if (spdX == 0 || spdX < 0) {
			bg1.setSpdx(0);
			bg2.setSpdx(0);
		}
		
		if (System.currentTimeMillis() - IntervalTime > dodgeLeftTime && isTapLeft()) {
			spdX = 0;
			setTapLeft(false);
			state = "IDLE";
			System.out.println("stop dodging");
			BGSPD = MOVESPD/5;
		} else if (isTapLeft()) {
			BGSPD = MOVESPD;
		} else { BGSPD = MOVESPD/3; } 
		
			if (centX<=SCREEN_WIDTH / 4 && spdX > 0) {
				centX += spdX;
			} else if (centX>=SCREEN_WIDTH / 4 && spdX < 0) {
				centX -= spdX;
			}
			
			if (spdX > 0 && centX > SCREEN_WIDTH / 4) {
				bg1.setSpdx(-BGSPD);
				bg2.setSpdx(-BGSPD);
			} else if (spdX < 0 && centX < SCREEN_WIDTH / 4) {
				bg1.setSpdx(BGSPD);
				bg2.setSpdx(BGSPD);
			}
			
	        // Updates Y Position
			centY += spdY;
	        if (jump) {
	        	spdY = -1;
	        } else {
	        	spdY = 0;
	        }
	        System.out.println(centY + ", " + (jumpStart - jumpHeight));
	        if (centY <= jumpStart - jumpHeight) {
	        	jump = false;
	        	System.out.println("hit jump limit");
	        }
		
		if(centX + spdX <= SCREEN_WIDTH / 5) {
			centX = SCREEN_WIDTH / 5 + 1;
		}
		rect.setRect(centX + 3, centY + 3, tilex - 3, tiley - 3);
	}
	
	public void moveRight() {
		if (state == "IDLE") {
			setMovingRight(true);
			spdX = MOVESPD;
			state = "WALKRIGHT";
		}
	}
	public void moveLeft() {
		if (state == "IDLE") {
			setMovingLeft(true);
			spdX = -MOVESPD;
			state = "WALKLEFT";
		}
	}

	public void tapLeft() {
		System.out.println("Tapped Left!!!");
		setTapLeft(true);
		spdX = -MOVESPD * 2;
		state = "JUMPLEFT";
		IntervalTime = System.currentTimeMillis();
	}
	
	public void jump() {
		if (jump == false) {
			//spdY = JUMPSPD;
			jumpStart = centY;
			jump = true;
			state = "JUMP";
			System.out.println("jumping");
		}
	}
	
	public void stop() {
		if (!isMovingRight() && !isMovingLeft() && !isTapLeft()) {
			spdX = 0;
			state = "IDLE";
		} else if (isMovingRight() && !isMovingLeft()) {
			System.out.println("stop and move right");
			moveRight();
		} else if (!isMovingRight() && isMovingLeft()) {
			moveLeft();
			System.out.println("stop and move left");
		}
	}

	public void shoot() {
		Projectile p = new Projectile(centX + 50, centY - 25);
		projectiles.add(p);
	}

	public boolean isJump() { return jump; }
	public void setJump(boolean jump) { this.jump = jump; }
	public boolean isDucked() { return ducked; }
	public void setDucked(boolean ducked) { this.ducked = ducked; }
	public void setMovingRight(boolean moving) { this.moveRight = moving; }
	public boolean isMovingRight() { return moveRight; }
	public void setMovingLeft(boolean moving) { this.moveLeft = moving; }
	public boolean isMovingLeft() { return moveLeft; }
	public void setTapLeft(boolean tap) { this.tapLeft = tap; }
	public boolean isTapLeft() { return tapLeft; }
	
	public int getSpdX() { return spdX; }
	public int getSpdY() { return spdY;	}
	public void setSpdX(int spdX) { this.spdX = spdX; }
	public void setSpdY(int spdY) { this.spdY = spdY; }
	public int getHeight() { return tiley; }
	public int getCenterX() { return centX;	}
	public int getCenterY() { return centY;	}
	public void setCenterX(int centX) {	this.centX = centX;	}
	public void setCenterY(int centY) {	this.centY = centY;	}
	public Rectangle getRect1() { return rect; }
	
	public ArrayList<Projectile> getProjectiles() { return projectiles;	}
}