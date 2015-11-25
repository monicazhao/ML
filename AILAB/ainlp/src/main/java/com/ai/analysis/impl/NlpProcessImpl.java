package com.ai.analysis.impl;

/**
 * author: raynard.pan
 * date: 2015/11/05
 */

import java.util.List;

import com.ai.analysis.interfaces.ICLibraryPkgSV;
import com.ai.analysis.interfaces.INlpProcessPkgSV;
import com.ai.analysis.util.ConfigureParamRead;
import com.hankcs.hanlp.summary.TextRankSentence;

public class NlpProcessImpl implements INlpProcessPkgSV {
	public NlpProcessImpl(){
		super();
		NlpProcessImpl.nlpInit();
	}

	public String wordParse(String sInput) {
		String nativeBytes = null;
		try {
			nativeBytes = ICLibraryPkgSV.Instance.NLPIR_ParagraphProcess(sInput, 1);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return nativeBytes;
	}

	public String keyWordExtractor(String sInput, int count) {

		String nativeBytes = null;
		try{
			nativeBytes = ICLibraryPkgSV.Instance.NLPIR_GetKeyWords(sInput, count, true);
		}catch(Exception e){
			e.printStackTrace();
		}
		return nativeBytes;
	}

	private static boolean nlpInit() {
		String argu = ConfigureParamRead.getValue("nlpir_root_path");
		int charset_type = 1 ;
		
		int init_flag = ICLibraryPkgSV.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;
		if (0 == init_flag) {
			nativeBytes = ICLibraryPkgSV.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			return false;
		}
		return true;
	}

	public void nlpExit() {
		ICLibraryPkgSV.Instance.NLPIR_Exit();
	}
	
	public List<String> getSummery(String sInput, int count) {

		return TextRankSentence.getTopSentenceList(sInput, count);
	}

	
}
