/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import game.GamePanel;
import game.UtilityTool;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;

/**
 *
 * @author Andre Policios
 */
public class Entity {
    GamePanel gp;
    
    public int worldX, worldY;
    
    
    public BufferedImage up1, up2,up3,up4, up5, up6, down1, down2,down3,down4,down5,down6, 
            left1, left2,left3,left4,left5,left6, right1, right2,right3,right4,right5,right6;
    public BufferedImage attackUp1,attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2,
            attackRight1, attackRight2;
    public String direction = "down";
    
    
    public int spriteNum = 1;
    public Rectangle solidArea= new Rectangle(0,0,48,48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    
    String dialogues[] = new String[20];
    int dialogueIndex = 0;
    
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;   
    boolean hpBarOn= false;
    public boolean collision = false;
    public boolean cooldownMessageShown = false;
    
    public BufferedImage image;
    public String name;

    //counter 
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    int dyingCounter = 0;
    public int shotAvailableCounter = 0;
    int hpBarCounter = 0;
    int regeneration, counterRegen;
    
    //character attributes
    public int maxLife, life;
    public int maxMana, mana;
    public int speed;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;
    public int cooldown;
    
    //Item Attributes
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    DecimalFormat df = new DecimalFormat("#0");
    //type
    public int type;
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_doorOnly = 8;
    
    
    public Entity(GamePanel gp){
        this.gp = gp;
    }
    
    public void setAction(){
        
    }
    public void speak(){
        if(dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        
        
        switch(gp.player.direction){
            case "up":
                direction = "down";
                break;
                
            case "down":
                direction = "up";
                break;
                
            case "left":
                direction = "right";
                break;
                
            case "right":
                direction = "left";
                break;
            
        }
    }
    
    public void use(Entity entity){}
    public void checkDrop(){}
    public void dropItem(Entity droppedItem){
        for(int i = 0; i < gp.obj.length; i++){
            if(gp.obj[i] == null){
                gp.obj[i] = droppedItem;
                gp.obj[i].worldX = worldX;
                gp.obj[i].worldY = worldY;
                break;
            }
        }
    }
    public void update(){
        setAction();
        
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        
        if(this.type == type_monster && contactPlayer == true){
            damagePlayer(attack);
        }
        
        if(collisionOn == false){
                switch(direction){
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            
            
            spriteCounter++;
            if(spriteCounter > 12){ 
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 1;
                }   
                spriteCounter = 0;
            }
            if(invincible == true){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }
    }
    
    public void damagePlayer(int attack){
        
        if(gp.player.invincible == false){
                gp.playSE(6);
                
                int damage = attack - gp.player.defense;
                    if(damage == 0){
                        damage = 0;
                    }
                gp.player.life -= damage;
                
                gp.player.invincible = true;
            }
    }
    
    public void draw(Graphics2D g2){
        BufferedImage  image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.worldY &&
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
                
                switch(direction){
                    case "up":
                        if(spriteNum == 1){image = up1;}
                        if(spriteNum == 2){image = up2;}
                        break;
                    case "down":
                        if(spriteNum == 1){image = down1;}
                        if(spriteNum == 2){image = down2;}
                        break;

                    case "left":
                        if(spriteNum == 1){image = left1;}
                        if(spriteNum == 2){image = left2;}
                        break;

                    case "right" :
                        if(spriteNum == 1){image = right1;
                        }
                        if(spriteNum == 2){image = right2;}
                        break;
                }
                
                if(type == 2 && hpBarOn == true){
                    double oneScale = (double)gp.tileSize/maxLife;
                    double hpBarValue = oneScale*life;
                    
                    
                    
                    //background
                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(screenX-1, screenY-6, gp.tileSize + 2, 7);
                    //Current HP
                    g2.setColor(new Color(255,0,30));
                    g2.fillRect(screenX, screenY -5,(int)hpBarValue, 5);
                    
                    hpBarCounter++;
                    
                    if(hpBarCounter> 600){
                        hpBarCounter =0;
                        hpBarOn = false;
                    }
                }
                if(invincible == true){
                    hpBarOn = true;
                    hpBarCounter = 0;
                    changeAlpha(g2,0.6F);
                }
                if(dying == true){
                    
                    dyingAnimation(g2);
                }
                g2.drawImage(image, screenX, screenY, null);
                
                changeAlpha(g2,1F);
            }
            
    }
    
    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;
        int i = 5;
        if(dyingCounter <= i){changeAlpha(g2,0f);  }
        if(dyingCounter > i && dyingCounter <= i*2){changeAlpha(g2,1f);}
        if(dyingCounter > i*2 && dyingCounter <= i*3){changeAlpha(g2,0f);}
        if(dyingCounter > i*3 && dyingCounter <= i*3){changeAlpha(g2,1f);}
        if(dyingCounter > i*4 && dyingCounter <= i*4){changeAlpha(g2,0f);}
        if(dyingCounter > i*5 && dyingCounter <= i*5){changeAlpha(g2,1f);}
        if(dyingCounter > i*6 && dyingCounter <= i*6){changeAlpha(g2,0f);}
        if(dyingCounter > i*7 && dyingCounter <= i*7){changeAlpha(g2,1f);}
        if(dyingCounter > i*8){
            
            alive = false;
        }
        
    }
    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }
    
    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        
        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath+ ".png"));
            image = uTool.scaleImage(image,width,height);
        }catch(Exception e){
            e.printStackTrace();
        }
        return image;
        
    }
}
