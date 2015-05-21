package com.ctqh.mobile.patch;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p>
 * 这里是打补丁的用户接口。jsp页面调用这个。使用urlclassloader获得新的算法实例
 * </p>
 * <p>打补丁时，对准备修复的类派生一个子类，子类中对有错误的函数重写。
 * 再添加一个PatchNames类，这个类的getPatchNames函数描述了即将被修复的文件。模板如下：</p>
 * <p>
 * public class PatchNames {<br>
 * &nbsp;&nbsp;public PatchNames() {}<br>
 * 
 * &nbsp;&nbsp;public String[] getPatchNames() {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;return new String[] {"net.workroom.ai.move.newimpl.MoveCtrl:net.workroom.patchs.PatchMoveCtrl" };<br>
 * &nbsp;&nbsp;}<br>
 * 
 * }<br>
 * </p>
 * @author user
 * 
 */
public class PatchHandle {
	private PatchHandle() {
	}

	private static PatchHandle obj = new PatchHandle();

	public static PatchHandle getPatchHandle() {
		return obj;
	}

	public Object loadClass(java.lang.String jarUrl) throws Exception {
		URL[] urls = new URL[1];
		urls[0] = new URL(jarUrl);

		URLClassLoader classLoader = new URLClassLoader(urls, this.getClass()
				.getClassLoader());
		Object patchInterface = loadObject(classLoader,
				"com.ctqh.mobile.patch.PatchNames");
		Class<?> PatchNamesClass = patchInterface.getClass();
		Method names = PatchNamesClass.getDeclaredMethod("getPatchNames",
				new Class[] {});
		Object retObject = names.invoke(patchInterface, new Object[] {});
		String[] patchNames = (String[]) retObject;// patchs.getPatchNames();
		
		for (String patch : patchNames) {
			String[] patchPair = patch.split(":");
			Object patchObject = loadObject(classLoader, patchPair[1]);
			Class<?> runtimeClass = Class.forName(patchPair[0]);
			Method setter = runtimeClass.getMethod("setInstance", runtimeClass);
			setter.invoke(null, patchObject);
		}
		return null;
	}

	private Object loadObject(ClassLoader loader, String className)
			throws Exception {
		Class<?> clazz = loader.loadClass(className);
		Object instance = clazz.newInstance();
		return instance;
	}

}
