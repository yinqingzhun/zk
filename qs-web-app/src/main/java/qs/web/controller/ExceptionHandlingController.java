package qs.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import qs.exception.*;

import java.sql.SQLException;

/**
 * A controller whose request-handler methods deliberately throw exceptions to
 * demonstrate the points discussed in the Blog.
 * <p>
 * Contains its own <tt>@ExceptionHandler</tt> methods to handle (most of) the
 * exceptions it raises.
 * 
 * @author Paul Chapman
 */
//@Controller
//@RequestMapping("/local")
public class ExceptionHandlingController   {

	protected Logger logger;

	public ExceptionHandlingController() {
		logger = LoggerFactory.getLogger(getClass());
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	/* . . . . . . . . . . . . . . REQUEST HANDLERS . . . . . . . . . . . . .. */
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Home page.
	 * 
	 * @return The view name (an HTML page with Thymeleaf markup).
	 */
	@RequestMapping("")
	String home1() {
		logger.info("Local home page 1");
		return "local";
	}

	/**
	 * Home page.
	 * 
	 * @return The view name (an HTML page with Thymeleaf markup).
	 */
	@RequestMapping("/")
	String home2() {
		logger.info("Local home page 2");
		return "local";
	}

	/**
	 * No handler is needed for this exception since it is annotated with
	 * <tt>@ResponseStatus</tt>.
	 * 
	 * @return Nothing - it always throws the exception.
	 * @throws OrderNotFoundException
	 *             Always thrown.
	 */
	@RequestMapping("/orderNotFound")
	String throwOrderNotFoundException() {
		logger.info("Throw OrderNotFoundException for unknown order 12345");
		throw new OrderNotFoundException("12345");
	}

	/**
	 * Throws an unannotated <tt>DataIntegrityViolationException</tt>. Must be
	 * caught by an exception handler.
	 * 
	 * @return Nothing - it always throws the exception.
	 * @throws DataIntegrityViolationException
	 *             Always thrown.
	 */
	@RequestMapping("/dataIntegrityViolation")
	String throwDataIntegrityViolationException() throws SQLException {
		logger.info("Throw DataIntegrityViolationException");
		throw new DataIntegrityViolationException("Duplicate id");
	}

	/**
	 * Simulates a database exception by always throwing <tt>SQLException</tt>.
	 * Must be caught by an exception handler.
	 * 
	 * @return Nothing - it always throws the exception.
	 * @throws SQLException
	 *             Always thrown.
	 */
	@RequestMapping("/databaseError1")
	String throwDatabaseException1() throws SQLException {
		logger.info("Throw SQLException");
		throw new SQLException();
	}

	/**
	 * Simulates a database exception by always throwing
	 * <tt>DataAccessException</tt>. Must be caught by an exception handler.
	 * 
	 * @return Nothing - it always throws the exception.
	 * @throws DataAccessException
	 *             Always thrown.
	 */
	@RequestMapping("/databaseError2")
	String throwDatabaseException2() throws DataAccessException {
		logger.info("Throw DataAccessException");
		return "";
//		throw new DataAccessException("Error accessing database");
	}

	/**
	 * Simulates an illegal credit-card exception by always throwing
	 * <tt>InvalidCreditCardException</tt>. Handled by
	 * <tt>SimpleMappingExceptionResolver</tt>.
	 * 
	 * @return Nothing - it always throws the exception.
	 * @throws InvalidCreditCardException
	 *             Always thrown.
	 */
	@RequestMapping("/invalidCreditCard")
	String throwInvalidCreditCard() throws Exception {
		logger.info("Throw InvalidCreditCardException");
		throw new InvalidCreditCardException("1234123412341234");
	}

	/**
	 * Simulates a database exception by always throwing
	 * <tt>DatabaseException</tt>. Handled by
	 * <tt>SimpleMappingExceptionResolver</tt>.
	 * 
	 * @return Nothing - it always throws the exception.
	 * @throws DatabaseException
	 *             Always thrown.
	 */
	@RequestMapping("/databaseException")
	String throwDatabaseException() throws Exception {
		logger.info("Throw InvalidCreditCardException");
		throw new DatabaseException("Database not found: info.db");
	}

	/**
	 * Always throws a <tt>SupportInfoException</tt>. Must be caught by an
	 * exception handler.
	 * 
	 * @return Nothing - it always throws the exception.
	 * @throws SupportInfoException
	 *             Always thrown.
	 */
	@RequestMapping("/supportInfoException")
	String throwCustomException() throws Exception {
		logger.info("Throw SupportInfoException");
		throw new SupportInfoException("Custom exception occurred");
	}

	/**
	 * Simulates a database exception by always throwing
	 * <tt>UnhandledException</tt>. Must be caught by an exception handler.
	 * 
	 * @return Nothing - it always throws the exception.
	 * @throws UnhandledException
	 *             Always thrown.
	 */
	@RequestMapping("/unhandledException")
	String throwUnhandledException() throws Exception {
		logger.info("Throw UnhandledException");
		throw new UnhandledException("Some exception occurred");
	}


}