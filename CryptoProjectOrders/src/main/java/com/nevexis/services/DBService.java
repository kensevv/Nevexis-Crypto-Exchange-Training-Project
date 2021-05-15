package com.nevexis.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nevexis.entities.CurrencyPairs;
import com.nevexis.entities.CurrencyPairsId;
import com.nevexis.entities.Orders;
import com.nevexis.entities.Traders;
import com.nevexis.entities.Trades;
import com.nevexis.enums.OrderType;
import com.nevexis.enums.StatusEnum;

@Service
@Transactional
public class DBService extends BasicService {
	@Autowired
	private MatchOrdersService matchOrdersService;

	public Traders getTraderById(Long id) {
		return em.find(Traders.class, id);
	}

	public CurrencyPairs findCurrencyPair(CurrencyPairs currencyPair) {
		return em.find(CurrencyPairs.class, new CurrencyPairsId(currencyPair));
	}

	public Orders createOrder(Orders newOrder) {
		em.persist(newOrder);
		matchOrdersService.matchOrders(newOrder);
		return newOrder;
	}

	public Orders cancelOrder(Long orderId) {
		Orders order = em.find(Orders.class, orderId);
		order.setStatus(StatusEnum.CANCELLED);
		return order;
	}

	public List<Orders> getAllOrdersByStatus(StatusEnum status) {
		return em.createNamedQuery(Queries.getAllOrdersByStatus, Orders.class).setMaxResults(100)
				.setParameter("status", status).getResultList();
	}

	public List<Orders> getAllOpenOrdersByCurrencyPairAndOrderType(CurrencyPairs currencyPair, OrderType orderType) {
		return em.createNamedQuery(Queries.getAllOpenOrdersByCurrencyPairAndOrderType, Orders.class).setMaxResults(10)
				.setParameter("orderType", orderType).setParameter("cryptoCode", currencyPair.getCryptoCode())
				.setParameter("fiatCode", currencyPair.getFiatCode()).getResultList();
	}

	public List<CurrencyPairs> getAllCurrencyPairs() {
		return em.createNamedQuery(Queries.getAllCurrencyPairs, CurrencyPairs.class).setMaxResults(20).getResultList();
	}

	public void addTrade(Trades newTrade) {
		em.persist(newTrade);
	}

	public Long getCurrencyPairsCount() {
		Long result = em.createQuery("SELECT count(c) FROM CurrencyPairs c", Long.class).getSingleResult();
		return result;
	}

	public List<Orders> getAllOrdersByType(OrderType type) {
		return em.createNamedQuery(Queries.getAllOrdersByType, Orders.class).setMaxResults(100)
				.setParameter("type", type).getResultList();

	}

	public List<Orders> getAllOrdersByTrader(Long id) {
		return em.createNamedQuery(Queries.getAllOrdersByTrader, Orders.class).setMaxResults(100).setParameter("id", id)
				.getResultList();
	}

	public List<Orders> getAllOrdersByCrypto(String cryptoCode) {
		return em.createNamedQuery(Queries.getAllOrdersByCrypto, Orders.class).setMaxResults(100)
				.setParameter("code", cryptoCode).getResultList();
	}

	public List<Trades> getAllTradesByCurrencyPair(CurrencyPairs currencyPair) {
		return em.createNamedQuery(Queries.getAllTradesByCurrencyPair, Trades.class).setMaxResults(1000)
				.setParameter("cryptoCode", currencyPair.getCryptoCode())
				.setParameter("fiatCode", currencyPair.getFiatCode())
				.getResultList();
	}

	public List<Trades> getAllTradesByTrader(Long traderID){
		return em.createNamedQuery(Queries.getAllTradesByTrader,Trades.class).setMaxResults(100)
				.setParameter("id",traderID).getResultList();
	}
}