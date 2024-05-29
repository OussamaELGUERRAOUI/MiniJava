/**
 * 
 */
package fr.n7.stl.block.ast.type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a function type.
 * @author Marc Pantel
 *
 */
public class FunctionType implements Type {

	private Type result;
	private List<Type> parameters;

	public FunctionType(Type _result, Iterable<Type> _parameters) {
		this.result = _result;
		this.parameters = new LinkedList<Type>();
		for (Type _type : _parameters) {
			this.parameters.add(_type);
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		boolean _res = true;
		if (_other instanceof FunctionType) {
			if (((FunctionType) _other).result.equalsTo(this.result)) {
				if (((FunctionType) _other).parameters.size() == this.parameters.size()) {
					for (int i = 0; i < this.parameters.size(); i++) {
						_res = _res && this.parameters.get(i).equalsTo(((FunctionType) _other).parameters.get(i));
					}
				} else {
					_res = false;
					Logger.error("FunctionType has not the same number of parameters.");
				}
			} else {
				_res = false;
				Logger.error("FunctionType has not the same result type.");
			}
		} else {
			_res = false;
			Logger.error("FunctionType is not an instance of FunctionType.");
		}
		return _res;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		
		boolean _res = true;
		if (((FunctionType) _other).result.compatibleWith(this.result)) {
			if (((FunctionType) _other).parameters.size() == this.parameters.size()) {
				for (int i = 0; i < this.parameters.size(); i++) {
					_res = _res && this.parameters.get(i).compatibleWith(((FunctionType) _other).parameters.get(i));
				}
			} else {
				Logger.error("FunctionType has not the same number of parameters.");
				_res = false;
			}
		} else {
			Logger.error("FunctionType has not the same result type.");
			_res = false;
		}
		return _res;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (_other instanceof FunctionType) {
			return this;
		} else {
			Logger.error("FunctionType is not an instance of FunctionType.");
			return AtomicType.ErrorType;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		return this.result.length();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = "(";
		Iterator<Type> _iter = this.parameters.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " ," + _iter.next();
			}
		}
		return _result + ") -> " + this.result;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean _result = this.result.resolve(_scope);
		for (Type _type : this.parameters) {
			_result = _result && _type.resolve(_scope);
		}
		return _result;
	}

}
