package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentService;

/**
 * 削除コントローラー
 */
@Controller // APIの入り口
public class DeleteBookController {
	final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);

	@Autowired
	private BooksService booksService;
	@Autowired
	private RentService rentService;

	/**
	 * 対象書籍を削除する
	 *
	 * @param locale ロケール情報
	 * @param bookId 書籍ID
	 * @param model  モデル情報
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/deleteBook", method = RequestMethod.POST)
	public String deleteBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		logger.info("Welcome delete! The client locale is {}.", locale);

		int rentDateCheck = rentService.rentDateCheck(bookId);

		// 貸出されてれば
		if (rentDateCheck == 0) {
			// 削除する
			booksService.deleteBookInfo(bookId);
			booksService.deleteRentBook(bookId);
			// 書籍一覧に戻る
			model.addAttribute("bookList", booksService.getBookList());
			return "home";
		} else {
			model.addAttribute("lendingError", "貸し出し中なので削除できません。");
			// 画面やり直し
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
			return "details";
		}
	}
}
