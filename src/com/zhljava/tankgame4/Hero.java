package com.zhljava.tankgame4;

import java.util.Vector;

public class Hero extends Tank {
    Bullet bullet = null;
    Vector<Bullet> bullets = new Vector<>();

    public Hero(int x, int y) {
        super(x, y);
    }

    public void shot() {
        switch (getDirect()) {
            case 0:
                bullet = new Bullet(getX() + 19, getY(), 0);
                break;
            case 1:
                bullet = new Bullet(getX() + 60, getY() + 19, 1);
                break;
            case 2:
                bullet = new Bullet(getX() + 19, getY() + 60, 2);
                break;
            case 3:
                bullet = new Bullet(getX(), getY() + 19, 3);
                break;
        }
        bullets.add(bullet);

        new Thread(bullet).start();
    }
}
