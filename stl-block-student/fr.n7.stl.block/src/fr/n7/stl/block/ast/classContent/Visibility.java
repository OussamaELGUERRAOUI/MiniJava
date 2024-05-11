package fr.n7.stl.block.ast.classContent;

public enum Visibility {
    Public,
    Private,
    Protected;

    public String toString() {
        switch (this) {
            case Public:
                return "public";
            case Private:
                return "private";
            case Protected:
                return "protected";
            default:
                throw new IllegalArgumentException("Invalid visibility type");
        }

    }
}