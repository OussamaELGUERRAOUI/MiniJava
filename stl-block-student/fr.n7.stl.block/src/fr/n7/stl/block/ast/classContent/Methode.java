package fr.n7.stl.block.ast.classContent;

import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;


public class Methode implements ContentInterface {

    AccessRight visibility;
    State state;
    Signature signature;
    Block body;

    public Methode(AccessRight _visibility, State _state, Signature _signature, Block _body) {
        this.visibility = _visibility;
        this.state = _state;
        this.signature = _signature;
        this.body = _body;
    }

    public String toString() {
        return this.visibility.toString() + " " + this.state.toString() + " " + this.signature + " " + this.body;
    }

    public String getName() {
        return this.signature.getName();
    }

    public Type getType() {
        return this.signature.getType();
    }

    public AccessRight getVisibility() {
        return this.visibility;
    }

    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        if (_scope.accepts(this)) {
            _scope.register(this);
            SymbolTable st = new SymbolTable(_scope);
            if (this.signature.getParameters() != null) {
                for (ParameterDeclaration p : this.signature.getParameters()) {
                    st.register(p);
                }
            }
            boolean res = this.body.collect(st);
            return res;
        } else {
            Logger.error("Error in Methode : scope can't accept this");
            return false;
        }
    }

    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        SymbolTable.methode = this;
        return this.body.resolve(_scope);
    }

    public boolean checkType() {
        SymbolTable.methode = this;
        boolean res = this.body.checkType();
        if (res) {
            for (ParameterDeclaration p : this.signature.getParameters()) {
                res = res && (!p.getType().equalsTo(AtomicType.ErrorType));
                if (!res) {
                    Logger.error("Error in Methode : parameter type error");
                    return false;
                }
            }
            return res;
        } else {
            Logger.error("Error in Methode : body checkType error");
            return false;
        }
    }

    public int allocateMemory(Register _register, int _offset) {
        int offset = 0;
        for (ParameterDeclaration p : this.signature.getParameters()) {
            offset += p.getType().length();
        }
        this.body.allocateMemory(Register.LB, offset);
        return 0;
    }

    public Fragment getCode(TAMFactory _factory) {
        Fragment fragment = _factory.createFragment();
        fragment.append(this.body.getCode(_factory));
        if (this.signature.getType().equalsTo(AtomicType.VoidType)) {
            fragment.add(_factory.createReturn(0, 0));
        }
        return fragment;
    }
}
