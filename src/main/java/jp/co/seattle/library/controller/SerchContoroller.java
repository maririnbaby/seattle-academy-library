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

@Controller // APIの入り口
public class SerchContoroller {
	final static Logger logger = LoggerFactory.getLogger(SerchContoroller.class);

	@Autowired
	private BooksService booksService;

	/**
	 * 検索機能
	 * 
	 * @param locale
	 * @param title
	 * @param model
	 * @return home
	 */
	@Transactional
	@RequestMapping(value = "/serch", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String serch(Locale locale, @RequestParam("title") String title, Model model) {
		logger.info("Welcome serch.java! The client locale is {}.", locale);

		model.addAttribute("bookList", booksService.serch(title));
		return "home";
	}
}
