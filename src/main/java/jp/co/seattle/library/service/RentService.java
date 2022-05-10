package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

//import jp.co.seattle.library.dto.BookDetailsInfo;


/**
 * 書籍サービス
 * 
 *  rentテーブルに関する処理を実装する
 */
@Service
public class RentService {
	final static Logger logger = LoggerFactory.getLogger(RentService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍を貸し出し登録する
     *
     * @param bookInfo 書籍情報
     */
    public void rentBook(int bookId) {

        String sql ="insert into rent (book_id) select " 
        + bookId 
        + " where NOT EXISTS (select book_id from rent where book_id=" 
        + bookId + ")";
        jdbcTemplate.update(sql);
    }
    
    public int countId() {
    String sql = "SELECT COUNT (book_id) FROM rent";
    int countId =jdbcTemplate.queryForObject(sql,int.class);
	return countId;
    }
    
    /**
     * 
     *書籍を返却する
     * 
     * @param 書籍情報
     */
    
    public void returnBook(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "DELETE FROM rent WHERE book_id = "+ bookId ;
        jdbcTemplate.update(sql);
       
    }
    
}
