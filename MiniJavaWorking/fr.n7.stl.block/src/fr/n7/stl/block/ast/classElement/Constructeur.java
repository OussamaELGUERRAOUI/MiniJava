package fr.n7.stl.block.ast.classElement;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import java_cup.runtime.Symbol;

import java.util.Iterator;
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
    public Constructeur(String _name, Block _body) {
        this.name = _name;
        this.parameters = null;
        this.body = _body;
    }

    public String toString() {
        String res = "public " + this.name + "(";
        if (parameters != null) {
            Iterator<ParameterDeclaration> _iter = this.parameters.iterator();
            if (_iter.hasNext()) {
                res += _iter.next();
                while (_iter.hasNext()) {
                    res += " ," + _iter.next();
                }
            }
        }
<<<<<<< HEAD:MiniJavaWorking/fr.n7.stl.block/src/fr/n7/stl/block/ast/classElement/Constructeur.java
        if ( body != null) {
        	return res + ") " + body;
        } else {return res + ") {}\n";}
=======
        if ( corps != null) {
        	return _result + ") " + corps;
        } else {return _result + ") {}\n";}
    }

    public List<ParameterDeclaration> getParameters() {
        return this.parameters;
>>>>>>> 118ba138d7bb44290bdffbc3fe092c4a43c0c485:MiniJavaWorking/fr.n7.stl.block/src/fr/n7/stl/block/ast/classElement/ConstructorDeclaration.java
    }

    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        
        if (this.parameters != null) {
            for (ParameterDeclaration p : this.parameters) {
                this.name += p.getType();
            }
        }
        if (!(this.name.contains(SymbolTable.classDeclaration.getName()))) {
            Logger.error("The constructor identifier " + this.name + " is not the same as the class identifier.");
            return false;
        }
        
        if (((HierarchicalScope<Declaration>) _scope).accepts(this)) {
            _scope.register(this);
            SymbolTable tableParametres = new SymbolTable(_scope);
            boolean result = true;
            if (this.parameters != null) {
                for (ParameterDeclaration d : this.parameters) {
                    tableParametres.register(d);
                }
            }
            this.table = tableParametres;
            if (this.body != null) {
            	result = this.body.collect(tableParametres);
            } else {
            	result = true;
            }
            return result;
        } else {
            Logger.error("error: The constructor " + this.name + " is already defined in the class.");
            return false;
        }
    
    }
        

    

    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        if (this.body.resolve(this.table)) {
            return true;
        } else {
            Logger.error("error: The constructor " + this.name + " is not resolved.");
            return false;
        }
       
    }

    @Override
    public boolean checkType() {
        
        
        if(this.parameters != null) {
            for (ParameterDeclaration p : this.parameters) {
                if(p.getType().equals(AtomicType.ErrorType)) {
                    Logger.error("error: The parameter " + p.getName() + " has an error type.");
                    return false;
                    
                }
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
