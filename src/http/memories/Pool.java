package http.memories;

import java.util.LinkedList;
import java.util.Queue;

public class Pool<T extends PoolableObject> {
	private int init;
	private int max;
	private PoolableObjectFactory<T> factory;
	
	private int objectNum=0;
	private Queue<T> buffer = new LinkedList<T>();
	public Pool(int initSize, int maxSize, PoolableObjectFactory<T> factory) {
		init=initSize;
		max = maxSize;
		this.factory=factory;
		
		if (init==0) {
			return ;
		}
		
		for (int i = 0; i < init; i++) {
			T t =factory.newObject();
			buffer.offer(t);
		}
		objectNum=init;
	}
	
	public T allocObject(){
		T t =buffer.poll();
		if (t!=null) {
			return t;
		}
		
		if(max!=0
				&&max==objectNum){
			return  null;
		}
		
		int diff =max-objectNum;
		int plan = objectNum * 2;
		if (diff>=plan) {
			for(int i=0;i<plan;i++){
				T obj =factory.newObject();
				buffer.offer(obj);
			}
			objectNum = objectNum+plan;
		}else {
			for(int i=0;i<diff;i++){
				T obj =factory.newObject();
				buffer.offer(obj);
			}
			objectNum = objectNum+diff;
		}
		T t2 =buffer.poll();
		return t2;
	}
	public void freeObject(T object){
		factory.freeObject(object);
		buffer.offer(object);
	}
	
	public void destory(){
		for (T t:buffer) {
			factory.freeObject(t);
		}
		buffer.clear();
	}
}
