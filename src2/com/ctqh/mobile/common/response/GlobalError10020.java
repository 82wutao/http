package com.ctqh.mobile.common.response;

import com.ctqh.mobile.common.protocol.MessageOutput;

public class GlobalError10020 extends MessageOutput{

	public GlobalError10020(int msgKey,String... params) {
		super(10020);
		writeShort(msgKey);		

		if (params==null
				||params.length==0) {
			writeShort(0);	
			return ;
		}
		writeShort(params.length);	
		for(String param:params){
			writeUTF(param);
		}
	}
}
