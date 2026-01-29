package states;

import core.GameWorld;
import utils.SpriteLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class MenuState implements GameState {

    private final GameWorld gameWorld;
    private Point mousePosition;

    // Chicken animation
    private final BufferedImage[] chickenSprites;
    private final BufferedImage[] chickenSpritesFlipped;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private static final int FRAME_DELAY_MS = 100;
    private double chickenX;
    private boolean movingRight = true;
    private static final double CHICKEN_SPEED = 150; // pixels per second
    private static final int CHICKEN_SIZE = 96;

    // Colors
    private static final Color BG_TOP = new Color(255, 200, 120);
    private static final Color BG_BOTTOM = new Color(255, 140, 70);
    private static final Color GROUND_COLOR = new Color(139, 90, 43);
    private static final Color GRASS_COLOR = new Color(34, 139, 34);
    private static final Color BUTTON_COLOR = new Color(255, 90, 60);
    private static final Color BUTTON_HOVER = new Color(255, 120, 90);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 50);

    // Timing
    private long lastUpdateTime;

    public MenuState(GameWorld game) {
        this.gameWorld = game;
        this.lastUpdateTime = System.currentTimeMillis();

        // Load chicken run sprites
        BufferedImage sheet = SpriteLoader.load("/sprites/chicken/cute_chicken_run.png");
        int frameCount = 2;
        int frameWidth = sheet.getWidth() / frameCount;
        int frameHeight = sheet.getHeight();

        chickenSprites = new BufferedImage[frameCount];
        chickenSpritesFlipped = new BufferedImage[frameCount];

        for (int i = 0; i < frameCount; i++) {
            BufferedImage raw = sheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            chickenSprites[i] = scaleImage(raw, CHICKEN_SIZE, CHICKEN_SIZE);
            chickenSpritesFlipped[i] = scaleAndFlipImage(raw, CHICKEN_SIZE, CHICKEN_SIZE);
        }

        // Start chicken at left side
        chickenX = 50;
    }

    private BufferedImage scaleImage(BufferedImage src, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(src, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }

    private BufferedImage scaleAndFlipImage(BufferedImage src, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(src, width, 0, -width, height, null);
        g2d.dispose();
        return result;
    }

    /** Call this when returning to menu to reset animation timing */
    public void resetTiming() {
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        double dt = (now - lastUpdateTime) / 1000.0;
        // Clamp dt to avoid huge jumps if menu was inactive
        dt = Math.min(dt, 0.1);
        lastUpdateTime = now;

        int width = gameWorld.getGameWidth();

        // Update chicken position
        if (movingRight) {
            chickenX += CHICKEN_SPEED * dt;
            if (chickenX > width - CHICKEN_SIZE - 50) {
                movingRight = false;
            }
        } else {
            chickenX -= CHICKEN_SPEED * dt;
            if (chickenX < 50) {
                movingRight = true;
            }
        }

        // Update animation frame
        if (now - lastFrameTime > FRAME_DELAY_MS) {
            currentFrame = (currentFrame + 1) % chickenSprites.length;
            lastFrameTime = now;
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();

        // Sky gradient background
        GradientPaint skyGradient = new GradientPaint(0, 0, BG_TOP, 0, height, BG_BOTTOM);
        g2.setPaint(skyGradient);
        g2.fillRect(0, 0, width, height);

        // Ground area (bottom 20%)
        int groundY = (int) (height * 0.80);
        g2.setColor(GROUND_COLOR);
        g2.fillRect(0, groundY, width, height - groundY);

        // Grass line
        g2.setColor(GRASS_COLOR);
        g2.fillRect(0, groundY - 8, width, 16);

        // Title with shadow - responsive font size
        int titleFontSize = Math.min(72, width / 12);
        g2.setFont(new Font("Arial", Font.BOLD, titleFontSize));
        String title = "Chicken Isekai Attack";
        FontMetrics fm = g2.getFontMetrics();
        int titleX = (width - fm.stringWidth(title)) / 2;
        int titleY = (int) (height * 0.18);

        // Title shadow
        g2.setColor(SHADOW_COLOR);
        g2.drawString(title, titleX + 4, titleY + 4);

        // Title text
        g2.setColor(Color.WHITE);
        g2.drawString(title, titleX, titleY);

        // Subtitle
        int subtitleFontSize = Math.min(24, width / 40);
        g2.setFont(new Font("Arial", Font.ITALIC, subtitleFontSize));
        String subtitle = "A Bullet Heaven Adventure";
        fm = g2.getFontMetrics();
        int subtitleX = (width - fm.stringWidth(subtitle)) / 2;
        int subtitleY = titleY + titleFontSize / 2 + 10;
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString(subtitle, subtitleX, subtitleY);

        // Play button - responsive size and position
        int buttonWidth = Math.min(300, width / 4);
        int buttonHeight = Math.min(70, height / 12);
        int buttonX = (width - buttonWidth) / 2;
        int buttonY = (int) (height * 0.45);
        Rectangle playButton = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);

        // Button shadow
        g2.setColor(SHADOW_COLOR);
        g2.fill(new RoundRectangle2D.Double(buttonX + 4, buttonY + 4, buttonWidth, buttonHeight, 20, 20));

        // Button background
        boolean hover = mousePosition != null && playButton.contains(mousePosition);
        g2.setColor(hover ? BUTTON_HOVER : BUTTON_COLOR);
        g2.fill(new RoundRectangle2D.Double(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20));

        // Button border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new RoundRectangle2D.Double(buttonX, buttonY, buttonWidth, buttonHeight, 20, 20));

        // Button text
        int buttonFontSize = Math.min(36, buttonHeight / 2 + 4);
        g2.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        String buttonText = "PLAY";
        fm = g2.getFontMetrics();
        int textX = buttonX + (buttonWidth - fm.stringWidth(buttonText)) / 2;
        int textY = buttonY + ((buttonHeight - fm.getHeight()) / 2) + fm.getAscent();
        g2.setColor(Color.WHITE);
        g2.drawString(buttonText, textX, textY);

        // Draw chicken running at bottom
        int chickenY = groundY - CHICKEN_SIZE + 10;
        BufferedImage chickenFrame = movingRight ? chickenSprites[currentFrame] : chickenSpritesFlipped[currentFrame];
        g2.drawImage(chickenFrame, (int) chickenX, chickenY, null);

        // Instructions at bottom
        g2.setFont(new Font("Arial", Font.PLAIN, Math.min(16, width / 60)));
        String instructions = "Press F11 for fullscreen";
        fm = g2.getFontMetrics();
        g2.setColor(new Color(255, 255, 255, 150));
        g2.drawString(instructions, (width - fm.stringWidth(instructions)) / 2, height - 20);
    }

    // Store button bounds for click detection (calculated fresh each frame for responsiveness)
    private Rectangle getPlayButtonBounds() {
        int width = gameWorld.getGameWidth();
        int height = gameWorld.getGameHeight();
        int buttonWidth = Math.min(300, width / 4);
        int buttonHeight = Math.min(70, height / 12);
        int buttonX = (width - buttonWidth) / 2;
        int buttonY = (int) (height * 0.45);
        return new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F11) {
            gameWorld.getGame().toggleFullscreen();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameWorld.getStateStack().push(new WeaponSelectState(gameWorld));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (getPlayButtonBounds().contains(e.getPoint())) {
            gameWorld.getStateStack().push(new WeaponSelectState(gameWorld));
        }
    }
}
