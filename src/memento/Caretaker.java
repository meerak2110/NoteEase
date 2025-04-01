package memento;

import java.util.Stack;

public class Caretaker {
    private Stack<Memento> mementoStack = new Stack<>();
    
    public void addMemento(Memento memento) {
        mementoStack.push(memento);
    }
    
    public Memento getMemento() {
        return mementoStack.isEmpty() ? null : mementoStack.pop();
    }
    
    // Get the stack for checking size
    public Stack<Memento> getStack() {
        return mementoStack;
    }
}