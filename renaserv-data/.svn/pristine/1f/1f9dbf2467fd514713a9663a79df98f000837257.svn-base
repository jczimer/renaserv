package com.winksys.renaserv.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="veiculo")
public class Veiculo  implements Serializable{

	@Embeddable
	public static class VeiculoId implements Serializable{ 
		
		@ManyToOne
		@JoinColumn(name="credential")		
		private Credential credential;
				
		private int id;

		public Credential getCredential() {
			return credential;
		}

		public void setCredential(Credential credential) {
			this.credential = credential;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
	}
	
	@EmbeddedId
	private VeiculoId id;
	
	private String placa;
	private String modelo;
	private String cor;
	private String ano;
	private String renavam;
	private String chassi;
	private float lat;
	private float lon;
	private float speed;
	private int io;
	private int direction;
	
	@Column(name="tensao_bateria")
	private float tensaoBateria;
	
	@Column(name="last_check")
	private long lastCheck;
	
	
	@Column(name="data_atualizacao")
	private Date dataAtualizacao;
	
	@Column(name="data_dados")
	private Date dataDados;
	
	
		
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name="proprietario", referencedColumnName="id", insertable=true, updatable=true),
			@JoinColumn(name="credential_prop", referencedColumnName="credential", insertable=true, updatable=true)
	})
	private Proprietario proprietario;


	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
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

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getIo() {
		return io;
	}

	public void setIo(int io) {
		this.io = io;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Date getDataDados() {
		return dataDados;
	}

	public void setDataDados(Date dataDados) {
		this.dataDados = dataDados;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getRenavam() {
		return renavam;
	}

	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public VeiculoId getId() {
		return id;
	}

	public void setId(VeiculoId id) {
		this.id = id;
	}

	public long getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(long lastCheck) {
		this.lastCheck = lastCheck;
	}

	public float getTensaoBateria() {
		return tensaoBateria;
	}

	public void setTensaoBateria(float tensaoBateria) {
		this.tensaoBateria = tensaoBateria;
	}	
	
}
