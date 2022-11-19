package br.com.empresa.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.sql.Delete;

import br.com.empresa.exception.BOException;
import br.com.empresa.exception.BOValidationException;
import br.com.empresa.vo.ClienteVO;
import br.com.empresa.vo.ProdutoVO;

public class ProdutoDAO implements IProdutoDAO {

	public ProdutoDAO() {
		// TODO implementar de forma correta posteriormente.
	}

	@Override
	public ProdutoVO buscarProdutoPorId(ProdutoVO produtoVO) throws BOException {
		// PROCURA POR ID ---PRONTO---
		EntityManager em = HibernateUtil.getEntityManager();

		ProdutoVO produto = em.find(ProdutoVO.class, produtoVO.getId());

		em.close();

		return produto;
	}

	@Override
	public List<ProdutoVO> listarProduto(BigInteger id, String descri, String status, String codbar, ClienteVO client)
			throws BOException {
		// LISTA DE PRODUTOS NA TABELA PRODUTO ---PRONTO---
		EntityManager em = HibernateUtil.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ProdutoVO> criteria = cb.createQuery(ProdutoVO.class);

		// Cláusula From
		Root<ProdutoVO> produtoFrom = criteria.from(ProdutoVO.class);

		// Cláusula Where
		Predicate produtoWhere = cb.equal(produtoFrom.get("client"), client);

		criteria.select(produtoFrom);
		criteria.where(produtoWhere);

		TypedQuery<ProdutoVO> query = em.createQuery(criteria);

		List<ProdutoVO> listaProdutos = query.getResultList();

		List<ProdutoVO> retorno = new ArrayList<>();

		for (ProdutoVO produto : listaProdutos) {

			if (produto.getClient().equals(client) == false) {
				continue;
			}

			if (id != null) {
				if (produto.getId().equals(id) == false) {
					continue;
				}
			}

			if (descri != null && descri.trim().length() > 0) {
				if (produto.getDescri().contains(descri) == false) {
					continue;
				}
			}

			if (status != null) {
				if (produto.getStatus().equals(status) == false) {
					continue;
				}
			}

			if (codbar != null && codbar.trim().length() > 0) {
				if (produto.getCodbar().contains(codbar) == false) {
					continue;
				}
			}

			retorno.add(produto);
		}

		em.close();

		return retorno;
	}

	@Override
	public int listarProdutoCount(BigInteger id, String descri, String status, String codbar, ClienteVO client)
			throws BOException {

		// TODO implementar de forma correta posteriormente.

		List<ProdutoVO> produtoVOs = Dados.getProdutoVOs();

		if (produtoVOs != null) {
			return produtoVOs.size();
		}

		return 0;
	}

	@Override
	public List<ProdutoVO> listarProduto(int first, int pageSize, Map<String, Object> filters, ClienteVO cliente)
			throws BOException {

		// TODO implementar de forma correta posteriormente.

		List<ProdutoVO> produtoVOs = Dados.getProdutoVOs();

		return produtoVOs;
	}

	@Override
	public int listarProdutoCount(Map<String, Object> filters, ClienteVO cliente) throws BOException {

		// TODO implementar de forma correta posteriormente.

		List<ProdutoVO> produtoVOs = Dados.getProdutoVOs();

		if (produtoVOs != null) {
			return produtoVOs.size();
		}

		return 0;
	}

	@Override
	public void salvarProduto(ProdutoVO produtoVO) throws BOValidationException, BOException {
		// SALVAR PRODUTO ---PRONTO---
		EntityManager em = HibernateUtil.getEntityManager();

		ClienteVO c = new ClienteVO();
		c.setId(BigInteger.ONE);

		if (produtoVO.getId() == null) {
			produtoVO.setDescri(produtoVO.getDescri());
			produtoVO.setCodbar(produtoVO.getCodbar());
			produtoVO.setQtdest(produtoVO.getQtdest());
			produtoVO.setValcom(produtoVO.getValcom());
			produtoVO.setValven(produtoVO.getValven());
			produtoVO.setStatus(produtoVO.getStatus());
			produtoVO.setDatfab(produtoVO.getDatfab());
			produtoVO.setDatval(produtoVO.getDatval());
			produtoVO.setClient(c);

			try {
				em.getTransaction().begin();
				em.persist(produtoVO);
				em.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				em.getTransaction().rollback();
			}
		} else {
			
			try {
				em.getTransaction().begin();
				ProdutoVO produto = em.find(ProdutoVO.class, produtoVO.getId());
				produtoVO.setDescri(produtoVO.getDescri());
				produtoVO.setCodbar(produtoVO.getCodbar());
				produtoVO.setQtdest(produtoVO.getQtdest());
				produtoVO.setValcom(produtoVO.getValcom());
				produtoVO.setValven(produtoVO.getValven());
				produtoVO.setStatus(produtoVO.getStatus());
				produtoVO.setDatfab(produtoVO.getDatfab());
				produtoVO.setDatval(produtoVO.getDatval());
				produtoVO.setClient(c);
				em.merge(produtoVO);
				em.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				em.getTransaction().rollback();
			}finally {
				em.close();
			}
		}

	}

	@Override
	public void excluirProduto(ProdutoVO produtoVO) throws BOValidationException, BOException {
		// DELETAR PRODUTO ---PRONTO---
		EntityManager em = HibernateUtil.getEntityManager();

		try {
			em.getTransaction().begin();
			ProdutoVO produto = em.find(ProdutoVO.class, produtoVO.getId());
			em.remove(produto);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}

	}

}
