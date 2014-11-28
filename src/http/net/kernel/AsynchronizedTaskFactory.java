package http.net.kernel;

import http.memories.PoolableObjectFactory;
/**
 * AsynchronizedTask 工厂。负责AsynchronizedTask的实例化。
 * @author wt
 *
 */
public class AsynchronizedTaskFactory extends
		PoolableObjectFactory<AsynchronizedTask> {
	
	@Override
	public AsynchronizedTask newObject() {
		return new AsynchronizedTask();
	}
	
}
