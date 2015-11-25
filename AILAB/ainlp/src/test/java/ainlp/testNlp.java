package ainlp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ai.analysis.impl.NlpProcessImpl;
import com.ai.analysis.interfaces.INlpProcessPkgSV;

public class testNlp {

	@Test
	public void test() {
		//fail("Not yet implemented");
		INlpProcessPkgSV nlpSV = new NlpProcessImpl();
		String parseRes = nlpSV.wordParse("中华人民共和国");
		System.out.println(parseRes);
		
		String sInput = "亚信十多年的创业、发展历程，书写了中国高科技企业崛起的经典传奇。作为中国归国留学人员科技报国的重要成果，亚信曾率先将Internet引入中国，为中国信息化建设作出卓越贡献，享有中国互联网建筑师的美誉。亚信首开中国企业吸纳风险投资之先河，率先引入国际化、规范的公司治理和管理制度。亚信成功实现向软件与服务的转型，立足于自主开发软件产品，形成大型软件研发和质量控制体系以及电信级IT项目实施管理体系，成为中国软件行业的重点企业。亚信一贯注重人才培养，凝聚着一批具有国际化背景和本地化经验的优秀人才，被称为中国IT业的黄埔军校。"+
"激情创业\n"+"1993年，田溯宁、丁健等留美学生在美国创建了Internet公司亚信。1995年，胸怀科技报国理想，田溯宁、丁健率公司主体回国，立志把Internet带回家、为中国做事，做中国最好的企业，亚信科技（中国）有限公司正式成立，先后承建了包括中国电信ChinaNet、中国联通CUNet、中国移动CMNet、中国网通CNCNet等六大全国骨干网工程在内的近千项大型互联网项目。1997年-1999年，亚信成为中国最早引入风险投资的高科技企业，先后获得风险投资和战略投资4300万美元。风险资金的引入同时带来了规范的企业管理制度和体系，为公司长远发展奠定了科学的治理结构。2000年，亚信公司在美国NASDAQ成功上市，成功融资1.2亿美元，成为第一家在NASDAQ上市的中国高科技公司。"+
"理性转型\n"+"1998年，亚信前瞻性地看到，随着硬件网络大发展已迈过巅峰，中国信息化建设亟需本土化应用，亚信前瞻性地开始大力发展软件业务，并通过收购杭州德康公司，正式进军软件。2002年，亚信全资收购广州邦讯科技有限公司，一举成为国内最大的通信软件和方案提供商。多元化挑战\n"+"2003年-2005年初，亚信公司先后收购太平洋软件公司核心HRM&BI业务、联想集团IT服务业务资产，围绕软件和服务核心业务，经历了向非电信行业多元化发展的尝试与努力，进一步扩大了亚信的软件与服务的经验与能力。稳步提升\n"+"2005年以来，亚信公司提出集中资源，专注核心业务，提升盈利能力的战略，在收缩非核心业务的同时，战略性地收购了上海长江科技、浙大兰德、上海亿软、北京国创科技等公司相关电信支撑业务，建立了在中国互联网软件、电信软件方案、安全软件与服务领域无可争议的领导者地位。立足中国良好的发展态势，努力实现业务创新，亚信正在迎来一个品牌、团队、业务与服务能力全面提升与创新的新时代。产品服务编辑\n"+
"亚信公司从1999年开始为客户定制业务系统，到目前为止已经服务于所有运营商，在亚信核心软件产品的基础上为用户度身定做大量的成熟业务系统解决方案，涉及运营商企业信息化的所有环节。";
		String sInput1 =         "目前国内从事算法研究的工程师不少，但是高级算法工程师却很少，是一个非常紧缺的专业工程师。算法工程师根据研究领域来分主要有音频/视频算法处理、图像技术方面的二维信息算法处理和通信物理层、雷达信号处理、生物医学信号处理等领域的一维信息算法处理。\n" +
					            "在计算机音视频和图形图像技术等二维信息算法处理方面目前比较先进的视频处理算法：机器视觉成为此类算法研究的核心；另外还有2D转3D算法(2D-to-3D conversion)，去隔行算法(de-interlacing)，运动估计运动补偿算法(Motion estimation/Motion Compensation)，去噪算法(Noise Reduction)，缩放算法(scaling)，锐化处理算法(Sharpness)，超分辨率算法(Super Resolution),手势识别(gesture recognition),人脸识别(face recognition)。\n" +
					            "在通信物理层等一维信息领域目前常用的算法：无线领域的RRM、RTT，传送领域的调制解调、信道均衡、信号检测、网络优化、信号分解等。\n" +
					            "另外数据挖掘、互联网搜索算法也成为当今的热门方向。\n" +
					            "算法工程师逐渐往人工智能方向发展。";

		System.out.println(nlpSV.getSummery(sInput1,3));
		System.out.println(nlpSV.keyWordExtractor(sInput1, 20));
		nlpSV.nlpExit();
	}
}
