package utils;

import java.awt.*;

public interface Renderable {
    void render(Graphics g, Camera c);
    double getRenderY();
}
