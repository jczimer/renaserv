package com.winksys.renaserv.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="log_ocorrencia")
public class LogOcorrencia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1634199614777326444L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String usuario;
	private Date data;
	
	@Lob
	private String tratativa;
	
	private int status;
	
	@ManyToOne
	@JoinColumns({	
			@JoinColumn(name="credential", referencedColumnName="credential"),
			@JoinColumn(name="veiculo", referencedColumnName="veiculo"),
			@JoinColumn(name="evento", referencedColumnName="evento"),
			@JoinColumn(name="data_dados", referencedColumnName="data_dados")
	}
	)
	private Ocorrencia ocorrencia;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTratativa() {
		return tratativa;
	}

	public void setTratativa(String tratativa) {
		this.tratativa = tratativa;
	}

	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
