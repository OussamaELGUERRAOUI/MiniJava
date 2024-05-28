package fr.n7.stl.block.ast.classContent;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import java_cup.runtime.Symbol;

import java.util.List;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.util.Logger;


public class Constructeur implements ContentInterface {

    private String name;
    private List<ParameterDeclaration> parameters;
    private Block body;
    private SymbolTable table;
    private int offset;


    public Constructeur(String _name, List<ParameterDeclaration> _parameters, Block _body) {
        this.name = _name;
        this.parameters = _parameters;
        this.body = _body;
    }

    public String toString() {
        String res = "public " + this.name + "(";
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

    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        if(_scope.accepts(this)){
            _scope.register(this);
            SymbolTable _local = new SymbolTable(_scope);
            for (ParameterDeclaration p : this.parameters) {
                _local.register(p);
            }
            this.table = _local;
            return this.body.collect(_local);

        } else {
            Logger.error("The scope does not accept the constructeur");
            return false;

        }
        

    }

    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        if (this.body.resolve(this.table)) {
            return true;
        } else {
            Logger.error("The constructeur could not be resolved");
            return false;
        }
       
    }

    @Override
    public boolean checkType() {
        for (ParameterDeclaration p : this.parameters) {
            if(p.getType().equals(AtomicType.ErrorType)) {
                Logger.error("Error : Constructeur " + this.name + " parameter type not resolved");
                return false;
            }

        }
        return this.body.checkType();

    }

    @Override
    public int allocateMemory(Register _register, int _offset) {
        this.offset = _offset;
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment fragment = _factory.createFragment();
        fragment.append(this.body.getCode(_factory));
        fragment.add(_factory.createReturn(0, 0));
        return fragment;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Type getType() {
        return null;
    }

    
}
