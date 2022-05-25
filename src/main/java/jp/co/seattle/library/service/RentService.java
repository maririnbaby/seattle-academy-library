package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.HistoryBookInfo;
import jp.co.seattle.library.rowMapper.HistoryBookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * rentテーブルに関する処理を実装する
 */
@Service
public class RentService {
	final static Logger logger = LoggerFactory.getLogger(RentService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 貸出登録
	 * 
	 * @param bookId
	 */
	public void rentBook(int bookId) {

		String sql = "insert into rent (book_id, rent_date) select " + bookId
				+ ", now() where NOT EXISTS (select book_id from rent where book_id=" + bookId + ")";
		jdbcTemplate.update(sql);
	}

	/**
	 * 貸出日と返却日の更新
	 * 
	 * @param bookId
	 */
	public void returnUpd(int bookId) {
		String sql = "UPDATE rent SET (rent_date, return_date) = (null, now()) where book_id =" + bookId
				+ "and exists (select book_id from rent where book_id =" + bookId + ")";
		jdbcTemplate.update(sql);
	}

	public void rentUpd(int bookId) {
		String sql = "UPDATE rent SET (rent_date, return_date) = (now(), null) where book_id =" + bookId
				+ "and exists (select book_id from rent where book_id =" + bookId + ")";
		jdbcTemplate.update(sql);
	}

	/**
	 * 履歴一覧表示
	 * 
	 * @param bookId
	 * @return 一覧
	 */

	public List<HistoryBookInfo> getHistoryBookList() {
		// TODO 取得したい情報を取得するようにSQLを修正
		List<HistoryBookInfo> historyBookList = jdbcTemplate.query(
				"SELECT books.id, books.title, rent.rent_date, rent.return_date FROM books left outer JOIN rent ON books.id = rent.book_id where rent_date is not null or return_date is not null",
				new HistoryBookInfoRowMapper());

		return historyBookList;
	}

	/**
	 * 初めて借りる時
	 * 
	 * @return カウント数
	 */
	public int exist(int bookId) {
		String sql = "SELECT COUNT (book_id) FROM rent where book_id=" + bookId;
		int exist = jdbcTemplate.queryForObject(sql, int.class);
		return exist;
	}

	/**
	 * 2回借りられないようにエラーを出したいので
	 * 貸出日付が入っていたら、の確認
	 * 
	 * @return　貸出日の存在
	 */
	public int rentDateCheck(int bookId) {
		String sql = "SELECT COUNT (rent_date) FROM rent where book_id=" + bookId;
		int rentDateCheck = jdbcTemplate.queryForObject(sql, int.class);
		return rentDateCheck;
	}
	
	public int returnDateCheck(int bookId) {
		String sql = "SELECT COUNT (return_date) FROM rent where book_id=" + bookId;
		int returnDateCheck = jdbcTemplate.queryForObject(sql, int.class);
		return returnDateCheck;
	}

	/**
	 * 
	 * 書籍を返却する
	 * 
	 * @param 書籍情報
	 */

	public void returnBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "DELETE FROM rent WHERE book_id = " + bookId;
		jdbcTemplate.update(sql);

	}

}
