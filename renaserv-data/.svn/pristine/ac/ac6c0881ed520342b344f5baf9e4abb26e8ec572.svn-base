package com.winksys.renaserv.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ocorrencia")
public class Ocorrencia implements Serializable {

	@Embeddable
	public static class OcorrenciaId implements Serializable {
		
		@ManyToOne
		@JoinColumns({
				@JoinColumn(name = "credential", referencedColumnName="credential"),
				@JoinColumn(name = "veiculo", referencedColumnName="id")
		})
		private Veiculo veiculo;

		@ManyToOne
		@JoinColumn(name = "evento")
		private Evento evento;
		
		@Column(name = "data_dados")
		private Date dataDados;
		
		

		public Veiculo getVeiculo() {
			return veiculo;
		}

		public void setVeiculo(Veiculo veiculo) {
			this.veiculo = veiculo;
		}

		public Evento getEvento() {
			return evento;
		}

		public void setEvento(Evento evento) {
			this.evento = evento;
		}

		public Date getDataDados() {
			return dataDados;
		}

		public void setDataDados(Date dataDados) {
			this.dataDados = dataDados;
		}

		

	}
	@EmbeddedId
	private OcorrenciaId id;


	@Column(name = "data_cad")
	private Date dataCad;
	private float lat;
	private float lon;
	private int direction;
	private int io;
	private float speed;
	
	@Lob
	private String tratativa;
	
	@Column(name="data_tratativa")
	private Date dataTratativa;
	
	@Column(name="usuario_tratativa")
	private String usuarioTratativa;
	
	private int status;
	
	
	@Column(name="event_id")
	private int eventId;
	
	@Column(name="data_primeira_tratativa")
	private Date dataPrimeiraTratativa;

	public Date getDataCad() {
		return dataCad;
	}

	public void setDataCad(Date dataCad) {
		this.dataCad = dataCad;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getIo() {
		return io;
	}

	public void setIo(int io) {
		this.io = io;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public OcorrenciaId getId() {
		return id;
	}

	public void setId(OcorrenciaId id) {
		this.id = id;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public Date getDataTratativa() {
		return dataTratativa;
	}

	public void setDataTratativa(Date dataTratativa) {
		this.dataTratativa = dataTratativa;
	}

	public String getUsuarioTratativa() {
		return usuarioTratativa;
	}

	public void setUsuarioTratativa(String usuarioTratativa) {
		this.usuarioTratativa = usuarioTratativa;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTratativa() {
		return tratativa;
	}

	public void setTratativa(String tratativa) {
		this.tratativa = tratativa;
	}

	public Date getDataPrimeiraTratativa() {
		return dataPrimeiraTratativa;
	}

	public void setDataPrimeiraTratativa(Date dataPrimeiraTratativa) {
		this.dataPrimeiraTratativa = dataPrimeiraTratativa;
	}

}
