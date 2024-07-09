package it.unipd.dei.webapp.resource;

//enum of Difficulty: easy, medium, hard
public enum Difficulty {
    easy, medium, hard;

    public String toString() {
        return this.name().toLowerCase();
    }
}
