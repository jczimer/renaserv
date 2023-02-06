package com.winksys.renaserv.data;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="evento_monitorado")
public class EventoMonitorado implements Serializable {

	@Embeddable
	public static class EventoMonitoradoId implements Serializable {
		
		@ManyToOne
		@JoinColumn(name="credential")
		private Credential credential;
		
		@ManyToOne
		@JoinColumn(name="evento")
		private Evento evento;

		public Credential getCredential() {
			return credential;
		}

		public void setCredential(Credential credential) {
			this.credential = credential;
		}

		public Evento getEvento() {
			return evento;
		}

		public void setEvento(Evento evento) {
			this.evento = evento;
		}
		
		
	}
	
	@EmbeddedId
	private EventoMonitoradoId id;
	
	
	public EventoMonitoradoId getId() {
		return id;
	}

	public void setId(EventoMonitoradoId id) {
		this.id = id;
	}
	
}
