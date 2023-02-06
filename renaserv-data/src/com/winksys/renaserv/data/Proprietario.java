package com.winksys.renaserv.data;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="proprietario")
public class Proprietario implements Serializable {

	
	@Embeddable
	public static class ProprietarioId implements Serializable {
			
		
		private int id;
		
		@ManyToOne
		@JoinColumn(name="credential")
		private Credential credential;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Credential getCredential() {
			return credential;
		}

		public void setCredential(Credential credential) {
			this.credential = credential;
		}

		
		
	}

	@EmbeddedId
	private ProprietarioId id;
	
	private String nome;
	private String documento;
	private String email;
	private String endereco;
	private String complemento;
	private String cep;
	private String bairro;	
	private String telefone;	
	
	private String contatos;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public ProprietarioId getId() {
		return id;
	}
	public void setId(ProprietarioId id) {
		this.id = id;
	}	
	public String getContatos() {
		return contatos;
	}
	public void setContatos(String contatos) {
		this.contatos = contatos;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	
	
}
