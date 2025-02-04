package com.barghest.games.roan;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile {
	private int tilex, tiley, spdx, type;
	private int width = 32;
	private int height = 32;
	private int offx = 0;
	private int offy = 0;
	public Image tileimg;
	private String spriteSheet = "TileSet1";
	private Rectangle r;
	
	private MainCharac player = MainClass.getPlayer();
	private Background bg = MainClass.getBg1();
	private BufferedImage[] tiling = {Sprite.getSprite(offx, offy, width, height, 0, 0, spriteSheet),
									  Sprite.getSprite(offx, offy, width, height, 1, 0, spriteSheet), 
									  Sprite.getSprite(offx, offy, width, height, 2, 0, spriteSheet), 
									  Sprite.getSprite(offx, offy, width, height, 0, 1, spriteSheet),
									  Sprite.getSprite(offx, offy, width, height, 1, 1, spriteSheet),
									  Sprite.getSprite(offx, offy, width, height, 2, 1, spriteSheet),
									  Sprite.getSprite(offx, offy, width, height, 0, 2, spriteSheet),
									  Sprite.getSprite(offx, offy, width, height, 1, 2, spriteSheet),
									  Sprite.getSprite(offx, offy, width, height, 2, 2, spriteSheet)};
	
	public Tile(int x, int y, int typeInt) {
		tilex = x * width;
		tiley = y * height;
		type = typeInt;
		
		if (type == 7) {
			tileimg = tiling[0];
		} else if (type == 8) {
			tileimg = tiling[1];
		} else if (type == 9) {
			tileimg = tiling[2];
		} else if (type == 4) {
			tileimg = tiling[3];
		} else if (type == 5) {
			tileimg = tiling[4];
		} else if (type == 6) {
			tileimg = tiling[5];
		} else if (type == 1) {
			tileimg = tiling[6];
		} else if (type == 2) {
			tileimg = tiling[7];
		} else if (type == 3) {
			tileimg = tiling[8];
		} else { type = 10; }
		r = new Rectangle();
	}
	
	public void update() {
		spdx = bg.getSpdx() * 5;
		tilex += spdx;
        r.setBounds(tilex, tiley, width, height);
        
        if (type != 10){
        	player.setCenterY(player.getCenterY() + 1);
            checkVerticalCollision(player.getRect1());
        }
	}
	
   public void checkVerticalCollision(Rectangle playerCol){
    	/*if (playerCol.intersects(r)){
    		System.out.println("collision with player");
    	}*/
    	if (playerCol.intersects(r) && (type == 8 || type == 7 || type == 9)) {
    		System.out.println("collision with ground");
    		player.setJump(false);
    		player.setSpdY(0);
    		player.setCenterY(player.getCenterY() - 1);
    	}
    }
	   
	public int getTileX() {
		return tilex;
	}
	
	public int getTileY() {
		return tiley;
	}
	
	public void setTileX(int tilex) {
		this.tilex = tilex;
	}
	
	public void setTileY(int tiley) {
		this.tiley = tiley;
	}
	
	public Image getTileImage() {
		return tileimg;
	}
	
	public void setTileImg(Image tileimg) {
		this.tileimg = tileimg;
	}
}
