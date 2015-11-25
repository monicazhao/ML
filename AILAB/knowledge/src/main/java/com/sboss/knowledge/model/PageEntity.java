package com.sboss.knowledge.model;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 分页信息，分页信息中包含了若干个AbstaractEntity，以及当前的页码信息
 * @author wenjie
 */
public class PageEntity extends AbstractEntity {
	
	/**
	 * 日志
	 */
	private static final Log LOGGER = LogFactory.getLog(PageEntity.class);
	
	/**
	 * 所有符合数据的总的数据量
	 */
	private int maxCount;

	/**
	 * 所有符合数据的总的页码数量
	 */
	private int maxPage;

	/**
	 * 当前的页面，索引号从0开始
	 */
	private int nowPage;

	/**
	 * 用开发人员设置的，每页最大的数据量，默认为20条
	 */
	private int maxPageRows = 20;
	
	/**
	 * 得到的查询数据
	 */
	private List<? extends AbstractEntity> results = new LinkedList<AbstractEntity>();
	
	/**
	 * 专门为数组形式的查询结果准备的记录信息（可能使用queryBySqlFile就会返回这样的信息）
	 */
	private List<Object[]> resultsByObject = new LinkedList<Object[]>();

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getMaxPageRows() {
		return maxPageRows;
	}

	public void setMaxPageRows(int maxPageRows) {
		this.maxPageRows = maxPageRows;
	}

	public List<? extends AbstractEntity> getResults() {
		return results;
	}

	public void setResults(List<? extends AbstractEntity> results) {
		this.results = results;
	}

	public List<Object[]> getResultsByObject() {
		return resultsByObject;
	}

	public void setResultsByObject(List<Object[]> resultsByObject) {
		this.resultsByObject = resultsByObject;
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
		String rts = "";
		List<String[]> data = new ArrayList<String[]>();

		if (!getResults().isEmpty()) {
			try {
				Field[] fds = getResults().get(0).getClass().getDeclaredFields();
				for (Object ae : getResults()) {
					String[] ss = new String[fds.length];
					int j = 0;
					for (Field fd : fds) {
						fd.setAccessible(true);
						if (StringUtils.equals(fd.getType().getName(), String.class.getName())) {
							ss[j++] = fd.get(ae).toString();
						} else if (StringUtils.equals(fd.getType().getName(), Date.class.getName())) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							ss[j++] = sdf.format((Date) fd.get(ae));
						} else if (StringUtils.equals(fd.getType().getName(), Integer.class.getName())) {
							ss[j++] = String.valueOf(fd.get(ae));
						} else if (StringUtils.equals(fd.getType().getName(), Double.class.getName())) {
							ss[j++] = String.valueOf(fd.get(ae));
						} else {
							ss[j++] = "";
						}
					}
					data.add(ss);
				}
			} catch (IllegalAccessException e) {
				PageEntity.LOGGER.warn("转换过程出现问题，请根据警告信息进行检查", e);
				return rts;
			}
		} else if (!getResultsByObject().isEmpty()) {
			for (Object[] obj : getResultsByObject()) {
				String[] ss = new String[obj.length];
				int j = 0;
				for (Object oc : obj) {
					ss[j++] = oc.toString();
				}
				data.add(ss);
			}
		}

		return rts;
	}
}