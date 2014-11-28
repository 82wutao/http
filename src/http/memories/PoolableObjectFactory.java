package http.memories;

public abstract class PoolableObjectFactory<T extends PoolableObject> {
	public abstract T newObject();

	public void freeObject(T t){
		t.free();
	}
}
