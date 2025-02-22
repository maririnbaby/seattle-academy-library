package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.RentService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class HistoryController {
	static Logger logger = LoggerFactory.getLogger(HistoryController.class);

	@Autowired
	private RentService rentService;

	/**
	 * 貸出履歴
	 * 
	 * @param model
	 * @return 履歴一覧
	 */

	@RequestMapping(value = "/historyBook", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String historyBook(Model model) {
		model.addAttribute("historyBookList", rentService.getHistoryBookList());

		return "history";
	}
}