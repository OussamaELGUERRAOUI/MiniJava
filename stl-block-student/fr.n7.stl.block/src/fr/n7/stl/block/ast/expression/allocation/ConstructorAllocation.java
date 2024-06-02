package fr.n7.stl.block.ast.expression.allocation;

import java.util.List;

import fr.n7.stl.block.ast.classContent.Constructeur;
import fr.n7.stl.block.ast.classes.NormalClass;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class ConstructorAllocation implements Expression {

    Type className;
    List<Expression> arguments;

    public ConstructorAllocation(Type _className, List<Expression> _arguments) {
        this.className = _className;
        this.arguments = _arguments;
    }

    public String toString() {
        String res = "new " + this.className + "(";
        for (Expression arg : this.arguments) {
            res += arg.toString() + ", ";
        }
        res += ")";
        return res;
    }

    public List<Expression> getArguments() {
        return this.arguments;
    }

    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        boolean res = true;
        boolean trouve = false;
        Declaration d = _scope.get(this.className.toString());
        if (d instanceof NormalClass) {
            for (Constructeur c : ((NormalClass) d).getConstructors()) {
                if (this.className.toString().equals(c.getName())) {
                    trouve = true;
                    if (this.arguments != null) {
                        for (Expression exp : this.arguments) {
                            res = res && exp.collectAndBackwardResolve(_scope);
                        }
                    }
                }
            }
            if (trouve) {return res;} else {Logger.error("The constructor " + this.className.toString() + " is not defined."); return false;}
        } else {
            Logger.error("The class " + this.className.toString() + " is not defined.");
            return false;
        }
    }
    
    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        boolean res = true;
        boolean trouve = false;
        Declaration d = _scope.get(this.className.toString());
        if (d instanceof NormalClass) {
            for (Constructeur c : ((NormalClass) d).getConstructors()) {
                if (this.className.toString().equals(c.getName())) {
                    trouve = true;
                    if (this.arguments != null) {
                        for (Expression expression : this.arguments) {
                            res = res && expression.fullResolve(_scope);
                        }
                    }
                }
            }
            if (trouve) {return res;} else {Logger.error("The constructor " + this.className.toString() + " is not defined."); return false;}
        } else {
            Logger.error("The class " + this.className.toString() + " is not defined.");
            return false;
        }
    }

    @Override
    public Type getType() {
        return this.className;
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment res = _factory.createFragment();
        res.add(_factory.createLoadL(1));
        //res.add(TAMFactory.createMalloc());
        if (this.arguments != null) {
            for (Expression _parameter : this.arguments) {
                res.append(_parameter.getCode(_factory));
            }
        }
		res.add(_factory.createCall("BEGIN:" + this.className.toString(), Register.SB));
        
        return res;
    }

}
