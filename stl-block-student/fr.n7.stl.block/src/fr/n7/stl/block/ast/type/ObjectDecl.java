package fr.n7.stl.block.ast.type;

import java.util.List;

import fr.n7.stl.block.ast.classContent.*;
import fr.n7.stl.block.ast.classes.*;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.Scope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.util.Logger;

public class ObjectDecl implements Type, Scope<ContentInterface>  {
    NormalClass classe;

    public ObjectDecl(NormalClass _classe) {
        this.classe = _classe;
    }

    public String toString() {
        return "new " + this.classe.getName() + "()";
    }

    public NormalClass getClasse() {
        return this.classe;
    }

    public void setClasse(NormalClass _classe) {
        this.classe = _classe;
    }

    public List<Attribut> getAttributs() {
        return this.classe.getAttributs();
    }

    public List<Methode> getMethods() {
        return this.classe.getMethods();
    }

    public List<Constructeur> getConstructors() {
        return this.classe.getConstructors();
    }

    public boolean equalsTo(Type _other) {
        boolean res = true;
        if (_other instanceof ObjectDecl) {
            for (NormalClass c : SymbolTable.classes) {
                if (c.getName().equals(this.classe.getName())) {
                    for (Attribut a : c.getAttributs()) {
                        for (Attribut b : ((ObjectDecl) _other).getAttributs()) {
                            if (a.getName().equals(b.getName()) && a.getType().equalsTo(b.getType())) {
                                res = true;
                            } else {
                                return false;
                            }
                        }
                    }
                    for (Methode m : c.getMethods()) {
                        for (Methode n : ((ObjectDecl) _other).getMethods()) {
                            if (m.getName().equals(n.getName()) && m.getType().equalsTo(n.getType())) {
                                res = true;
                            } else {
                                return false;
                            }
                        }
                    }
                    return res;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return res;
    }

    public boolean compatibleWith(Type _other) {
        return this.equalsTo(_other);
    }

    public Type merge(Type _other) {
        if (this.equalsTo(_other)) {
            return this;
        } else {
            Logger.error("Error in ObjectDecl : merge error");
            return AtomicType.ErrorType;
        }
    }

    public int length() {
        int res = 0;
        for (Attribut a : this.getAttributs()) {
            res += a.getType().length();
        }
        return res;
    }

    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        Declaration _declaration = _scope.get(this.classe.getName());
        if (_declaration instanceof NormalClass) {
            this.classe = (NormalClass) _declaration;
            return true;
        } else {
            Logger.error("Error in ObjectDecl : class not found");
            return false;
        }
    }

    public ContentInterface get(String _name) {
        for (Attribut a : this.getAttributs()) {
            if (a.getName().equals(_name)) {
                return a;
            }
        }
        for (Methode m : this.getMethods()) {
            if (m.getName().equals(_name)) {
                return m;
            }
        }
        for (Constructeur c : this.getConstructors()) {
            if (c.getName().equals(_name)) {
                return c;
            }
        }
        return null;
    }

    public boolean contains(String _name) {
        for (Attribut a : this.getAttributs()) {
            if (a.getName().equals(_name)) {
                return true;
            }
        }
        for (Methode m : this.getMethods()) {
            if (m.getName().equals(_name)) {
                return true;
            }
        }
        for (Constructeur c : this.getConstructors()) {
            if (c.getName().equals(_name)) {
                return true;
            }
        }
        return false;
    }

    public boolean accepts(ContentInterface _content) {
        return !this.contains(_content.getName());
    }

    public void register(ContentInterface _content) {
    }
}
