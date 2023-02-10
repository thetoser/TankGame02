package com.zhljava.tankgame5;

public class Enemy extends Tank implements Runnable {
    Bullet bullet;

    public Enemy(int x, int y) {
        super(x, y);
    }

    //子弹消亡后，可以再发射子弹
    public void addBullet(int x, int y, int direct) {
        switch (direct) {
            case 0:
                bullet = new Bullet(x + 19, y, direct);
                break;
            case 1:
                bullet = new Bullet(x + 60, y + 19, direct);
                break;
            case 2:
                bullet = new Bullet(x + 19, y + 60, direct);
                break;
            case 3:
                bullet = new Bullet(x, y + 19, direct);
                break;
        }
        bullets.add(bullet);
        new Thread(bullet).start();
    }

    @Override
    public void run() {
        while (true) {
            switch (getDirect()) {
                case 0:

                    for (int i = 0; i < 30; i++) {
                        if (getDirect() != 0) {
                            break;
                        }
                        if (getY() > 0) {
                            moveUp();
                        } else { //碰到边界，跳出循环
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (getDirect() == 0) { //因重叠转向后，不再随机转向
                        setDirect((int) (Math.random() * 4));
                    }
                    break;
                case 1:

                    for (int i = 0; i < 30; i++) {
                        if (getDirect() != 1) {
                            break;
                        }
                        if (getX() + 60 < 1000) {
                            moveRight();
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (getDirect() == 1) { //因重叠转向后，不再随机转向
                        setDirect((int) (Math.random() * 4));
                    }
                    break;
                case 2:

                    for (int i = 0; i < 30; i++) {
                        if (getDirect() != 2) {
                            break;
                        }
                        if (getY() + 60 < 750) {
                            moveDown();
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (getDirect() == 2) { //因重叠转向后，不再随机转向
                        setDirect((int) (Math.random() * 4));
                    }
                    break;
                case 3:

                    for (int i = 0; i < 30; i++) {
                        if (getDirect() != 3) {
                            break;
                        }
                        if (getX() > 0) {
                            moveLeft();
                        } else {
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (getDirect() == 3) { //因重叠转向后，不再随机转向
                        setDirect((int) (Math.random() * 4));
                    }
                    break;
            }

            if (!isLive) {
                break;
            }
        }
    }
}
