package fr.n7.stl.block.ast.classContent;

public enum State {

    Static,
    Final,
    StaticFinal,
    None;

    public String toString() {
        switch (this) {
            case Static:
                return "static";
            case Final:
                return "final";
            case StaticFinal:
                return "static final";
            case None:
                return "";
            default:
                throw new IllegalArgumentException("Invalid state type");
        }

    }
    
}
