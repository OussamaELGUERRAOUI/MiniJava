package fr.n7.stl.block.ast.classContent;

import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import java.util.List;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.Block;
import fr.n7.stl.util.Logger;

public class Main implements  {
    
    private List<ParameterDeclaration> parameters;
    private Block body;

    public Main(List<ParameterDeclaration> _parameters, Block _body) {
        this.parameters = _parameters;
        this.body = _body;
    }

    public String toString() {
        String res = "public static void main(";
        for (ParameterDeclaration p : this.parameters) {
            res += p.toString() + ", ";
        }
        if (this.parameters.size() > 0) {
            res = res.substring(0, res.length() - 2);
        }
        res += ") {\n";
        res += this.body.toString();
        res += "}\n";
        return res;
    }

    

    

    
}
