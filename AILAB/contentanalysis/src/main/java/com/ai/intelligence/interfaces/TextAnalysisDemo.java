package com.ai.intelligence.interfaces;

import com.ai.analysis.impl.NlpProcessImpl;
import com.ai.analysis.interfaces.INlpProcessPkgSV;

public class TextAnalysisDemo {
	public static void main(String[] args){
		INlpProcessPkgSV nlpSV = new NlpProcessImpl();
		String parseRes = nlpSV.wordParse("中华人民共和国");
		System.out.println(parseRes);
	}
}
