package utils;
import states.GameState;
import states.MenuState;
import states.PlayingState;

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

    public boolean isEmpty() {
        return states.isEmpty();
    }

    public void resetToMainMenu(GameWorld gameWorld) {
        // Pop states until the stack is empty or the top is already the main menu
        while (!states.isEmpty() && !(states.peek() instanceof MenuState)) {
            pop();
        }

        // If the stack is empty (no main menu), push it
        if (states.isEmpty()) {
            push(new MenuState(gameWorld));
        }
    }

    public GameState peekBelowTop() {
        if (states.size() < 2) return null; // nothing below top
        return states.get(states.size() - 2); // second-to-last element
    }

    public GameState peek() {
        return states.isEmpty() ? null : states.peek();
    }

}
