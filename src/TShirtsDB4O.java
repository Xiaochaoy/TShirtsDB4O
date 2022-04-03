import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Entities.*;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

/**
 * @author Joan Anton Perez Branya
 * @since 19/02/2017
 *
 */

public class TShirtsDB4O {
	public static ArrayList<Order> orders;
	static ObjectContainer db;
	

	/**
	 * Implement TODO methods and run to test
	 * 
	 * @param args
	 *            no args
	 * @throws IOException
	 *             in order to read files
	 * @throws ParseException
	 *             in order to parse data formats
	 */
	public static void main(String[] args) throws IOException, ParseException {
		TShirtsDB4O TSM = new TShirtsDB4O();
		FileAccessor fa = new FileAccessor();
		fa.readArticlesFile("articles.csv");
		fa.readCreditCardsFile("creditCards.csv");
		fa.readCustomersFile("customers.csv");
		fa.readOrdersFile("orders.csv");
		fa.readOrderDetailsFile("orderDetails.csv");
		orders = fa.orders;
		try {

			File file = new File("orders.db");
			String fullPath = file.getAbsolutePath();
			db = Db4o.openFile(fullPath);

			TSM.addOrders();
			TSM.listOrders();
			TSM.listArticles();
			TSM.addArticle(7, "CALCETINES EJECUTIVOS 'JACKSON 3PK'", "gris", "45", 18.00);
			TSM.updatePriceArticle(7, 12.00);
			TSM.llistaArticlesByName("CALCETINES EJECUTIVOS 'JACKSON 3PK'");
			TSM.deletingArticlesByName("POLO BÁSICO 'MANIA'");
			TSM.deleteArticleById(7);
			TSM.listArticles();
			TSM.listCustomers();
			TSM.changeCreditCardToCustomer(1);
			TSM.listCustomers();
			TSM.llistaCustomerByName("Laura");
			TSM.showOrdersByCustomerName("Laura");
			TSM.showCreditCardByCustomerName("Laura");
			TSM.deleteCustomerbyId(2);
			TSM.retrieveOrderContentById_Order(2);
			TSM.deleteOrderContentById_Order(2);
			TSM.retrieveOrderContentById_Order(2);
			TSM.listCustomers();
			TSM.clearDatabase();
			TSM.listOrders();

		} finally {
			// close database
			db.close();
		}
	}

	/**
	 * Select Customer using customer id and next generate a new credit card and
	 * update customer using the new credit card
	 * 
	 * @param i
	 *            idCustomer
	 */
	public void changeCreditCardToCustomer(int i) {
		// TODO Auto-generated method stub
		System.out.println("poner un nuevo targeta credito a un customer");
		int[] credito = new int[16];
		for (int j = 0; j < 16; j++) {
			credito[j] = (int) Math.random()*9+1;
		}
		String r = "" + credito[0] + credito[1] + credito[2] + credito[3] + credito[4] + credito[5] + credito[6] + credito[7] + credito[8] + credito[9] + credito[10] + credito[11] + credito[12] + credito[13] + credito[14] + credito[15];
		CreditCard creditCard = new CreditCard(r,"" + credito[2] + credito[5] + credito[12],((int) Math.random()*12+1), ((int) Math.random()*10+20));
		db.store(creditCard);
		ObjectSet<Customer> result = db.queryByExample(new Customer(i,null,null,null,null,null));
		result.get(0).setCreditCard(creditCard);
	}

	/**
	 * Select Article using id and next update price
	 * 
	 * @param id
	 *            article
	 * @param newPrice
	 */
	public void updatePriceArticle(int id, double newPrice) {
		// TODO Auto-generated method stub
		System.out.println("actualizar precio de article");
		Article article = new Article();
		article.setIdArticle(id);
		ObjectSet<Article> result = db.queryByExample(article);
		result.get(0).setRecommendedPrice((float) newPrice);
	}

	/**
	 * Add a new article into database
	 * 
	 * @param i
	 *            article id
	 * @param string
	 *            article name
	 * @param string2
	 *            article colour
	 * @param string3
	 *            article size
	 * @param d
	 *            article price
	 */
	public void addArticle(int i, String string, String string2, String string3, double d) {
		// TODO Auto-generated method stub
		System.out.println("añadir article");
		Article article = new Article(i,string,string2,string3,(float) d);
		db.store(article);
		System.out.println(article.toString());
	}

	/**
	 * Delete an article using idArticle
	 * 
	 * @param i
	 *            idArticle
	 */
	public void deleteArticleById(int i) {
		// TODO Auto-generated method stub
		System.out.println("borrar article por id");
		Article article = new Article();
		article.setIdArticle(i);
		ObjectSet<Article> result = db.queryByExample(article);
		db.delete(result);
	}

	/**
	 * Delete Order and its orderdetails using idOrder
	 * 
	 * @param i
	 *            idOrder
	 */
	public void deleteOrderContentById_Order(int i) {
		// TODO Auto-generated method stub
		System.out.println("borrar order contenido por order id");
		Order order = new Order();
		order.setIdOrder(i);
		ObjectSet<Order> result = db.queryByExample(order);
		result.get(0).setDetails(null);
	}

	/**
	 * Select Order using his id and order details
	 * 
	 * @param i
	 *            idOrder
	 */
	public void retrieveOrderContentById_Order(int i) {
		// TODO Auto-generated method stub

	}

	/**
	 * Delete Customer using idCustomer
	 * 
	 * @param i
	 *            idCustomer
	 */
	public void deleteCustomerbyId(int i) {
		// TODO Auto-generated method stub
		System.out.println("borrar customer id");
		ObjectSet<Customer> result = db.queryByExample(new Customer(i,null,null,null,null,null));
		db.delete(result);
	}

	/**
	 * Select Customer using customer name and next select the credit card
	 * values
	 * 
	 * @param string
	 *            customer name
	 */
	public void showCreditCardByCustomerName(String string) {
		// TODO Auto-generated method stub
		List<Customer> customers = db.query(new Predicate<Customer>() {
			public boolean match(Customer customer) {
				return customer.getName().compareTo(string) == 0;
			}
		});
		System.out.println("mostrar targeta credito por customer name");
		for (Customer c: customers) {
			System.out.println(c.getCreditCard());
		}
	}

	/**
	 * Method to list Oders and orderdetails from the database using the
	 * customer name
	 */
	public void showOrdersByCustomerName(String string) {
		// TODO Auto-generated method stub
		List<Order> orders = db.query(new Predicate<Order>() {
			public boolean match(Order order) {
				return order.getCustomer().getName().compareTo(string) == 0;
			}
		});
		System.out.println("mostrar orders por customer name");
		for (Order o: orders) {
			System.out.println(o.toString());
		}
	}

	/** delete all objects from the whole database */
	public void clearDatabase() {
		// TODO Auto-generated method stub
		System.out.println("borrar todo bbdd");

		ObjectSet<Article> result3 = db.queryByExample(new Article());
		while (result3.hasNext()){
			db.delete(result3.next());
		}


		ObjectSet<CreditCard> result5 = db.queryByExample(new CreditCard());
		while (result5.hasNext()){
			db.delete(result5.next());
		}

		ObjectSet<Customer> result4 = db.queryByExample(new Customer());
		while (result4.hasNext()){
			db.delete(result4.next());
		}
		ObjectSet<OrderDetail> result2 = db.queryByExample(new OrderDetail());
		while (result2.hasNext()){
			db.delete(result2.next());
		}

		ObjectSet<Order> result = db.queryByExample(new Order());
		while (result.hasNext()){
			db.delete(result.next());
		}

	}

	/**
	 * Delete Article using article name
	 * 
	 * @param string
	 *            Article name
	 */
	public void deletingArticlesByName(String string) {
		// TODO Auto-generated method stub
		System.out.println("borrar todos los customers por name");
		Article article = new Article();
		article.setName(string);
		ObjectSet<Article> result = db.queryByExample(article);
		while (result.hasNext()){
			db.delete(result);
		}
	}

	/** Method to list Articles from the database using their name */
	public void llistaArticlesByName(String string) {
		// TODO Auto-generated method stub
		List<Article> articles = db.query(new Predicate<Article>() {
			public boolean match(Article article) {
				return article.getName().compareTo(string) == 0;
			}
		});

		System.out.println("listar todos los articles por name");
		for (Article a: articles) {
			System.out.println(a.toString());
		}
	}

	/** Method to list Customers from the database using their name */
	public void llistaCustomerByName(String string) {
		// TODO Auto-generated method stub
		List<Customer> customers = db.query(new Predicate<Customer>() {
			public boolean match(Customer customer) {
				return customer.getName().compareTo(string) == 0;
			}
		});

		System.out.println("listar todos los customers por name");
		for (Customer c: customers) {
			System.out.println(c.toString());
		}
	}

	/** Method to list all Customers from the database */
	public void listCustomers() {
		// TODO Auto-generated method stub
		System.out.println("listar todos los customers que hay");
		ObjectSet<Customer> result = db.queryByExample(new Customer());
		while (result.hasNext()){
			System.out.println(result.next());
		}
	}

	/** Method to list all Articles from the database */
	public void listArticles() {
		// TODO Auto-generated method stub
		System.out.println("listar todos los article que hay");
		ObjectSet<Article> result = db.queryByExample(new Article());
		while (result.hasNext()){
			System.out.println(result.next());
		}
	}

	/** Method to add all orders from ArrayList and store them into database */
	public void addOrders() {
		// TODO Auto-generated method stub
		System.out.println("añadir Orders");
		for (Order o: orders) {
			db.store(o);
			System.out.println(o.toString());
		}
	}

	/** Method to list all Orders from the database */
	public void listOrders() {
		// TODO Auto-generated method stub
		System.out.println("listar todos los orders que hay");
		ObjectSet<Order> result = db.queryByExample(new Order());
		while (result.hasNext()){
			System.out.println(result.next());
		}
	}
}
