package fr.n7.stl.block.ast.classes;

import java.util.List;

import fr.n7.stl.block.ast.classContent.ContentInterface;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class Interface implements ClasseInterface, Declaration {

    String name;
    List<ContentInterface> contents;

    public Interface(String _name, List<ContentInterface> _contents) {
        this.name = _name;
        this.contents = _contents;
    }

    public String toString() {
        String res = "Interface " + this.name + " {\n";
        for (ContentInterface c : this.contents) {
            res += c.toString() + "\n";
        }
        res += "}\n";
        return res;
    }

    public String getName() {
        return this.name;
    }

    public List<ContentInterface> getContents() {
        return this.contents;
    }

    public Type getType() {
        return null;
    }

    public boolean collect(HierarchicalScope<Declaration> _scope) {
        if (_scope.accepts(this)) {
            _scope.register(this);
            boolean res = true;
            for (ContentInterface c : this.contents) {
                res = res && c.collectAndBackwardResolve(_scope);
            }
            return true;
        } else {
            Logger.error("Error in NormalClass : scope can't accept this");
            return false;
        }
    }

    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        for (ContentInterface c : this.contents) {
            c.fullResolve(_scope);
        }
        return true;
    }
    
    public boolean checkType() {
        for (ContentInterface c : this.contents) {
            if (!c.checkType()) {
                return false;
            }
        }
        return true;
    }

    public int allocateMemory(Register _register, int _offset) {
        return 0;
    }

    public Fragment getCode(TAMFactory _factory) {
        return null;
    }    
}
