package com.zhljava.tankgame5;

import javax.swing.*;

/**
 * 增加功能 :
 * 敌人坦克发射子弹消亡后，可以再发射子弹
 * 敌人坦克击中我方坦克时，我方坦克消失
 * 防止坦克重叠
 */
public class TankGame05 extends JFrame {
    MyPanel mp = null;

    public static void main(String[] args) {
        TankGame05 tankGame01 = new TankGame05();
    }

    public TankGame05() {
        mp = new MyPanel();
        Thread thread = new Thread(mp);
        thread.start();
        this.add(mp);
        this.setSize(1000, 750);
        this.addKeyListener(mp);//让JFrame 监听mp的键盘事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
