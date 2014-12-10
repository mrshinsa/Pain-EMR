package com.klee.painemr;

/**
 * Created by klee on 11/5/2014.
 */
public class PollSelectionEvent {
    int index;
    int selection;


    public PollSelectionEvent(int index, int selection) {
        this.index = index;
        this.selection = selection;
    }

    public int getIndex() {
        return this.index;
    }

    public int getSelection() {
        return this.selection;
    }
}
