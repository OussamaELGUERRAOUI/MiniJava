package fr.n7.stl.block.ast.classContent;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.PartialType;
import fr.n7.stl.util.Pair;

import java.util.List;

import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.type.Type;

public class Signature implements Declaration {

    private Pair<String,PartialType> name;
    private Type type;
    private List<ParameterDeclaration> parameters;

    public Signature(Pair<String,PartialType> _name, Type _type, List<ParameterDeclaration> _parameters) {
        this.name = _name;
        this.type = _type;
        this.parameters = _parameters;
    }
    
    public Signature(Pair<String,PartialType> _name, Type _type) {
        this.name = _name;
        this.type = _type;
        this.parameters = null;
    }

    public String toString() {
        String res = this.type.toString() + " " + this.name.getLeft() + "(";
        for (ParameterDeclaration p : this.parameters) {
            res += p.toString() + ", ";
        }
        if (this.parameters.size() > 0) {
            res = res.substring(0, res.length() - 2);
        }
        res += ")";
        return res;
    }

    @Override
    public String getName() {
        return this.name.getLeft();
    }

    @Override
    public Type getType() {
        return this.type;
    }

    public List<ParameterDeclaration> getParameters() {
        return this.parameters;
    }
    
}
