package fr.fgirard.batch.match.bo;

import java.util.List;

import fr.fgirard.batch.domain.Tournament;

public class ListTournamentsBO {

	
	private List<Tournament> listTournaments;

	public ListTournamentsBO() {
		super();
	}

	public List<Tournament> getListTournaments() {
		return this.listTournaments;
	}

	public void setListTournaments(final List<Tournament> listTournaments) {
		this.listTournaments = listTournaments;
	}

	@Override
	public String toString() {
		return "TournamentsBO [listTournaments=" + this.listTournaments + "]";
	}
	
	
}
