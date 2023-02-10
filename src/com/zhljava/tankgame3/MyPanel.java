package com.zhljava.tankgame3;

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

    public MyPanel() {
        hero = new Hero(300, 200);
        //初始化敌人坦克
        for (int i = 0; i < enemySize; i++) {
            Enemy enemy = new Enemy((i + 1) * 100, 0);
            enemy.setDirect(2);
            enemies.add(enemy);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);

        //画出坦克
        drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            drawTank(enemy.getX(), enemy.getY(), g, enemy.getDirect(), 1);
        }

        //画出hero发射的子弹
        if (hero.bullet != null && hero.bullet.isLive == true) {
            g.setColor(Color.ORANGE);
            g.fillOval(hero.bullet.x, hero.bullet.y, 3, 3);
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //处理wasd键按下的情况
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {//按下W键
            hero.setDirect(0);
            hero.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            hero.setDirect(1);
            hero.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            hero.setDirect(2);
            hero.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            hero.setDirect(3);
            hero.moveLeft();
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
            this.repaint();
        }
    }
}
