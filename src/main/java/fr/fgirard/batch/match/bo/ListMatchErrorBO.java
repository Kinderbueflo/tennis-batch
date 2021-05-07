package fr.fgirard.batch.match.bo;

import java.util.List;

public class ListMatchErrorBO {

	
	private List<MatchBO> listMatchError;

	public ListMatchErrorBO() {
		super();
	}

	public List<MatchBO> getListMatchError() {
		return this.listMatchError;
	}

	public void setListMatchError(final List<MatchBO> listMatchError) {
		this.listMatchError = listMatchError;
	}

	@Override
	public String toString() {
		return "ListMatchErrorBO [listMatchError=" + this.listMatchError + "]";
	}

	
}
