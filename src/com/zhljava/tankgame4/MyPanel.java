package com.zhljava.tankgame4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

//坦克大战绘图区域
//监听键盘事件
public class MyPanel extends JPanel implements KeyListener, Runnable {
    Hero hero = null;
    Vector<Enemy> enemies = new Vector<>();
    int enemySize = 3;
    Vector<Bomb> bombs = new Vector<>();
    //用于显示爆炸效果
    Image image1;
    Image image2;
    Image image3;

    public MyPanel() {
        hero = new Hero(300, 200);
        //初始化敌人坦克
        for (int i = 0; i < enemySize; i++) {
            Enemy enemy = new Enemy((i + 1) * 100, 0);
            enemy.setDirect(2);
            new Thread(enemy).start();
            //给enemy 加入一颗子弹
            Bullet bullet = new Bullet(enemy.getX() + 19, enemy.getY() + 60, enemy.getDirect());
            enemy.bullets.add(bullet);
            new Thread(bullet).start();
            enemies.add(enemy);
        }
        //初始化图片
        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/com/zhljava/bomb_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/com/zhljava/bomb_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/com/zhljava/bomb_3.gif"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);

        //画出坦克
        drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.isLive) {//当敌人坦克存活，才画出
                drawTank(enemy.getX(), enemy.getY(), g, enemy.getDirect(), 1);
                //画出敌人的子弹
                for (int j = 0; j < enemy.bullets.size(); j++) {
                    Bullet bullet = enemy.bullets.get(j);
                    if (bullet.isLive) {
                        g.setColor(Color.cyan);
                        g.fillOval(bullet.x, bullet.y, 3, 3);
                    } else {
                        enemy.bullets.remove(bullet);
                    }
                }
            } else { //坦克不存活
                enemies.remove(enemy);
            }
        }

        //画出hero发射的子弹
        for (int i = 0; i < hero.bullets.size(); i++) {
            Bullet bullet = hero.bullets.get(i);
            if (bullet != null && bullet.isLive) {
                g.setColor(Color.ORANGE);
                g.fillOval(bullet.x, bullet.y, 3, 3);
            } else if (!(bullet.isLive)) {
                hero.bullets.remove(bullet);
            }
        }

        //画出爆炸效果
        for (int i = 0; i < bombs.size(); i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bomb bomb = bombs.get(i);
            if (bomb.life > 6) {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 3) {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            bomb.lifeDown();
            if (!bomb.isLive) {
                bombs.remove(bomb);
            }
        }

    }

    /**
     * @param x      坦克左上角x坐标
     * @param y      坦克左上角y坐标
     * @param g      画笔
     * @param direct 坦克方向
     * @param type   坦克类型
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        //根据不同类型坦克，设置不同颜色
        switch (type) {
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.yellow);
                break;
        }

        //根据坦克方向，来绘制坦克
        switch (direct) {
            case 0: //向上
                g.fill3DRect(x, y, 10, 60, false);//左边轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//中间
                g.fill3DRect(x + 30, y, 10, 60, false);//右边轮子
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y);//炮筒
                break;
            case 1: //向右
                g.fill3DRect(x, y, 60, 10, false);//上边轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//中间
                g.fill3DRect(x, y + 30, 60, 10, false);//下边轮子
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);//炮筒
                break;
            case 2: //向下
                g.fill3DRect(x, y, 10, 60, false);//左边轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//中间
                g.fill3DRect(x + 30, y, 10, 60, false);//右边轮子
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y + 60);//炮筒
                break;
            case 3: //向左
                g.fill3DRect(x, y, 60, 10, false);//上边轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//中间
                g.fill3DRect(x, y + 30, 60, 10, false);//下边轮子
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x, y + 20);//炮筒
                break;
        }

    }

    //判断我方子弹是否击中坦克
    public void hitTank(Vector<Bullet> bullets , Enemy enemy) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            switch (enemy.getDirect()) {
                case 0:
                case 2:
                    if (b.x > enemy.getX() && b.x < enemy.getX() + 40
                            && b.y > enemy.getY() && b.y < enemy.getY() + 60) {
                        b.isLive = false;
                        enemy.isLive = false;
                        //子弹击中坦克时，创建爆炸效果
                        Bomb bomb = new Bomb(enemy.getX(), enemy.getY());
                        bombs.add(bomb);
                    }
                    break;
                case 1:
                case 3:
                    if (b.x > enemy.getX() && b.x < enemy.getX() + 60
                            && b.y > enemy.getY() && b.y < enemy.getY() + 40) {
                        b.isLive = false;
                        enemy.isLive = false;
                        Bomb bomb = new Bomb(enemy.getX(), enemy.getY());
                        bombs.add(bomb);
                    }
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //处理wasd键按下的情况
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {//按下W键
            hero.setDirect(0);
            if (hero.getY() > 0) {
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(1);
            if (hero.getX() + 60 < 1000) {
                hero.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(2);
            if (hero.getY() + 60 < 750) {
                hero.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(3);
            if (hero.getX() > 0) {
                hero.moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_J) {
            hero.shot();
        }

        this.repaint();//面板重绘
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //是否击中敌人坦克
            if (hero.bullets != null) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    hitTank(hero.bullets, enemy);
                }
            }
            this.repaint();
        }
    }
}
