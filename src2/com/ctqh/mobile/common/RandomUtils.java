package com.ctqh.mobile.common;

import java.util.Collection;
import java.util.Random;

/**
 * 随机工具
 * 
 * @author lizhiwei
 * 
 */
public class RandomUtils {

	private static RandomUtils instance;

	public static RandomUtils getInstance() {
		if (instance == null) {
			instance = new RandomUtils();
		}
		return instance;
	}

	/**
	 * 计算概率
	 * 
	 * @param r
	 * @return 产生的结果的下标
	 */
	public int computeChance(int r[]) {
		int randomCount = 0;
		for (int i = 0; i < r.length; i++) {
			randomCount += r[i];
		}
		int ra = randomIntValue(1, randomCount);
		for (int i = 0; i < r.length; i++) {
			if (ra <= r[i]) {
				return i;
			} else {
				ra = ra - r[i];
			}
		}
		return r.length - 1;
	}
	/***
	 * 通过概率投影，随机出一个对象
	 * @param collection 一组对象概率对
	 * @return
	 */
	public <T> KeyValue<T,Integer> randomFromCollection(Collection<KeyValue<T,Integer>> collection){
		int scop =0;
		for (KeyValue<T,Integer> kv:collection) {
			scop += kv.value;
		}
		
		int ra = randomIntValue(1,scop);
		for (KeyValue<T,Integer> kv:collection) {
			if (ra <= kv.value) {
				return kv;
			} else {
				ra = ra - kv.value;
			}
		}
		return null;
	}
	

	/**
	 * 计算概率<br>
	 * 此方法会把r中的元素扩大100倍，然后强转成整形，所以使用时，应当注意，在扩大100倍后不要超过整数的最大值<br>
	 * 另外，r的小数点后应该使用2位，如果使用3位，会把第三位舍去
	 * 
	 * @param r
	 * @return 产生的结果的下标
	 */
	public int computeChanceFloat(float r[]) {
		return computeChanceFloat(r, 100);
	}

	/**
	 * 计算概率<br>
	 * 此方法会把r中的元素扩大beishu倍，然后强转成整形，所以使用时，应当注意，在扩大beishu倍后不要超过整数的最大值<br>
	 * 
	 * @param r
	 * @param beishu
	 * @return 产生的结果的下标
	 */
	public int computeChanceFloat(float r[], int beishu) {
		int ran[] = new int[r.length];
		for (int i = 0; i < ran.length; i++) {
			ran[i] = (int) r[i] * beishu;
		}
		return computeChance(ran);
	}

	/**
	 * 计算概率
	 * 
	 * @param r
	 * @return 产生的结果的下标
	 */
	public int computeChance(Integer r[]) {
		int ran[] = new int[r.length];
		for (int i = 0; i < ran.length; i++) {
			ran[i] = r[i];
		}
		return computeChance(ran);
	}

	/**
	 * 从all中得到size个不重复的元素<br>
	 * 例如，假设有十个刷新复活点可以用，但是必须只能选择五个。第一个物体被选择的概率将是5/ 10或0.5。<br>
	 * 如果选中第一个之后，第二个的概率为4 /9或0.44（即，四个物体仍然需要从剩下的九个选择）。<br>
	 * 然而，如果第一个没有选中，第二个选中的概率为5/ 9（即5/ 9或0.56，仍然需要从剩下的九个中选择）。这一直持续到要求的5个都被选上。
	 * 
	 * @param all
	 * @param size
	 * @return
	 */
	public int[] getRandomSubArray(int[] all, int size) {
		int[] result = new int[size];
		int sizeTemp = size;
		for (int i = all.length; i > 0; i--) {
			float prob = (float) sizeTemp / (float) i;
			if (randomFloatValue(0, 1) <= prob) {
				sizeTemp--;
				result[sizeTemp] = all[i - 1];
				if (sizeTemp == 0) {
					break;
				}
			}
		}
		return result;
	}


	/**
	 * 随机产生min到max之间的整数值 包括min max
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomIntValue(int min, int max) {
		return (int) (Math.random() * (double) (max - min + 1)) + min;
	}

	public static float randomFloatValue(float min, float max) {
		return (float) (Math.random() * (double) (max - min)) + min;
	}


	/**
	 * 根据给定的数据随机这些数字
	 * 
	 * @param mn
	 * @return
	 */

	public static int randomValue(int... mn) {
		Random random = new Random();
		int v = random.nextInt(mn.length);
		return mn[v];
	}
	public static int randomInt(int range) {
		Random random = new Random();
		return random.nextInt(range);
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 11; i++) {
			System.out.println(new Random().nextInt(2));
		}
	}
}
