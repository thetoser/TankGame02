package com.zhljava.tankgame5;

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
        hero = new Hero(500, 300);
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
        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);

        //画出坦克
        if (hero.isLive) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        }
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.isLive) { //当敌人坦克存活，才画出
                drawTank(enemy.getX(), enemy.getY(), g, enemy.getDirect(), 1);
                //画出敌人的子弹
                for (int j = 0; j < enemy.bullets.size(); j++) {
                    Bullet bullet = enemy.bullets.get(j);
                    if (bullet.isLive) {
                        g.setColor(Color.cyan);
                        g.fillOval(bullet.x, bullet.y, 3, 3);
                    } else {
                        enemy.bullets.remove(bullet);
                        //子弹消亡后可以再发射子弹
                        enemy.addBullet(enemy.getX(), enemy.getY(), enemy.getDirect());
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
            } else {
                hero.bullets.remove(bullet);
            }
        }

        //画出爆炸效果
        for (int i = 0; i < bombs.size(); i++) {
            try {
                Thread.sleep(50);
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

    //判断子弹是否击中坦克
    public void hitTank(Vector<Bullet> bullets, Tank t) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            switch (t.getDirect()) {
                case 0:
                case 2:
                    if (b.x > t.getX() && b.x < t.getX() + 40
                            && b.y > t.getY() && b.y < t.getY() + 60) {
                        b.isLive = false;
                        t.isLive = false;
                        //子弹击中坦克时，创建爆炸效果
                        Bomb bomb = new Bomb(t.getX(), t.getY());
                        bombs.add(bomb);
                    }
                    break;
                case 1:
                case 3:
                    if (b.x > t.getX() && b.x < t.getX() + 60
                            && b.y > t.getY() && b.y < t.getY() + 40) {
                        b.isLive = false;
                        t.isLive = false;
                        Bomb bomb = new Bomb(t.getX(), t.getY());
                        bombs.add(bomb);
                    }
                    break;
            }
        }
    }

    //防止坦克重叠
    public void changeDirect(Vector<Enemy> enemies) {
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < enemies.size(); j++) {
                //判断和其他的坦克是否重叠
                if (j != i) {
                    Enemy enemy = enemies.get(i);
                    int direct = enemy.getDirect();//当前坦克方向
                    Enemy elseEnemy = enemies.get(j);
                    int x = enemy.getX();
                    int y = enemy.getY();

                    int elseX = elseEnemy.getX();
                    int elseY = elseEnemy.getY();
                    switch (elseEnemy.getDirect()) {
                        case 0:
                        case 2:
                            if (direct == 0 || direct == 2) {
                                if (x <= elseX + 40 && x >= elseX - 40 &&
                                        y <= elseY + 60 && y >= elseY - 60) {
                                    if (direct == 0) {
                                        enemy.setDirect(2);
                                    } else {
                                        enemy.setDirect(0);
                                    }

                                }
                            } else {
                                if (x <= elseX + 40 && x >= elseX - 60 &&
                                        y <= elseY + 60 && y >= elseY- 40) {
                                    if (direct == 1) {
                                        enemy.setDirect(3);
                                    } else {
                                        enemy.setDirect(1);
                                    }

                                }
                            }
                            break;
                        case 1:
                        case 3:
                            if (direct == 0 || direct == 2) {
                                if (x <= elseX + 60 && x >= elseX - 40 &&
                                        y <= elseY + 40 && y >= elseY - 60) {
                                    if (direct == 0) {
                                        enemy.setDirect(2);
                                    } else {
                                        enemy.setDirect(0);
                                    }

                                }
                            } else {
                                if (x <= elseX + 60 && x >= elseX - 60 &&
                                        y <= elseY + 40 && y >= elseY- 40) {
                                    if (direct == 1) {
                                        enemy.setDirect(3);
                                    } else {
                                        enemy.setDirect(1);
                                    }

                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //处理wasd键按下的情况
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W && hero.isLive) {//按下W键
            hero.setDirect(0);
            if (hero.getY() > 0) {
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D && hero.isLive) {
            hero.setDirect(1);
            if (hero.getX() + 60 < 1000) {
                hero.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S && hero.isLive) {
            hero.setDirect(2);
            if (hero.getY() + 60 < 750) {
                hero.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A && hero.isLive) {
            hero.setDirect(3);
            if (hero.getX() > 0) {
                hero.moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_J && hero.isLive) {
            if (hero.bullets.size() < 5) {
                hero.shot();
            }
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
            if (hero.bullets != null && enemies.size() > 0) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    hitTank(hero.bullets, enemy);
                }
            }

            //是否击中hero
            if (hero.isLive && enemies.size() > 0) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    hitTank(enemy.bullets, hero);
                }
            }

            changeDirect(enemies);

            this.repaint();
        }
    }
}
