package br.com.empresa;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.empresa.dao.HibernateUtil;
import br.com.empresa.vo.ClienteVO;
import br.com.empresa.vo.ProdutoVO;

public class Principal_teste {
	
	public static final String ASCENDING = "ASCENDING";
	public static final String DESCENDING = "DESCENDING";

	public Principal_teste() {
	}

	public static void main(String[] args) {

		Principal_teste p = new Principal_teste();
		
		//p.consultarProdutoPorId();
		p.conultaProdutoSimples();
		//p.conultaProdutoSimplesComTuple();
		//p.conultaProdutoSimplesComTupleJoin();
		//p.conultaProdutoSimplesComTupleJoinPaginacao(10, 3);
		ClienteVO cliente = new ClienteVO();
		cliente.setId(BigInteger.ONE);
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("descri", "AGUA");
		/*p.conultaProdutoSimplesComTupleJoinPaginacaoFiltroCompleto(
				0, 5, "descri", Principal_teste.DESCENDING, filters, cliente);*/
		//p.consultarProduto();
		//p.inserirProduto();
		//p.editarProduto();
		//p.excluirProduto();
		
		System.exit(0);
		
		

	}

	private void conultaProdutoSimplesComTupleJoinPaginacaoFiltroCompleto(Integer first, Integer pageSize, String sortField, String sortOrder, Map<String, Object> filters, ClienteVO cliente) {
		
		System.out.println("Consultando com tupla e utilizando joins e paginação e filtros");
		
		EntityManager em = HibernateUtil.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
		
		//Clausula from LEFT OUTER JOIN
		Root<ProdutoVO> produtoFrom = criteria.from(ProdutoVO.class);
		Join<ProdutoVO, ClienteVO> clienteFrom = produtoFrom.join("client");
		
		Path<BigInteger> produtoFrom_Id = produtoFrom.get("id");
		Path<String> produtoFrom_Descri = produtoFrom.get("descri");
		Path<BigDecimal> produtoFrom_Qtdest = produtoFrom.get("qtdest");
		
		//Clausula select
		criteria.multiselect(produtoFrom_Id,produtoFrom_Descri,produtoFrom_Qtdest, clienteFrom);
		
		//Clausula where
		//Predicate produtoWhere = cb.equal(produtoFrom.get("client"), BigInteger.ONE);
		Predicate produtoWhere = cb.equal(clienteFrom, cliente);
		
		if(filters != null) {
			for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
				
				String filterProperty = it.next();
				String filterValue = filters.get(filterProperty).toString();
				
				if(filterProperty.equals("id")) {
					
					produtoWhere = cb.and(produtoWhere, 
							cb.equal(produtoFrom.get(filterProperty), filterValue));
					
				} else if(filterProperty.equals("client")) {
					
					produtoWhere = cb.and(produtoWhere,
				    cb.like(cb.lower(clienteFrom.get("descri")), 
				    	 "%" + filterValue.toLowerCase() + "%"));
					
				} else {
					
					produtoWhere = cb.and(produtoWhere,
					cb.like(cb.lower(clienteFrom.get("descri")), 
							"%" + filterValue.toLowerCase() + "%"));
					
				}
				
			}
		}
		
		Order produtoOrderBy = cb.asc(produtoFrom.get("descri"));
		
		if(sortField != null) {
			
			if(sortOrder.equals(Principal_teste.ASCENDING)) {
				produtoOrderBy = cb.asc(produtoFrom.get(sortField));
			} else if(sortOrder.equals(Principal_teste.DESCENDING)) {
				produtoOrderBy = cb.desc(produtoFrom.get(sortField));
			}
			
		}
		
		//Fecha o entity manager
		em.close();
		
		System.out.println("Termino consulta com tupla e utilizando joins e paginação e filtros");
		
	}

	private void conultaProdutoSimplesComTupleJoinPaginacao(Integer first, Integer pageSize) {
		
		System.out.println("Consultando com tupla e utilizando joins e paginação");
		
		EntityManager em = HibernateUtil.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
		
		//Clausula from LEFT OUTER JOIN
		Root<ProdutoVO> produtoFrom = criteria.from(ProdutoVO.class);
		Join<ProdutoVO, ClienteVO> clienteFrom = produtoFrom.join("client", JoinType.LEFT);
		
		Path<BigInteger> produtoFrom_Id = produtoFrom.get("id");
		Path<String> produtoFrom_Descri = produtoFrom.get("descri");
		Path<BigDecimal> produtoFrom_Qtdest = produtoFrom.get("qtdest");
		
		//Clausula select
		criteria.multiselect(produtoFrom_Id,produtoFrom_Descri,produtoFrom_Qtdest, clienteFrom);
		
		//Clausula where
		//Predicate produtoWhere = cb.equal(produtoFrom.get("client"), BigInteger.ONE);
		Predicate produtoWhere = cb.equal(produtoFrom.get("client"), BigInteger.ONE);
		
		produtoWhere = cb.and(produtoWhere, cb.like(cb.lower(produtoFrom_Descri), "%agua%"));
		
		//Predicate aguaWhere = cb.like(produtoFrom, "%agua%");
		
		//Clausula Orderby
		Order produtoOrderBy = cb.asc(produtoFrom.get("descri"));
		Order produtoOrderByQtdest = cb.desc(produtoFrom.get("qtdest"));
		
		criteria.where(produtoWhere);
		criteria.orderBy(produtoOrderBy,produtoOrderByQtdest);
		
		
		TypedQuery<Tuple> query = em.createQuery(criteria);
		
		//Paginação
		if(first != null) {
			query.setFirstResult(first);
		}
		
		if(pageSize != null) {
			query.setMaxResults(pageSize);
		}
		
		
		List<Tuple> tuples = query.getResultList();
		
		List<ProdutoVO> ret = new ArrayList<ProdutoVO>();
		if(tuples != null) {
			for(Tuple tuple : tuples) {
				ClienteVO cliente = tuple.get(clienteFrom);
				
				ProdutoVO produto = new ProdutoVO();
				produto.setId(tuple.get(produtoFrom_Id));
				produto.setDescri(tuple.get(produtoFrom_Descri));
				produto.setQtdest(tuple.get(produtoFrom_Qtdest));
				produto.setClient(cliente);
				
				ret.add(produto);
			}
		}
		
		for (ProdutoVO produto : ret) {
			System.out.println("Produto: "+produto.getId()+" - "+produto.getDescri());
		}
		
		//Fecha o entity manager
		em.close();
		
		System.out.println("Termino consulta com tupla e utilizando joins e paginação");
		
	}

	private void conultaProdutoSimplesComTupleJoin() {
		
		System.out.println("Consultando com tupla e utilizando joins");
		
		EntityManager em = HibernateUtil.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
		
		//Clausula from
		Root<ProdutoVO> produtoFrom = criteria.from(ProdutoVO.class);
		Join<ProdutoVO, ClienteVO> clienteFrom = produtoFrom.join("client");
		
		Path<BigInteger> produtoFrom_Id = produtoFrom.get("id");
		Path<String> produtoFrom_Descri = produtoFrom.get("descri");
		Path<BigDecimal> produtoFrom_Qtdest = produtoFrom.get("qtdest");
		
		//Clausula select
		criteria.multiselect(produtoFrom_Id,produtoFrom_Descri,produtoFrom_Qtdest);
		
		//Clausula where
		//Predicate produtoWhere = cb.equal(produtoFrom.get("client"), BigInteger.ONE);
		Predicate produtoWhere = cb.equal(produtoFrom, BigInteger.ONE);
		
		//Clausula Orderby
		Order produtoOrderBy = cb.asc(produtoFrom.get("descri"));
		
		criteria.where(produtoWhere);
		criteria.orderBy(produtoOrderBy);
		
		TypedQuery<Tuple> query = em.createQuery(criteria);
		query.setMaxResults(10);
		
		List<Tuple> tuples = query.getResultList();
		
		List<ProdutoVO> ret = new ArrayList<ProdutoVO>();
		if(tuples != null) {
			for(Tuple tuple : tuples) {
				ProdutoVO produtoVO = new ProdutoVO(tuple.get(produtoFrom_Id));
				produtoVO.setDescri(tuple.get(produtoFrom_Descri));
				produtoVO.setQtdest(tuple.get(produtoFrom_Qtdest));
				ret.add(produtoVO);
			}
		}
		
		for (ProdutoVO produtoVO : ret) {
			System.out.println("Produto: "+produtoVO.getId()+" - "+produtoVO.getDescri());
		}
		
		//Fecha o entity manager
		em.close();
		
		System.out.println("Termino consulta com tupla e utilizando joins");
		
	}

	private void conultaProdutoSimplesComTuple() {
		
		System.out.println("Consultando com tupla");
		
		EntityManager em = HibernateUtil.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
		
		//Clausula from
		Root<ProdutoVO> produtoFrom = criteria.from(ProdutoVO.class);
		
		Path<BigInteger> produtoFrom_Id = produtoFrom.get("id");
		Path<String> produtoFrom_Descri = produtoFrom.get("descri");
		Path<BigDecimal> produtoFrom_Qtdest = produtoFrom.get("qtdest");
		
		//Clausula select
		criteria.multiselect(produtoFrom_Id,produtoFrom_Descri,produtoFrom_Qtdest);
		
		//Clausula where
		Predicate produtoWhere = cb.equal(produtoFrom.get("client"), BigInteger.ONE);
		
		//Clausula Orderby
		Order produtoOrderBy = cb.asc(produtoFrom.get("descri"));
		
		criteria.where(produtoWhere);
		criteria.orderBy(produtoOrderBy);
		
		TypedQuery<Tuple> query = em.createQuery(criteria);
		query.setMaxResults(10);
		
		List<Tuple> tuples = query.getResultList();
		
		List<ProdutoVO> ret = new ArrayList<ProdutoVO>();
		if(tuples != null) {
			for(Tuple tuple : tuples) {
				ProdutoVO produtoVO = new ProdutoVO(tuple.get(produtoFrom_Id));
				produtoVO.setDescri(tuple.get(produtoFrom_Descri));
				produtoVO.setQtdest(tuple.get(produtoFrom_Qtdest));
				ret.add(produtoVO);
			}
		}
		
		for (ProdutoVO produtoVO : ret) {
			System.out.println("Produto: "+produtoVO.getId()+" - "+produtoVO.getDescri());
		}
		
		//Fecha o entity manager
		em.close();
		
		System.out.println("Termino consulta com tupla");
		
	}

	private void conultaProdutoSimples() {
		
		System.out.println("----------------------começando-----------------------");
		
		EntityManager em = HibernateUtil.getEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProdutoVO> criteria = cb.createQuery(ProdutoVO.class);
		
		//Clausula from
		Root<ProdutoVO> produtoFrom = criteria.from(ProdutoVO.class);
		
		//Clausula where
		Predicate produtoWhere = cb.equal(produtoFrom.get("client"), new BigInteger("1"));
		
		//Clausula order by
		Order produtoOrderBy = cb.asc(produtoFrom.get("descri"));
		
		//Atribuindo as clausulas a consulta
		criteria.select(produtoFrom);
		criteria.where(produtoWhere);
		criteria.orderBy(produtoOrderBy);
		
		TypedQuery<ProdutoVO> query = em.createQuery(criteria);
		List<ProdutoVO> listaProdutos = query.getResultList();
		
		for (ProdutoVO produtoVO : listaProdutos) {
			System.out.println("Produto: "+produtoVO.getId()+" - "+produtoVO.getDescri() + " - " + 
		     produtoVO.getClient().getDescri());
		}
		
		em.close();
		
		System.out.println("---------------------terminando--------------------");
		
	}

	private void consultarProdutoPorId() {
		
		EntityManager em = HibernateUtil.getEntityManager();
		
		ProdutoVO produto = em.find(ProdutoVO.class, new BigInteger("378799"));
		
		System.out.println("Produto >>> "+ produto.getId() + "-" + produto.getDescri());
		
		
		
	}

	private void excluirProduto() {
		// TODO Auto-generated method stub

	}

	private void editarProduto() {
		// TODO Auto-generated method stub

	}

	private void inserirProduto() {
		// TODO Auto-generated method stub

	}

	private void consultarProduto() {

		// TODO Auto-generated method stub

	}

}
