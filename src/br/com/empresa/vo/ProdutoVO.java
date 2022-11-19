package br.com.empresa.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ESPRODUT")
public class ProdutoVO implements Serializable {

	private static final long serialVersionUID = 3929961339408678201L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "SQ_ESPRODUT", sequenceName = "SQ_ESPRODUT",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, 
		generator = "SQ_ESPRODUT")
	private BigInteger id;
	
	//Nome do produto - 100 caracteres
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "descri", nullable = false, length = 100)
	private String descri;
	
	//Código de barras - 20 caracteres
	@Basic
	@Column(name = "codbar", length = 20)
	private String codbar;
	
	//Status - 1 caracter
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1)
	@Column(name = "status", nullable = false, length = 1)
	private String status;
	
	//Quantidade em estoque - 7 inteiros e 4 decimais
	@Basic(optional = false)
	@NotNull
	@Column(name = "qtdest", nullable = false, precision = 7, scale = 4)
	private BigDecimal qtdest;
	
	//Valor de compra - 7 inteiros e 2 decimais
	@Basic(optional = false)
	@NotNull
	@Column(name = "vlrcom", nullable = false, precision = 7, scale = 2)
	private BigDecimal valcom;
		
	//Valor de venda - 7 inteiros e 2 decimais
	@Basic(optional = false)
	@NotNull
	@Column(name = "vlrven", nullable = false, precision = 7, scale = 2)
	private BigDecimal valven;
	
	//Cliente
	@NotNull
	@JoinColumn(name = "client", referencedColumnName = "id", 
		nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ClienteVO client;
	
	//Data fabricação
	@Basic(optional = true)
	@Column(name = "datfab", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date datfab;
	
	//Data validade
	@Basic(optional = true)
	@Column(name = "datval", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date datval;
	
	public ProdutoVO() {
	}

	public ProdutoVO(BigInteger id) {
		super();
		this.id = id;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getQtdest() {
		return qtdest;
	}

	public void setQtdest(BigDecimal qtdest) {
		this.qtdest = qtdest;
	}


	public ClienteVO getClient() {
		return client;
	}

	public void setClient(ClienteVO client) {
		this.client = client;
	}
	
	public BigDecimal getValcom() {
		return valcom;
	}

	public void setValcom(BigDecimal valcom) {
		this.valcom = valcom;
	}

	public BigDecimal getValven() {
		return valven;
	}

	public void setValven(BigDecimal valven) {
		this.valven = valven;
	}

	public String getCodbar() {
		return codbar;
	}

	public void setCodbar(String codbar) {
		this.codbar = codbar;
	}

	public Date getDatfab() {
		return datfab;
	}

	public void setDatfab(Date datfab) {
		this.datfab = datfab;
	}

	public Date getDatval() {
		return datval;
	}

	public void setDatval(Date datval) {
		this.datval = datval;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoVO other = (ProdutoVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
