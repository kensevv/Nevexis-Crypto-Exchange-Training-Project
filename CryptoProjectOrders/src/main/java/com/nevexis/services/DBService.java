package com.nevexis.services;

import java.util.List;

import org.hibernate.criterion.Order;
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
	private static final String getAllOrdersByStatus = "Orders.getAllOrdersByStatus";
	private static final String getAllOpenOrdersByCurrencyPairAndOrderType = "Orders.getAllOpenOrdersByCurrencyPairAndOrderType";
	private static final String getAllCurrencyPairs = "CurrencyPairs.getAllCurrencyPairs";
	private static final String getAllOrdersByTrader = "Orders.getAllOrdersByTrader";
	private static final String getAllOrdersByType = "Orders.getAllOrdersByType";
	private static final String getAllOrdersByCrypto = "Orders.getAllOrdersByCrypto";
	private static final String getAllTradesByTrader = "Trades.getAllTradesByTrader";

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
		matchOrdersService.matchOrders(newOrder.getCurrencyPair());
		return newOrder;
	}

	public Orders cancelOrder(Long orderId) {
		Orders order = em.find(Orders.class, orderId);
		order.setStatus(StatusEnum.CANCELLED);
		return order;
	}

	public List<Orders> getAllOrdersByStatus(StatusEnum status) {
		return em.createNamedQuery(getAllOrdersByStatus, Orders.class).setMaxResults(100).setParameter("status", status)
				.getResultList();
	}

	public List<Orders> getAllOpenOrdersByCurrencyPairAndOrderType(CurrencyPairs currencyPair, OrderType orderType) {
		return em.createNamedQuery(getAllOpenOrdersByCurrencyPairAndOrderType, Orders.class).setMaxResults(10)
				.setParameter("orderType", orderType).setParameter("cryptoCode", currencyPair.getCryptoCode())
				.setParameter("fiatCode", currencyPair.getFiatCode()).getResultList();
	}

	public List<CurrencyPairs> getAllCurrencyPairs() {
		return em.createNamedQuery(getAllCurrencyPairs, CurrencyPairs.class).setMaxResults(20).getResultList();
	}

	public void addTrade(Trades newTrade) {
		em.persist(newTrade);
	}
	
	public Long getCurrencyPairsCount() {
		Long result = em.createQuery("SELECT count(c) FROM CurrencyPairs c", Long.class).getSingleResult();
		return result;
	}

	public List<Orders> getAllOrdersByType(OrderType type){
		return em.createNamedQuery(getAllOrdersByType, Orders.class).setMaxResults(100)
				.setParameter("type", type).getResultList();


	}

	public List<Orders> getAllOrdersByTrader(Long id){
		return em.createNamedQuery(getAllOrdersByTrader, Orders.class).setMaxResults(100)
				.setParameter("id", id).getResultList();
	}

	public List<Orders> getAllOrdersByCrypto(String cryptoCode){
		return em.createNamedQuery(getAllOrdersByCrypto,Orders.class).setMaxResults(100)
				.setParameter("code",cryptoCode).getResultList();
	}

	public List<Trades> getAllTradesByTrader(Long traderID){
		return em.createNamedQuery(getAllTradesByTrader,Trades.class).setMaxResults(100)
				.setParameter("id",traderID).getResultList();
	}
}