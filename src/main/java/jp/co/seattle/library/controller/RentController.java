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

//import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentService;

@Controller // APIの入り口
public class RentController {
	final static Logger logger = LoggerFactory.getLogger(RentController.class);

	@Autowired
	private RentService rentService;
	@Autowired
	private BooksService booksService;

	/**
	 * 書籍情報を登録する
	 * 
	 * @param locale    ロケール情報
	 * @param title     書籍名
	 * @param author    著者名
	 * @param publisher 出版社
	 * @param file      サムネイルファイル
	 * @param model     モデル
	 * @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String rentBook(Locale locale, @RequestParam("bookId") int bookId, Model model) {
		logger.info("Welcome rentBook.java! The client locale is {}.", locale);

		int before = rentService.countId();

		// パラメータで受け取った書籍情報をDtoに格納する。
		rentService.rentBook(bookId);

		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		int after = rentService.countId();

		if (before == after) {
			model.addAttribute("rentError", "貸し出し中です。");

			return "details";
		}
		return "details";
	}

}
