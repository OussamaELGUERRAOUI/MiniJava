package fr.n7.stl.block.ast.expression;

import java.util.List;

import fr.n7.stl.block.ast.classContent.AccessRight;
import fr.n7.stl.block.ast.classContent.Methode;
import fr.n7.stl.block.ast.classes.NormalClass;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.ObjectDecl;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AbstractMethod implements Expression {
    String name;
    List<Expression> arguments;
    Methode methode;
    Expression record;

    public AbstractMethod(String name, List<Expression> arguments, Expression record) {
        this.name = name;
        this.arguments = arguments;
        this.record = record;
    }

    public String toString() {
        String res = this.record.toString() + "." + this.name + "(";
        for (Expression arg : this.arguments) {
            res += arg.toString() + ", ";
        }
        res += ")";
        return res;
    }

    public void setMethode(Methode methode) {
        this.methode = methode;
    }

    public Methode getMethode() {
        return this.methode;
    }

    public String getName() {
        return this.name;
    }

    public List<Expression> getArguments() {
        return this.arguments;
    }

    public Expression getRecord() {
        return this.record;
    }

    public void setRecord(Expression record) {
        this.record = record;
    }

    @Override
    public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
        boolean res = true;
        boolean trouve = false;
        if (((HierarchicalScope<Declaration>) _scope).knows(this.record.toString())) {
			Declaration dec = _scope.get(this.record.toString());

			if (dec instanceof VariableDeclaration) {
                Type _type = ((VariableDeclaration) dec).getType();

		        if (_type instanceof ObjectDecl) {
			        Declaration d = _scope.get(_type.toString());

			        if (d instanceof NormalClass) {
                        List<Methode> methods = ((NormalClass) d).getMethods();
                        
                        for (Methode m : methods) {
                            if (this.name.equals(m.getName())) {
                                if (m.getVisibility().equals(AccessRight.Private)) {
							        Logger.error("The method " + this.name + " is private !");
							        return false;
						        } else {
                                    trouve = true;
                                    this.methode = m;
                                    if (this.arguments != null) {
                                        for (Expression expression : this.arguments) {
                                            res = res && expression.collectAndBackwardResolve(_scope);
                                        }
                                    }
                                    res = res && !m.getType().equals(AtomicType.VoidType);
                                }
                            }
                        }

                        if (trouve) {return res;} else {Logger.error("The method " + this.name + " doesn't exist."); return false;}   
                    } else {
                        Logger.error("The identifier " + this.record + " is not a class.");
                        return false;
                    }
                } else {
                    Logger.error("The identifier " + this.record + " is not a class.");
                    return false;
                }     
            } else {
                Logger.error("The identifier " + this.record + " is not a class.");
                return false;
            }        
	    } else {
		    Logger.error("The identifier " + this.record + " is not declared.");
			return false;
		}
    }

    @Override
    public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
        boolean res = true;
        boolean trouve = false;
        if (((HierarchicalScope<Declaration>) _scope).knows(this.record.toString())) {
			Declaration dec = _scope.get(this.record.toString());

			if (dec instanceof VariableDeclaration) {
                Type _type = ((VariableDeclaration) dec).getType();

		        if (_type instanceof ObjectDecl) {
			        Declaration d = _scope.get(_type.toString());

			        if (d instanceof NormalClass) {
                        List<Methode> methods = ((NormalClass) d).getMethods();
                        
                        for (Methode m : methods) {
                            if (this.name.equals(m.getName())) {
                                if (m.getVisibility().equals(AccessRight.Private)) {
							        Logger.error("The method " + this.name + " is private !");
							        return false;
						        } else {
                                    trouve = true;
                                    this.methode = m;
                                    if (this.arguments != null) {
                                        for (Expression expression : this.arguments) {
                                            res = res && expression.fullResolve(_scope);
                                        }
                                    }
                                    res = res && !m.getType().equals(AtomicType.VoidType);
                                }
                            }
                        }

                        if (trouve) {return res;} else {Logger.error("The method " + this.name + " doesn't exist."); return false;}   
                    } else {
                        Logger.error("The identifier " + this.record + " is not a class.");
                        return false;
                    }
                } else {
                    Logger.error("The identifier " + this.record + " is not a class.");
                    return false;
                }     
            } else {
                Logger.error("The identifier " + this.record + " is not a class.");
                return false;
            }        
	    } else {
		    Logger.error("The identifier " + this.record + " is not declared.");
			return false;
		}
    }
    
    @Override
    public Type getType() {
        return this.methode.getType();
    }

    @Override
    public Fragment getCode(TAMFactory _factory) {
        Fragment res = this.record.getCode(_factory);
        for (Expression arg : this.arguments) {
            res.append(arg.getCode(_factory));
        }
        return res;
    }
}
