package fr.fgirard.batch.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tennis_surface")
public class Surface implements Serializable {

	
	private static final long serialVersionUID = -8040864183858452871L;
	
	@Id
	@Column(name = "surface_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int surfaceId;

	@Column(name = "surface", nullable = false, length = 10)
	private String surface;
	
	public Surface() {
		super();
	}
	
	public Surface(final String surface) {
		super();
		this.surface = surface;
	}

	public int getSurfaceId() {
		return this.surfaceId;
	}

	public String getSurface() {
		return this.surface;
	}

	public void setSurfaceId(final int surfaceId) {
		this.surfaceId = surfaceId;
	}

	public void setSurface(final String surface) {
		this.surface = surface;
	}

	@Override
	public String toString() {
		return "Surface [surfaceId=" + this.surfaceId + ", surface=" + this.surface + "]";
	}

}
