package utils;
import states.GameState;

import java.awt.*;
import java.util.Stack;

public class StateStack {
    private Stack<GameState> states;

    public StateStack() {
        states = new Stack<>();
    }

    public void push(GameState state) {
        states.push(state);
    }

    public void pop() {
        states.pop();
    }

    public void change(GameState state) {
        while (!states.empty()) {
            pop();
        }
        push(state);
    }

    public void update() {
        if (!states.empty()) {
            states.peek().update();
        }
    }

    public void render(Graphics g) {
        if (!states.empty()) {
            states.peek().render(g);
        }
    }

    public GameState peek() {
        return states.isEmpty() ? null : states.peek();
    }

}
