package com.ai.analysis.interfaces;

/**
 * author: raynard.pan
 * date: 2015/11/05
 */
import com.ai.analysis.util.ConfigureParamRead;
import com.sun.jna.Native;
import com.sun.jna.Library;

public interface ICLibraryPkgSV extends Library {
	// 定义并初始化接口的静态变量
	String dataPath = ConfigureParamRead.getValue("dll_or_so_path");

	ICLibraryPkgSV Instance = (ICLibraryPkgSV) Native.loadLibrary(dataPath,
			ICLibraryPkgSV.class);

	public int NLPIR_Init(String sDataPath, int encoding, String sLicenceCode);

	public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

	public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
			boolean bWeightOut);

	public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
			boolean bWeightOut);

	public int NLPIR_AddUserWord(String sWord);// add by qp 2008.11.10

	public int NLPIR_DelUsrWord(String sWord);// add by qp 2008.11.10

	public String NLPIR_GetLastErrorMsg();

	public void NLPIR_Exit();
}
