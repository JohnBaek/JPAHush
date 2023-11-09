package com.john.jpahush.data.commondata.responses;

import com.john.jpahush.utils.MoreExceptionHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 목록 및 목록에 대한 개수 및 페이징 정보를 가지는 클래스
 * @param <T> 데이터 타입
 */
@SuppressWarnings("unused")
@Getter
@Setter
public class QueryResults<T> implements IQueryResults<T> {

	/**
	 * 생성자
	 */
	public QueryResults() {
	}

	/**
	 * 생성자
	 * @param data 데이터 목록
	 */
	public QueryResults(List<T> data)
	{
		try
		{
			if(data != null)
				this.items.addAll(data);
			this.ResetWithItems();
		}
		catch(Exception e)
		{
			MoreExceptionHandler.Log(e);
		}
	}

	/**
	 * 생성자
	 * @param data 데이터 목록
	 * @param totalCount 전체 데이터 개수
	 * @param skip 건너뛸 레코드 수
	 * @param countPerPage 페이지 당 레코드 수
	 */
	public QueryResults(List<T> data, long totalCount, long skip, int countPerPage)
	{
		try
		{
			this.skips = skip;
			this.totalCount = totalCount;
			this.pageNo = (int)(skip / countPerPage) + 1;
			this.countPerPage = countPerPage;
			this.pagePerSection = (int)(totalCount / countPerPage) + (totalCount % countPerPage) == 0 ? 0 : 1;

			this.items.addAll(data);
		}
		catch(Exception e)
		{
			MoreExceptionHandler.Log(e);
		}
	}

	/**
	 * 생성자
	 * @param data 데이터 목록
	 * @param totalCount 전체 데이터 개수
	 * @param skip 건너뛸 레코드 수
	 * @param countPerPage 페이지 당 레코드 수
	 * @param pagePerSection 페이지 섹션 당 페이지 수
	 */
	public QueryResults(List<T> data, long totalCount, long skip, int countPerPage, int pagePerSection)
	{
		try
		{
			this.skips = skip;
			this.totalCount = totalCount;
			this.pageNo = (int)(skip / countPerPage) + 1;
			this.countPerPage = countPerPage;
			this.pagePerSection = pagePerSection;

			this.items.addAll(data);
		}
		catch(Exception e)
		{
			MoreExceptionHandler.Log(e);
		}
	}

	/**
	* 전체 레코드 개수
	*/
	@ApiModelProperty(value = "전체 레코드 개수", dataType = "long", required = true)
	private long totalCount= 0;

	/**
	* 건너뛴 레코드 수
	*/
	@ApiModelProperty(value = "건너뛴 레코드 수", dataType = "long", required = true)
	private long skips= 0;

	/**
	* 페이지 번호
	*/
	@ApiModelProperty(value = "페이지 번호", dataType = "int", required = true)
	private int pageNo= 1;


	/**
	* 페이지당 개수
	*/
	@ApiModelProperty(value = "페이지당 개수", dataType = "int", required = true)
	int countPerPage= 20;

	/**
	* 섹션당 페이지 수
	*/
	@ApiModelProperty(value = "섹션당 페이지 수", dataType = "int", required = true)
	int pagePerSection= 5;

	/**
	 * 전체 페이지 수를 반환한다.
	 */
	@ApiModelProperty(value = "전체 페이지 수", dataType = "int", required = true)
	int totalPage = 0;
	public int getTotalPage() {
		if(totalPage > 0) return totalPage;

		// 페이지당 레코드 수가 0인 경우 전체 데이터 (따라서 페이지 수는 1)
		if (totalCount == 0 || countPerPage <= 0)
			return 1;

		return (int) Math.ceil(totalCount / (double) countPerPage);
	}
	public void setTotalPage(int value) {
		this.totalPage = value;
	}

	/**
	 * 현재 섹션 내의 시작 페이지 번호를 반환한다.
	 */
	@ApiModelProperty(value = "현재 섹션 내의 시작 페이지 번호", dataType = "int", required = true)
	int startPageNo = 0;
	public int getStartPageNo() {
		if(startPageNo > 0) return startPageNo;

		// 레코드가 존재하지 않는 경우
		if (totalCount <= 0)
			return 1;

		// 섹션 당 페이지 수가 없는 경우
		if (pagePerSection <= 0)
			return 1;

		// 현재 섹션 번호
		int currentSection = pageNo / pagePerSection;
		if (pageNo % pagePerSection > 0)
			currentSection++;

		return (currentSection - 1) * pagePerSection + 1;
	}
	public void setStartPageNo(int value) {
		this.startPageNo = value;
	}

	/**
	 * 현재 섹션 내의 마지막 페이지 번호를 반환한다.
	 */
	@ApiModelProperty(value = "현재 섹션 내의 마지막 페이지 번호", dataType = "int", required = true)
	int endPageNo = 0;
	public int getEndPageNo() {

		if(endPageNo > 0) return endPageNo;

		// 레코드가 존재하지 않는 경우
		if (totalCount <= 0)
			return 1;

		// 페이지 당 레코드 수가 없는 경우
		if (countPerPage <= 0)
			return 1;

		int lastPageNo = (int)(totalCount / countPerPage);
		if (totalCount % countPerPage > 0)
			lastPageNo++;

		return Math.min(lastPageNo, this.getStartPageNo() + pagePerSection - 1);
	}
	public void setEndPageNo(int value) {
		this.endPageNo = value;
	}

	/**
	 * 표시될 페이지 번호 목록을 반환한다.
	 */
	@ApiModelProperty(value = "표시될 페이지 번호 목록", dataType = "java.util.List<Integer>", required = true)
	List<Integer> pageNos = null;
	public List<Integer> getPageNos() {

		if(pageNos != null && pageNos.size() > 0) return pageNos;

		List<Integer> result = new ArrayList<>();
		for (int pageNo = getStartPageNo(); pageNo <= getEndPageNo(); pageNo++)
			result.add(pageNo);
		return result;
	}
	public void setPageNos(List<Integer> value) {
		this.pageNos = value;
	}

	/**
	 * 이전 페이지가 존재하는지 여부를 반환한다.
	 */
	@ApiModelProperty(value = "이전 페이지가 존재하는지 여부", dataType = "boolean", required = true)
	boolean havePreviousPage = false;
	public boolean isHavePreviousPage() {

		return getTotalPage() > 1 && pageNo > 1;
	}
	public void setHavePreviousPage(boolean value) {
		this.havePreviousPage = value;
	}

	/**
	 * 다음 페이지가 존재하는지 여부를 반환한다.
	 */
	@ApiModelProperty(value = "다음 페이지가 존재하는지 여부", dataType = "boolean", required = true)
	boolean haveNextPage = false;
	public boolean isHaveNextPage() {
		return pageNo < getTotalPage();
	}
	public void setHaveNextPage(boolean value) {
		this.haveNextPage = value;
	}

	/**
	 * 첫번째 페이지 섹션인지 여부를 반환한다.
	 */
	@ApiModelProperty(value = "첫번째 페이지 섹션인지 여부", dataType = "boolean", required = true)
	boolean havePreviousPageSection = false;
	public boolean isHavePreviousPageSection() {
		return getTotalPage() > 1 && pageNo > pagePerSection;
	}
	public void setHavePreviousPageSection(boolean value) {
		this.havePreviousPageSection = value;
	}

	/**
	 * 마지막 페이지 섹션인지 여부를 반환한다.
	 */
	@ApiModelProperty(value = "마지막 페이지 섹션인지 여부", dataType = "boolean", required = true)
	boolean haveNextPageSection = false;
	public boolean isHaveNextPageSection() {
		// 레코드가 존재하지 않는 경우
		if (totalCount <= 0)
			return false;

		// 섹션 당 페이지 수가 없는 경우
		if (pagePerSection <= 0)
			return false;

		// 전체 섹션 수
		int sectionCount = getTotalPage() / pagePerSection;
		if (getTotalPage() % pagePerSection > 0)
			sectionCount++;

		// 현재 섹션 번호
		int currentSection = pageNo / pagePerSection;
		if (pageNo % pagePerSection > 0)
			currentSection++;

		// 마지막 섹션이 아닌 경우 true, 마지막 섹션인 경우 false
		return sectionCount != currentSection;
	}
	public void setHaveNextPageSection(boolean value) {
		this.haveNextPageSection = value;
	}

	/**
	 * 아이템 목록으로 페이징 값을 재설정한다.
	 */
	public void ResetWithItems() {
		this.totalCount = this.items.size();
		this.skips = 0;
		this.pageNo = 1;
		this.countPerPage = this.items.size();
		this.pagePerSection = 1;
	}

	/**
	 * 결과 목록
	 */
	@ApiModelProperty(value = "결과 목록", dataType = "java.util.List<T>", required = true)
	private List<T> items = new ArrayList<>();

	/**
	 * 결과 목록을 설정한다.
	 * @param items 설정할 결과 목록
	 */
	public void setItems(List<T> items) {
		if(items == null)
			this.items.clear();
		else
			this.items = items;
	}
}
