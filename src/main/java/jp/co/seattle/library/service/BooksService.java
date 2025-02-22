package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"select id, title, author, publisher, publish_date, thumbnail_url from books order by title asc",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 書籍を検索する
	 * 
	 * @param title 
	 * @return 書籍リスト
	 */
	public List<BookInfo> serch(String title) {
		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate
				.query("select id, title, author, publisher, publish_date, thumbnail_url from books where title like '%"
						+ title + "%' order by title asc", new BookInfoRowMapper());

		return getedBookList;
	}

	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する

		String sql = "SELECT *, CASE WHEN rent_date is null then '貸出可' ELSE '貸出中' END as status FROM books left outer JOIN rent ON books.id = rent.book_id where books.id=" + bookId;
		
		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param maxId 最新の書籍ID
	 * @return 最新の書籍情報
	 */

	public int maxId() {
		String sql = "SELECT Max(id) FROM books";
		int maxId = jdbcTemplate.queryForObject(sql, int.class);
		return maxId;
	}

	/**
	 * 書籍情報を削除する
	 * 
	 * @param bookId
	 */

	public void deleteBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "DELETE FROM books WHERE id = " + bookId;
		jdbcTemplate.update(sql);

	}
	
	public void deleteRentBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "DELETE FROM rent WHERE book_id = " + bookId;
		jdbcTemplate.update(sql);

	}

	/**
	 * 書籍を借りる
	 * 
	 * @param bookId
	 * @return 書籍情報
	 */
	public int rent(int bookId) {

		try {
			// JSPに渡すデータを設定する
			String sql = "select book_id from rent where book_id = " + bookId;
			int rent = jdbcTemplate.queryForObject(sql, int.class);
			return rent;

		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 
	 * 書籍登録
	 * 
	 * @param bookInfo
	 */
	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,thumbnail_name,thumbnail_url, publish_date, ISBN, explanation, reg_date, upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getThumbnailName() + "','" + bookInfo.getThumbnailUrl() + "','" + bookInfo.getPublishDate()
				+ "','" + bookInfo.getIsbn() + "','" + bookInfo.getExplanation() + "'," + "now()," + "now())";

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を編集する
	 *
	 */

	public void editBook(BookDetailsInfo bookInfo) {
		String sql = "UPDATE books SET title ='" + bookInfo.getTitle() + "', author = '" + bookInfo.getAuthor()
				+ "', publisher = '" + bookInfo.getPublisher() + "', publish_date = '" + bookInfo.getPublishDate()
				+ "', thumbnail_name = '" + bookInfo.getThumbnailName() + "', thumbnail_url = '"
				+ bookInfo.getThumbnailUrl() + "', isbn ='" + bookInfo.getIsbn() + "', upd_date = now(), explanation ='"
				+ bookInfo.getExplanation() + "' Where id = " + bookInfo.getBookId();

		jdbcTemplate.update(sql);
	}

}
