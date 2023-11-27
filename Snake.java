package com.javarush.games.snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";

    private final List<GameObject> snakeParts = new ArrayList<>();
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y) {
        GameObject first = new GameObject(x, y);
        GameObject second = new GameObject(x + 1, y);
        GameObject third = new GameObject(x + 2, y);
        snakeParts.add(first);
        snakeParts.add(second);
        snakeParts.add(third);
    }

    public void draw(Game game) {
        Color color = isAlive ? Color.BLACK : Color.RED;

        for (int i = 0; i < snakeParts.size(); i++) {
            GameObject part = snakeParts.get(i);
            String sign = (i != 0) ? BODY_SIGN : HEAD_SIGN;
            game.setCellValueEx(part.x, part.y, Color.NONE, sign, color, 75);
        }
    }

    public void setDirection(Direction direction) {
        if ((this.direction == Direction.LEFT || this.direction == Direction.RIGHT) && snakeParts.get(0).x == snakeParts.get(1).x) {
            return;
        }
        if ((this.direction == Direction.UP || this.direction == Direction.DOWN) && snakeParts.get(0).y == snakeParts.get(1).y) {
            return;
        }

        if (direction == Direction.UP && this.direction == Direction.DOWN) {
            return;
        } else if (direction == Direction.LEFT && this.direction == Direction.RIGHT) {
            return;
        } else if (direction == Direction.RIGHT && this.direction == Direction.LEFT) {
            return;
        } else if (direction == Direction.DOWN && this.direction == Direction.UP) {
            return;
        }

        this.direction = direction;
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead(); //Создаю новую голову

        if (checkCollision(newHead)) {
            isAlive = false; // Если найдено совпадение координат, устанавливаем флаг isAlive змеи в false
            return;
        }
        // Проверка на нахождение координат новой головы в пределах игрового поля
        if (newHead.x >= SnakeGame.WIDTH || newHead.x < 0 || newHead.y >= SnakeGame.HEIGHT || newHead.y < 0) {
            isAlive = false; // Установка флага isAlive в false, если координаты выходят за пределы игрового поля
            return;
        }
        // Проверка на совпадение координат новой головы с координатами яблока
        if (newHead.x == apple.x && newHead.y == apple.y) {
            apple.isAlive = false;
        }// Установка флага isAlive яблока в false, если координаты совпадают
        else {
            removeTail(); // Удаление хвоста
        }

        snakeParts.add(0, newHead); // Добавление новой головы в начало списка


    }


    public GameObject createNewHead() {
        GameObject head = snakeParts.get(0);
        int headX = head.x;
        int headY = head.y;
        int newHeadX = headX;
        int newHeadY = headY;
        if (direction == Direction.LEFT) {
            newHeadX--;
        } else if (direction == Direction.RIGHT) {
            newHeadX++;
        } else if (direction == Direction.UP) {
            newHeadY--;
        } else {
            newHeadY++;
        }

        return new GameObject(newHeadX, newHeadY);
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject) {
        for (GameObject part : snakeParts) {
            if (part.x == gameObject.x && part.y == gameObject.y) {
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return snakeParts.size();
    }
}