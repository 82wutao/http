package com.ctqh.mobile.schedule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * <p>按顺序依次为<br>
 * 秒(0~59)<br>
 * 分(0~59)<br>
 * 时(0~23)<br>
 * 日(0~31，但是你需要考虑你月的天数)<br>
 * 月(0~11)<br>
 * 周(1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT)<br>
 * 年(1970－2099)</p>
 * <p>
 * 0 0 10,14,16 * * ? 每天上午10点，下午2点，4点<br>
 * 0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时<br>
 * 0 0 12 ? * WED 表示每个星期三中午12点<br>
 * 0 0 12 * * ?" 每天中午12点触发 <br>
 * 0 15 10 ? * *" 每天上午10:15触发 <br>
 * 0 15 10 * * ?" 每天上午10:15触发 <br>
 * 0 15 10 * * ? *" 每天上午10:15触发 <br>
 * 0 15 10 * * ? 2005" 2005年的每天上午10:15触发 <br>
 * 0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发 <br>
 * 0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发 <br>
 * 0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发 <br>
 * 0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发 <br>
 * 0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发 <br>
 * 0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发 <br>
 * 0 15 10 15 * ?" 每月15日上午10:15触发 <br>
 * 0 15 10 L * ?" 每月最后一日的上午10:15触发 <br>
 * 0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发 <br>
 * 0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发 <br>
 * 0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发 
</p>
 * @author wutao
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QuartzJobAnn {

	public String cronExpression();
	public String jobName();
}