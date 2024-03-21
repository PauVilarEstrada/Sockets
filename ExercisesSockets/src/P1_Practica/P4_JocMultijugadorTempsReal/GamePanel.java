package P4_JocMultijugadorTempsReal;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    private GameLogic gameLogic;

    public GamePanel() {
        gameLogic = new GameLogic();
        setPreferredSize(new Dimension(GameLogic.WIDTH, GameLogic.HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar las paletas de los jugadores
        List<GameLogic.Player> players = gameLogic.getPlayers();
        for (GameLogic.Player player : players) {
            g.fillRect(player.x, player.y, GameLogic.PADDLE_WIDTH, GameLogic.PADDLE_HEIGHT);
        }

        // Dibujar la pelota
        GameLogic.Ball ball = gameLogic.getBall();
        g.fillOval(ball.x, ball.y, GameLogic.BALL_SIZE, GameLogic.BALL_SIZE);

        Toolkit.getDefaultToolkit().sync(); // Sincronizar la ventana
    }
}
