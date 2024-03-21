package P4_JocMultijugadorTempsReal;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int PADDLE_WIDTH = 10;
    public static final int PADDLE_HEIGHT = 60;
    public static final int BALL_SIZE = 10;
    public static final int PADDLE_SPEED = 5;
    public static final int BALL_SPEED_X = 3;
    public static final int BALL_SPEED_Y = 2;

    private List<Player> players;
    private Ball ball;

    public GameLogic() {
        players = new ArrayList<>();
        players.add(new Player(0, HEIGHT / 2 - PADDLE_HEIGHT / 2)); // Player 1
        players.add(new Player(WIDTH - PADDLE_WIDTH, HEIGHT / 2 - PADDLE_HEIGHT / 2)); // Player 2
        ball = new Ball(WIDTH / 2 - BALL_SIZE / 2, HEIGHT / 2 - BALL_SIZE / 2);
    }

    public void update() {
        // Mover la pelota
        ball.x += ball.velX;
        ball.y += ball.velY;

        // Colisión con las paredes superior e inferior
        if (ball.y <= 0 || ball.y >= HEIGHT - BALL_SIZE) {
            ball.velY *= -1;
        }

        // Colisión con las paletas de los jugadores
        for (Player player : players) {
            if (ball.x <= player.x + PADDLE_WIDTH &&
                    ball.y + BALL_SIZE >= player.y &&
                    ball.y <= player.y + PADDLE_HEIGHT) {
                ball.velX *= -1;
            }
        }

        // Colisión con los bordes laterales (puntuación)
        if (ball.x <= 0 || ball.x >= WIDTH - BALL_SIZE) {
            // Reiniciar la posición de la pelota
            ball.x = WIDTH / 2 - BALL_SIZE / 2;
            ball.y = HEIGHT / 2 - BALL_SIZE / 2;
            // Reiniciar la velocidad de la pelota
            ball.velX = BALL_SPEED_X;
            ball.velY = BALL_SPEED_Y;
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Ball getBall() {
        return ball;
    }

    public static class Player {
        public int x, y;

        public Player(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Ball {
        public int x, y;
        public int velX = BALL_SPEED_X;
        public int velY = BALL_SPEED_Y;

        public Ball(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
